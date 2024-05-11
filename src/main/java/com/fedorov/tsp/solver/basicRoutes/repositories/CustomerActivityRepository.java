package com.fedorov.tsp.solver.basicRoutes.repositories;

import com.fedorov.tsp.solver.basicRoutes.models.database.CustomerActivity;
import com.fedorov.tsp.solver.basicRoutes.models.database.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CustomerActivityRepository extends JpaRepository<CustomerActivity, Long> {
}
