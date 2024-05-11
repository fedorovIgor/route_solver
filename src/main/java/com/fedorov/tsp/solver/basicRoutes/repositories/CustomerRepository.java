package com.fedorov.tsp.solver.basicRoutes.repositories;

import com.fedorov.tsp.solver.basicRoutes.models.database.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByIdIn(List<Long> ids);
}
