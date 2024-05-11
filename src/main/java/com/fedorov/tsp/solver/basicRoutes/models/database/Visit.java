package com.fedorov.tsp.solver.basicRoutes.models.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "BASIC_VISITS")
@Data
public class Visit  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NDAY_IN_WEEK")
    private Integer dayInWeek;
    @Column(name = "POS")
    private Integer position;;
    @Column(name = "NWEEK")
    private Integer weekNumber;
    @Column(name = "CUST_ID")
    private Long customerId;
    @Column(name = "CUST_STRID")
    private Long customerStrId;
    @Column(name = "START_FINISH_ID")
    private Long warehouseId;
    @Column(name = "DIST_TO_NEXT")
    private Double distanceToNext;
    @Column(name = "TIME_TO_NEXT")
    private Double timeToNext;
    @Column(name = "TIME_ARR")
    private LocalDateTime timeArr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTE_DAY_ID")
    private RouteDay routeDay;

    public Boolean isHasRouteDay() {
        if (routeDay != null)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", dayInWeek=" + dayInWeek +
                ", position=" + position +
                ", weekNumber=" + weekNumber +
                ", customerId=" + customerId +
                ", customerStrId=" + customerStrId +
                ", warehouseId=" + warehouseId +
                ", distanceToNext=" + distanceToNext +
                ", timeToNext=" + timeToNext +
                ", timeArr=" + timeArr +
                '}';
    }
}
