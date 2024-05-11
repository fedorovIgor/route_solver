package com.fedorov.tsp.solver.basicRoutes.repositories;

import com.fedorov.tsp.solver.basicRoutes.models.database.RouteDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteDayRepository extends JpaRepository<RouteDay, Long> {

   List<RouteDay> findByRouteId(Long routeId);

   @Query("SELECT DISTINCT routeDay FROM RouteDay routeDay LEFT JOIN FETCH routeDay.visits visit WHERE routeDay.route.id = :routeId")
   List<RouteDay> findByRouteIdWithVisits(@Param("routeId") Long routeId);

   @Query("SELECT routeDay FROM RouteDay routeDay LEFT JOIN FETCH routeDay.visits visit WHERE routeDay.id IN :ids")
   List<RouteDay> findByIdInWithVisits(@Param("ids")List<Long> ids);

   List<RouteDay> findByIdIn(List<Long> dayIds);
}
