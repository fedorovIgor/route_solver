package com.fedorov.tsp.solver.basicRoutes.services;

import com.fedorov.tsp.solver.basicRoutes.models.pojo.RouteDayData;
import com.fedorov.tsp.solver.basicRoutes.models.pojo.VisitData;

import java.util.List;

public interface RouteServiceCalculate {

    List<RouteDayData> calculateTSPForMonth(Long routeId);
    List<RouteDayData> calculateTSPForDays(List<Long> dayIds);
    List<VisitData> calculateTspForDay(Long routeDayId);
    void calculateVrpByDayIds(List<Long> dayIds);
    void distributeVisitsByRoute(Long routeId);
}
