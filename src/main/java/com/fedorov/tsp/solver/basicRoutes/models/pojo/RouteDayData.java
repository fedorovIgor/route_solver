package com.fedorov.tsp.solver.basicRoutes.models.pojo;

import com.fedorov.tsp.solver.basicRoutes.models.database.RouteDay;
import com.fedorov.tsp.solver.basicRoutes.models.database.Visit;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RouteDayData {

    private Long id;
    private Long routeId;
    private Integer week;
    private Integer dayInWeek;
    private Double routeLength;
    private Double routeDuration;
    private Long startPointId;
    private Long finishPointId;
    private LocalDateTime startTime;
    private String routeDescription;
    private String color;

    private List<VisitData> visits = new ArrayList<VisitData>();

    public RouteDayData(RouteDay routeDay) {
        this.id = routeDay.getId();
        this.week = routeDay.getWeek();
        this.dayInWeek = routeDay.getDayInWeek();
        this.routeLength = routeDay.getRouteLength();
        this.routeDuration = routeDay.getRouteDuration();
        this.startPointId = routeDay.getStartPointId();
        this.finishPointId = routeDay.getFinishPointId();
        this.startTime = routeDay.getStartTime();
        this.routeDescription = routeDay.getRouteDescription();
        this.color = routeDay.getColor();

    }

    public void addVisitsData(Collection<VisitData> visitData) {
        visits.addAll(visitData);
    }

    public void addVisitsData(VisitData visitData) {
        visits.add(visitData);
    }
}
