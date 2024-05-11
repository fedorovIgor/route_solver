package com.fedorov.tsp.solver.basicRoutes.services;

import com.fedorov.tsp.solver.basicRoutes.models.VisitPoint;
import com.fedorov.tsp.solver.basicRoutes.models.calculation.VisitPointBasic;
import com.fedorov.tsp.solver.basicRoutes.models.database.*;
import com.fedorov.tsp.solver.basicRoutes.models.pojo.RouteData;
import com.fedorov.tsp.solver.basicRoutes.models.pojo.RouteDayData;
import com.fedorov.tsp.solver.basicRoutes.models.pojo.VisitData;
import com.fedorov.tsp.solver.basicRoutes.repositories.*;
import com.fedorov.tsp.solver.basicRoutes.services.calculation.CalculatorTspVrp;
import com.graphhopper.ResponsePath;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RouteServiceBasic implements RouteServiceCalculate, RouteServiceRead {

    private final RouteRepository routeRepository;
    private final RouteDayRepository routeDayRepository;
    private final VisitRepository visitRepository;
    private final CustomerRepository customerRepository;
    private final WarehouseRepository warehouseRepository;
    private final CustomerActivityRepository customerActivityRepository;
    private final TSMRepository tsmRepository;

    private final CalculatorTspVrp calculatorTspVrp;
    private final static String REGION = "out3";


    public RouteServiceBasic(RouteRepository routeRepository, RouteDayRepository routeDayRepository,
                             VisitRepository visitRepository, CustomerRepository costumerRepository,
                             WarehouseRepository warehouseRepository, CustomerActivityRepository customerActivityRepository,
                             TSMRepository tsmRepository, CalculatorTspVrp calculatorTspVrp) {
        this.routeRepository = routeRepository;
        this.routeDayRepository = routeDayRepository;
        this.visitRepository = visitRepository;
        this.customerRepository = costumerRepository;
        this.warehouseRepository = warehouseRepository;
        this.customerActivityRepository = customerActivityRepository;
        this.tsmRepository = tsmRepository;
        this.calculatorTspVrp = calculatorTspVrp;
    }


    public List<RouteData> getAllRoute() {
        var routes = routeRepository.findAllWithCustomerActivityAntVisits();

        var result = new ArrayList<RouteData>();

        for (var route : routes) {
            var routeData = new RouteData(route);
            for (var day : route.getRouteDays()) {
                var dayData = new RouteDayData(day);
                for (var visit : day.getVisits()){
                    var visitData = new VisitData(visit);
                    visitData.setRouteId(routeData.getId());
                    dayData.addVisitsData(visitData);
                }
                routeData.addRouteDayData(dayData);
            }
            result.add(routeData);
        }

        return result;
    }

    private List<RouteDay> getRouteDayByRouteId(Long routeId) {
        var result = routeDayRepository.findByRouteIdWithVisits(routeId);

        return result;
    }

    public List<VisitData> getVisitsByRouteDayId(Long routeDayId) {
        var result = visitRepository.findByRouteDayId(routeDayId)
                .stream()
                .map(v -> new VisitData(v))
                .toList();

        return result;
    }

    public List<RouteDayData> calculateTSPForMonth(Long routeId) {
        var routeDays = routeDayRepository.findByRouteId(routeId);

        for (var routeDay : routeDays) {
            this.calculateTspForDay(routeDay.getId());
        }

        return routeDayRepository.findByRouteId(routeId)
                .stream()
                .map(rd -> new RouteDayData(rd))
                .toList();
    }

    public List<RouteDayData> calculateTSPForDays(List<Long> dayIds) {

        if (dayIds.size() == 0)
            return List.of();

        var resultList = new ArrayList<RouteDay>();

        var checkList = routeDayRepository.findByIdInWithVisits(dayIds);

        for (var id : dayIds) {
            this.calculateTspForDay(id);
            var day = routeDayRepository.findById(id).orElseThrow(() -> new RuntimeException("Can`t find day by id: " + id));
            resultList.add(day);
        }

        return resultList.stream()
                .map(rd -> new RouteDayData(rd))
                .toList();
    }


    public List<VisitData> calculateTspForDay(Long routeDayId) {

        var visits = visitRepository.findByRouteDayId(routeDayId);

        if (visits.size() == 0)
            return List.of();

        var costumersIds = visits.stream()
                .map(v -> v.getCustomerId())
                .toList();

        var costumers = customerRepository.findByIdIn(costumersIds)
                .stream()
                .collect(Collectors.toMap(Customer::getId, customer -> customer));

        var visitPointsQueue = new LinkedList<VisitPointBasic>();

        Collections.sort(visits, Comparator.comparing(Visit::getPosition));

        for (var visit : visits) {
            if (visit.getCustomerStrId() == null) {
                var warehouse = warehouseRepository.findById(visit.getWarehouseId()).get();
                visitPointsQueue.add(new VisitPointBasic(warehouse));
                continue;
            }

            visitPointsQueue.add(new VisitPointBasic(costumers.get(visit.getCustomerId())));
        }

        var visitMap = visits.stream()
                .distinct()
                .collect(Collectors.toMap(v -> v.getCustomerId(), v -> v, (existing, replacement) -> existing));

        visits.clear();

        for (int i = 0; i + 1 < visitPointsQueue.size(); i++) {
            var start = visitPointsQueue.get(i);
            var stop = visitPointsQueue.get(i+1);

            var path = calculatorTspVrp.calculateTspByVisits(start, stop, REGION);

            BigDecimal lengthDecimal= new BigDecimal(String.valueOf(path.getDistance()));
            var length = lengthDecimal.divide( new BigDecimal(1), 2, RoundingMode.HALF_DOWN);

            BigDecimal timeDecimal= new BigDecimal(String.valueOf(path.getTime()));
            var time = timeDecimal.divide( new BigDecimal(60_000), 2, RoundingMode.HALF_DOWN);

            var visit = new Visit();
            if (start.getCustomerId() == null ) {
                visit = visitMap.get(0L);
            }
            else
                visit = visitMap.get(start.getCustomerId());

            visit.setDistanceToNext(length.doubleValue());
            visit.setTimeToNext(time.doubleValue());

            visits.add(visit);
        }


        var fullPathResult = calculatorTspVrp.calculateTspByVisits(visitPointsQueue, REGION);

       visitRepository.saveAll(visits);

        updateRouteDayByVisits(routeDayId, fullPathResult);

        return visits.stream()
                .map(v -> new VisitData(v))
                .toList();
    }

    private void updateRouteDayByVisits(Long routeDayId, ResponsePath path) {

        var fullPath = new StringBuilder();
        for (var coordinate : path.getPoints()) {
            fullPath.append(coordinate.getLon());
            fullPath.append(" ");
            fullPath.append(coordinate.getLat());
            fullPath.append(",");
        }

        BigDecimal lengthDecimal= new BigDecimal(String.valueOf(path.getDistance()));
        var length = lengthDecimal.divide( new BigDecimal(1_000), 2, RoundingMode.HALF_DOWN);

        BigDecimal timeDecimal= new BigDecimal(String.valueOf(path.getTime()));
        var time = timeDecimal.divide( new BigDecimal(60_000), 2, RoundingMode.HALF_DOWN);

        RouteDay routeDay = routeDayRepository.findById(routeDayId).orElseThrow(
                () -> new RuntimeException("Can`t find day with id: " + routeDayId)
        );
        routeDay.setRouteLength(length.doubleValue());
        routeDay.setRouteDuration(time.doubleValue());
        routeDay.setRouteDescription(fullPath.toString());

        routeDayRepository.save(routeDay);
    }


    public void calculateVrpByDayIds(List<Long> dayIds) {
        if (dayIds.size() == 0)
            return;

        for (var dayId : dayIds) {
            var visits = visitRepository.findByRouteDayId(dayId);
            var costumerVisitIds = visits.stream()
                    .map(v -> v.getCustomerId())
                    .toList();
            var warehouseVisitIds = visits.stream()
                    .map(v -> v.getWarehouseId())
                    .toList();

            var visitPointsList = new ArrayList<VisitPoint>();

            visitPointsList.addAll(customerRepository.findByIdIn(costumerVisitIds));
            visitPointsList.addAll(warehouseRepository.findByIdIn(warehouseVisitIds));

            this.calculateVrpByVisits(visitPointsList, dayId);
        }
    }

    private void calculateVrpByVisits(List<VisitPoint> visitPoints, Long dayId) {

        if (visitPoints.size() == 0)
            return;

        var visitPointsBasic = visitPoints.stream()
                .map(c -> {
                    Class<? extends VisitPoint> classType = c.getClass();

                    if (classType.equals(Customer.class))
                        return new VisitPointBasic((Customer) c);

                    return new VisitPointBasic((Warehouse) c);
                })
                .toList();

        var solution = calculatorTspVrp.calculateVrpByVisits(visitPointsBasic, REGION);

        var visitList = new ArrayList<Visit>();

        var visits = visitRepository.findByRouteDayId(dayId);

        for (var rout : solution.getRoutes()) {
            var activities = rout.getActivities();
            var start = rout.getStart();
            var stop = rout.getEnd();

            var startStopVisits = visits.stream()
                    .filter(v -> v.getWarehouseId().equals((long)start.getLocation().getIndex()))
                    .toList();

            var startVisit = startStopVisits.get(0);
            startVisit.setPosition(0);
            visitList.add(startVisit);

            var stopVisit = startStopVisits.get(1);
            stopVisit.setPosition(activities.size()+1);
            visitList.add(stopVisit);

            for (int i = 0; i < activities.size(); i++) {

                var activity = activities.get(i);
                var key = (long)activity.getLocation().getIndex();
                var position = i + 1;

                var visitOptional = visits.stream()
                        .filter(v -> v.getCustomerId().equals(key))
                        .findFirst();

                if (visitOptional.isEmpty()) {
                    visitOptional = visits.stream()
                            .filter(v -> v.getWarehouseId().equals(key))
                            .findFirst();
                }

                var visit = visitOptional.get();

                visit.setPosition(position);

                visitList.add(visit);
            }
        }

        visitRepository.saveAll(visitList);
    }

    public void distributeVisitsByRoute(Long routeId) {
        var route = routeRepository.findByIdWithCustomerActivity(routeId).orElseThrow(() -> new RuntimeException("Cant find route by id: " + routeId));
        var routeDay = routeDayRepository.findByRouteId(routeId);
        var daysIds = routeDay.stream()
                .map(d -> d.getId())
                .toList();

        var visits = visitRepository.findByRouteDayIdIn(daysIds);
        var visitIds = visits.stream()
                .map(v -> v.getId())
                .toList();

        visitRepository.deleteAll(visits);

        var routeCustomerActivities = route.getCustomerActivities();

        Comparator<CustomerActivity> frequencyComparator = Comparator.comparingInt(CustomerActivity::getFrequency).reversed();
        Collections.sort(routeCustomerActivities, frequencyComparator);

        int numDays = 5;
        int totalFrequency = routeCustomerActivities.stream()
                .mapToInt(a -> a.getFrequency())
                .sum();

        double averageFrequency = totalFrequency / numDays;

        int currentGroupIndex = 0;
        int currentGroupFrequency = 0;

        var dayMap = new HashMap<Integer, List<CustomerActivity>>();

        for (var activity : routeCustomerActivities) {
            for (int i = 0; i < activity.getFrequency(); i++) {
                if (currentGroupFrequency + activity.getFrequency() > averageFrequency) {
                    currentGroupIndex++;
                    currentGroupFrequency = 0;
                }
                if (currentGroupIndex >= numDays)
                    currentGroupIndex = 0;


                for (int j = 0; j < 5; j++) {
                    dayMap.computeIfAbsent(currentGroupIndex, v -> new ArrayList<>());

                    var isExistInLayer = dayMap.get(currentGroupIndex).stream()
                            .filter(v -> v.getId().equals(activity.getId()))
                            .findFirst();
                    if (isExistInLayer.isEmpty()) {
                        break;
                    }
                    currentGroupIndex++;

                    if (currentGroupIndex == 5)
                        currentGroupIndex = 0;
                }


                dayMap.get(currentGroupIndex).add(activity);


                currentGroupFrequency += activity.getFrequency();
            }
        }

        for (var keyValue : dayMap.entrySet()) {
            var dayOptional = routeDay.stream()
                    .filter(d -> d.getDayInWeek().equals(keyValue.getKey() + 1))
                    .findFirst();

            if(dayOptional.isEmpty())
                continue;

            var day = dayOptional.get();

            var tsm = tsmRepository.findById(route.getTsmId()).get();

            var warehouses = warehouseRepository.findByIdIn(List.of(tsm.getStartPointId(), tsm.getFinishPointId()));

            if (warehouses.size() < 2){
                for (int i = 0; i < 2; i++) {
                    var visit = new Visit();
                    visit.setRouteDay(day);
                    visit.setDayInWeek(day.getDayInWeek());
                    visit.setWeekNumber(day.getWeek());
                    visit.setPosition(0);
                    visit.setWarehouseId(warehouses.get(0).getId());
                    visit.setCustomerId(0L);

                    visitRepository.save(visit);
                }
            }
            else {
                for (var warehouse : warehouses) {
                    var visit = new Visit();
                    visit.setRouteDay(day);
                    visit.setDayInWeek(day.getDayInWeek());
                    visit.setWeekNumber(day.getWeek());
                    visit.setPosition(0);
                    visit.setWarehouseId(warehouse.getId());
                    visit.setCustomerId(0L);

                    visitRepository.save(visit);
                }
            }


            for (var dayActivity : keyValue.getValue()) {
                var visit = new Visit();
                visit.setRouteDay(day);
                visit.setDayInWeek(day.getDayInWeek());
                visit.setCustomerId(dayActivity.getCustomerId());
                visit.setPosition(0);
                visit.setDistanceToNext(0.0);
                visit.setCustomerStrId(dayActivity.getCustomerStrId());
                visit.setWeekNumber(day.getWeek());
                visit.setWarehouseId(0L);

                visitRepository.save(visit);
            }
        }
    }

    public List<VisitPoint> getVitPointsByIds(List<Visit> visits) {
        var customerIds = visits.stream()
                .map(v -> v.getCustomerId())
                .distinct()
                .toList();

        var warehouseIds = visits.stream()
                .map(v -> v.getWarehouseId())
                .distinct()
                .toList();

        var customers = this.customerRepository.findByIdIn(customerIds);
        var warehouses = this.warehouseRepository.findByIdIn(warehouseIds);

        var resultList = new ArrayList<VisitPoint>();
        resultList.addAll(customers);
        resultList.addAll(warehouses);

        return resultList;
    }
}
