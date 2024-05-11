package com.fedorov.tsp.solver.basicRoutes.models.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fedorov.tsp.solver.basicRoutes.models.database.Route;
import com.fedorov.tsp.solver.basicRoutes.models.database.RouteDay;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RouteData {

    private Long id;
    private Long regionId;
    private Long activityId;
    private Integer routeNumber;
    private Long tsmId;
    private Long agentId;
    private Long groupId;
    private String routeName;
    private Integer startPointId;
    private Integer finishPointId;

    private List<RouteDayData> dayRoutes = new ArrayList<>();

    public RouteData (Route route) {
        id = route.getId();
        regionId = route.getRegionId();
        routeNumber = route.getRouteNumber();
        tsmId = route.getTsmId();
        agentId = route.getAgentId();
        groupId = route.getGroupId();
        routeName = route.getRouteName();
        startPointId = route.getStartPointId();
        finishPointId = route.getFinishPointId();
    }

    public void addRouteDayData(Collection<RouteDayData> days) {
        dayRoutes.addAll(days);
    }

    public void addRouteDayData(RouteDayData day) {
        dayRoutes.add(day);
    }
}
