package com.fedorov.tsp.solver.basicRoutes.repositories;

import com.fedorov.tsp.solver.basicRoutes.models.database.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    List<Warehouse> findByIdIn(List<Long> ids);

}
