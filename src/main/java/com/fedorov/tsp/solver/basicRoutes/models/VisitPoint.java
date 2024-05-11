package com.fedorov.tsp.solver.basicRoutes.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fedorov.tsp.solver.basicRoutes.models.database.Customer;
import com.fedorov.tsp.solver.basicRoutes.models.database.Warehouse;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Customer.class, name = "Customer"),
        @JsonSubTypes.Type(value = Warehouse.class, name = "Warehouse")
})
public abstract class VisitPoint {
    private Long id;
    private Long customerId;
    private Double lonX;
    private Double latY;

    public abstract Long getId();
    public abstract Double getLatY();
    public abstract Double getLonX();
    public abstract Long getCustomerId();
}
