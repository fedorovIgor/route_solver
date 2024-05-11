package com.fedorov.tsp.solver.basicRoutes.controllers;


import com.fedorov.tsp.solver.basicRoutes.models.VisitPoint;
import com.fedorov.tsp.solver.basicRoutes.models.database.*;
import com.fedorov.tsp.solver.basicRoutes.models.pojo.RouteData;
import com.fedorov.tsp.solver.basicRoutes.models.pojo.RouteDayData;
import com.fedorov.tsp.solver.basicRoutes.models.pojo.VisitData;
import com.fedorov.tsp.solver.basicRoutes.services.RouteServiceBasic;
import com.fedorov.tsp.solver.basicRoutes.services.RouteServiceCalculate;
import com.fedorov.tsp.solver.basicRoutes.services.RouteServiceRead;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/route")
public class RouteController {

    private final RouteServiceRead routeServiceRead;
    private final RouteServiceCalculate routeServiceCalculate;

    public RouteController(RouteServiceRead routeServiceRead, RouteServiceCalculate routeServiceCalculate) {
        this.routeServiceRead = routeServiceRead;
        this.routeServiceCalculate = routeServiceCalculate;
    }


    @GetMapping
    public List<RouteData> getAllRoute() {
        return routeServiceRead.getAllRoute();
    }


    @GetMapping("/visit/{routeDayId}")
    public List<VisitData> getVisitsByRouteDayId(@PathVariable Long routeDayId) {
        return routeServiceRead.getVisitsByRouteDayId(routeDayId);
    }

    @PostMapping("customer")
    public List<VisitPoint> getVisitPointsByIds(@RequestBody List<Visit> visits) {
        return routeServiceRead.getVitPointsByIds(visits);
    }


    @PostMapping("calculate/day/{routeDayId}")
    public List<VisitData> calculateTSPForDay(@PathVariable Long routeDayId) {
        return routeServiceCalculate.calculateTspForDay(routeDayId);
    }

    @PostMapping("calculate/month/{routeId}")
    public List<RouteDayData> calculateTSPForMonth(@PathVariable Long routeId) {
        return routeServiceCalculate.calculateTSPForMonth(routeId);
    }

    @PostMapping("calculate/days")
    public List<RouteDayData> calculateTSPForDays(@RequestBody List<Long> dayIds) {
        return routeServiceCalculate.calculateTSPForDays(dayIds);
    }

    @PostMapping("calculate/vrp/visits")
    public void calculateVRPByDayId(@RequestBody List<Long> dayIds) {
        routeServiceCalculate.calculateVrpByDayIds(dayIds);
    }

    @PostMapping("distribute/route/{routeId}")
    public void distributeVisitsByRoute(@PathVariable Long routeId) {
        routeServiceCalculate.distributeVisitsByRoute(routeId);
    }
}

