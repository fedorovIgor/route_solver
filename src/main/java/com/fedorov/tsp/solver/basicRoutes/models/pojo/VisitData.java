package com.fedorov.tsp.solver.basicRoutes.models.pojo;

import com.fedorov.tsp.solver.basicRoutes.models.database.Visit;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitData {

    private Long id;
    private Integer dayInWeek;
    private Integer position;
    private Integer weekNumber;
    private Long customerId;
    private Long customerStrId;
    private Long warehouseId;
    private Double distanceToNext;
    private Double timeToNext;
    private LocalDateTime timeArr;
    private Long routeId;

    public VisitData(Visit visit) {
        this.id = visit.getId();
        this.dayInWeek = visit.getDayInWeek();
        this.position = visit.getPosition();
        this.weekNumber = visit.getWeekNumber();
        this.customerId = visit.getCustomerId();
        this.customerStrId = visit.getCustomerStrId();
        this.warehouseId = visit.getWarehouseId();
        this.distanceToNext = visit.getDistanceToNext();
        this.timeToNext = visit.getTimeToNext();
        this.timeArr = visit.getTimeArr();
    }
}
