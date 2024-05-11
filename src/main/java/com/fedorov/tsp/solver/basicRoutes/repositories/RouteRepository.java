package com.fedorov.tsp.solver.basicRoutes.repositories;

import com.fedorov.tsp.solver.basicRoutes.models.database.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT route FROM Route route JOIN FETCH route.customerActivities customerActivities WHERE route.id = :routeId")
    Optional<Route> findByIdWithCustomerActivity(@Param("routeId")Long routeId);

    @Query("SELECT route FROM Route route " +
            "LEFT JOIN FETCH route.routeDays routeDay " +
            "LEFT JOIN FETCH routeDay.visits visit")
    List<Route> findAllWithCustomerActivityAntVisits();

}
