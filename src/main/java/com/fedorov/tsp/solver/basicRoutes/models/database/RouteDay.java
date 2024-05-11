package com.fedorov.tsp.solver.basicRoutes.models.database;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "BASIC_ROUTE_DAY")
@Data
public class RouteDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NWEEK")
    private Integer week;
    @Column(name = "NDAY_IN_WEEK")
    private Integer dayInWeek;
    @Column(name = "ROUTE_LENGTH")
    private Double routeLength;
    @Column(name = "ROUTE_DURATION")
    private Double routeDuration;
    @Column(name = "START_POINT_ID")
    private Long startPointId;
    @Column(name = "FINISH_POINT_ID")
    private Long finishPointId;
    @Column(name = "START_TIME")
    private LocalDateTime startTime;
    @Column(name = "ROUTE_DESCR")
    private String routeDescription;
    @Column(name = "COLOR")
    private String color;

    @OneToMany(mappedBy = "routeDay",  fetch = FetchType.LAZY)
    private Set<Visit> visits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTE_ID")
    private Route route;

    public Boolean isVisitsEmpty() {
        return visits == null || visits.isEmpty();
    }

    public Boolean isHasRoute() {
        if (route != null)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "RouteDay{" +
                "id=" + id +
                ", week=" + week +
                ", dayInWeek=" + dayInWeek +
                ", routeLength=" + routeLength +
                ", routeDuration=" + routeDuration +
                ", startPointId=" + startPointId +
                ", finishPointId=" + finishPointId +
                ", startTime=" + startTime +
                ", routeDescription='" + routeDescription + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteDay routeDay = (RouteDay) o;
        return Objects.equals(id, routeDay.id) && Objects.equals(week, routeDay.week) && Objects.equals(dayInWeek, routeDay.dayInWeek) && Objects.equals(routeLength, routeDay.routeLength) && Objects.equals(routeDuration, routeDay.routeDuration) && Objects.equals(startPointId, routeDay.startPointId) && Objects.equals(finishPointId, routeDay.finishPointId) && Objects.equals(startTime, routeDay.startTime) && Objects.equals(routeDescription, routeDay.routeDescription) && Objects.equals(color, routeDay.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, week, dayInWeek, routeLength, routeDuration, startPointId, finishPointId, startTime, routeDescription, color);
    }
}
