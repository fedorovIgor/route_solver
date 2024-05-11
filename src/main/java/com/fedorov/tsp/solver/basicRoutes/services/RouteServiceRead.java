package com.fedorov.tsp.solver.basicRoutes.services;

import com.fedorov.tsp.solver.basicRoutes.models.VisitPoint;
import com.fedorov.tsp.solver.basicRoutes.models.database.Route;
import com.fedorov.tsp.solver.basicRoutes.models.database.RouteDay;
import com.fedorov.tsp.solver.basicRoutes.models.database.Visit;
import com.fedorov.tsp.solver.basicRoutes.models.pojo.RouteData;
import com.fedorov.tsp.solver.basicRoutes.models.pojo.RouteDayData;
import com.fedorov.tsp.solver.basicRoutes.models.pojo.VisitData;

import java.util.List;

public interface RouteServiceRead {

    List<RouteData> getAllRoute();
    List<VisitData> getVisitsByRouteDayId(Long routeDayId);
    List<VisitPoint> getVitPointsByIds(List<Visit> visits);

}

