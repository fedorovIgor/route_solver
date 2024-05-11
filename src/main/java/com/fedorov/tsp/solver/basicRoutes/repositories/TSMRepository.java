package com.fedorov.tsp.solver.basicRoutes.repositories;

import com.fedorov.tsp.solver.basicRoutes.models.database.TSM;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TSMRepository extends JpaRepository<TSM, Long> {
}
