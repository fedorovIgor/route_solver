package com.fedorov.tsp.solver.basicRoutes.models.calculation;

import com.fedorov.tsp.solver.basicRoutes.models.VisitPoint;
import com.fedorov.tsp.solver.basicRoutes.models.database.Customer;
import com.fedorov.tsp.solver.basicRoutes.models.database.Warehouse;
import com.graphhopper.jsprit.core.problem.job.Service;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VisitPointBasic extends VisitPoint {
    private Long id;
    private Long customerId;
    private Double lonX;
    private Double latY;
    private String name;
    private String address;
    private boolean isWarehouse;

    public VisitPointBasic(Service service) {
        this.id = (long) service.getLocation().getIndex();
        this.lonX = service.getLocation().getCoordinate().getX();
        this.latY = service.getLocation().getCoordinate().getY();
        this.name = service.getLocation().getId();
    }

    public VisitPointBasic(Customer customer) {
        id = customer.getId();
        lonX = customer.getLonX();
        latY = customer.getLatY();
        address = customer.getAddress();
        customerId = customer.getCustomerId();
        isWarehouse = false;
    }

    public VisitPointBasic(Warehouse warehouse) {
        id = warehouse.getId();
        lonX = warehouse.getLonX();
        latY = warehouse.getLatY();
        address = warehouse.getPointName();
        isWarehouse = true;
    }
}
