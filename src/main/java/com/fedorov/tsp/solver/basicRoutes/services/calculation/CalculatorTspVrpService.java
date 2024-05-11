package com.fedorov.tsp.solver.basicRoutes.services.calculation;

import com.fedorov.tsp.solver.basicRoutes.models.calculation.VisitPointBasic;
import com.fedorov.tsp.solver.basicRoutes.models.calculation.TransportDistanceTime;
import com.fedorov.tsp.solver.basicRoutes.services.map.GraphService;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Coordinate;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import com.graphhopper.util.shapes.GHPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@org.springframework.stereotype.Service
public class CalculatorTspVrpService implements CalculatorTspVrp {

    private final GraphService graphService;

    public CalculatorTspVrpService(GraphService graphService) {
        this.graphService = graphService;
    }

    public VehicleRoutingProblemSolution calculateVrpByVisits(List<VisitPointBasic> visits, String region) {

        // create job services
        var services = new ArrayList<Service>();
        for (var point : visits) {

            if (point.isWarehouse())
                continue;

            var location = new Location.Builder().newInstance()
                    .setId(point.getAddress())
                    .setIndex(point.getId().intValue())
                    .setCoordinate(Coordinate.newInstance(point.getLonX(), point.getLatY()))
                    .build();

            var service = Service.Builder.newInstance(point.getId().toString())
                    .setName(point.getAddress())
                    .addSizeDimension(0, 0)
                    .setLocation(location)
                    .build();

            services.add(service);
        }

        // get start stop point
        var startFinishPoint = visits.stream()
                .filter(v -> v.isWarehouse())
                .findFirst()
                .orElseThrow(() ->  new RuntimeException("Cant find start finish point"));

        //create start stop service
        var startStopLocation = new Location.Builder().newInstance()
                .setId(startFinishPoint.getAddress())
                .setIndex(startFinishPoint.getId().intValue())
                .setCoordinate(Coordinate.newInstance(startFinishPoint.getLonX(), startFinishPoint.getLatY()))
                .build();

        var stopStartService = Service.Builder.newInstance(startFinishPoint.getId().toString())
                .setName(startFinishPoint.getAddress())
                .addSizeDimension(0, 1)
                .setLocation(startStopLocation)
                .build();

        services.add(stopStartService);

        // calculate distance time matrix
        var timeMatrix = findMinPathBetweenClients(services, region);

        // Create a vehicle
        VehicleTypeImpl type = VehicleTypeImpl.Builder.newInstance("type")
                .build();
        VehicleImpl v = VehicleImpl.Builder.newInstance("v")
                .setType(type)
                .setReturnToDepot(false)
                .setStartLocation(stopStartService.getLocation())
                .build();

        // create cost
        var vehicleCost = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);
        for (var distance : timeMatrix) {
            vehicleCost.addTransportDistance(distance.getFromId(), distance.getToId(), distance.getDistance());
            vehicleCost.addTransportTime(distance.getFromId(), distance.getToId(), distance.getTime());
        }

        // Build the problem
        VehicleRoutingProblem vrp = VehicleRoutingProblem.Builder.newInstance()
                .addAllJobs(services)
                .addVehicle(v)
                .setFleetSize(VehicleRoutingProblem.FleetSize.FINITE)
                .setRoutingCost(vehicleCost.build())
                .build();


        var algorithm = Jsprit.Builder.newInstance(vrp)
                .addCoreStateAndConstraintStuff(true)
                .buildAlgorithm();

        var results = algorithm.searchSolutions();

        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(results);

        SolutionPrinter.print(vrp, bestSolution, SolutionPrinter.Print.VERBOSE);

        return bestSolution;
    }


    private List<TransportDistanceTime> findMinPathBetweenClients(List<Service> clients, String hopperKey) {

        var distances = new ArrayList<TransportDistanceTime>();

        var hopper = graphService.getGraph(hopperKey);

        for (int i = 0; i < clients.size(); i++) {
            var startClient = clients.get(i);
            var startVisitPoint = new VisitPointBasic(startClient);

            for (int j = 0; j < clients.size(); j++) {
                var stopClient = clients.get(j);
                if (startClient.equals(stopClient))
                    continue;

                var stopVisitPoint = new VisitPointBasic(stopClient);
                var path = this.calculateTSPCustomers(startVisitPoint, stopVisitPoint, hopper);

                var distance = new TransportDistanceTime(startVisitPoint.getId(),
                        stopVisitPoint.getId(),
                        path.getDistance(),
                        path.getTime(),
                        startVisitPoint.getName(),
                        stopVisitPoint.getName());

                distances.add(distance);
            }
        }

        return distances;
    }


    private ResponsePath calculateTSPCustomers(VisitPointBasic startVisit, VisitPointBasic stopVisit, GraphHopper hopper) {

        GHRequest req = new GHRequest(startVisit.getLatY(), startVisit.getLonX(),
                stopVisit.getLatY(), stopVisit.getLonX())
                .setProfile("car_custom")
                .setLocale(Locale.ENGLISH);

        GHResponse rsp = hopper.route(req);

        if (rsp.hasErrors())
            throw new RuntimeException(rsp.getErrors().toString());

        // use the best path, GHResponse
        ResponsePath path = rsp.getBest();

        return path;
    }

    public ResponsePath calculateTspByVisits(List<VisitPointBasic> visits, String region) {

        GraphHopper hopper = graphService.getGraph(region);

        // get start stop point
        var startFinishPoint = visits.stream()
                .filter(v -> v.isWarehouse())
                .findFirst()
                .orElseThrow(() ->  new RuntimeException("Cant find start finish point"));

        List<GHPoint> points = new ArrayList<>();
        points.add(new GHPoint(startFinishPoint.getLatY(), startFinishPoint.getLonX()));

        for (var visit : visits) {
            if (visit.isWarehouse())
                continue;
            points.add(new GHPoint(visit.getLatY(), visit.getLonX()));
        }
        points.add(new GHPoint(startFinishPoint.getLatY(), startFinishPoint.getLonX()));


        GHRequest req = new GHRequest(points);
        req.setProfile("car_custom");
        GHResponse rsp = hopper.route(req);

        var result = rsp.getBest();

        return result;
    }

    public ResponsePath calculateTspByVisits(VisitPointBasic startVisit, VisitPointBasic stopVisit, String region) {

        GraphHopper hopper = graphService.getGraph(region);

        List<GHPoint> points = new ArrayList<>();
        points.add(new GHPoint(startVisit.getLatY(), startVisit.getLonX()));
        points.add(new GHPoint(stopVisit.getLatY(), stopVisit.getLonX()));


        GHRequest req = new GHRequest(points);
        req.setProfile("car_custom");
        GHResponse rsp = hopper.route(req);

        var result = rsp.getBest();

        return result;
    }

}
