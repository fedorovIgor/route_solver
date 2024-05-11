package com.fedorov.tsp.solver.basicRoutes.models.database;

import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Entity
@Table(name = "BASIC_ROUTES")
@Data
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "REGION_ID")
    private Long regionId;
    @Column(name = "ACTIVITY_ID")
    private Long activityId;
    @Column(name = "ROUTE_NUM")
    private Integer routeNumber;
    @Column(name = "TSM_ID")
    private Long tsmId;
    @Column(name = "AGENT_ID")
    private Long agentId;
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Column(name = "ROUTE_NAME")
    private String routeName;
    @Column(name = "START_POINT_ID")
    private Integer startPointId;
    @Column(name = "FINISH_POINT_ID")
    private Integer finishPointId;

    @OneToMany(mappedBy = "route",  fetch = FetchType.LAZY)
    private Set<RouteDay> routeDays = new HashSet<>();

    @OneToMany(mappedBy = "route",  fetch = FetchType.LAZY)
    private List<CustomerActivity> customerActivities = new ArrayList<CustomerActivity>();

    public Boolean isCustomerActivitiesEmpty() {
        return customerActivities == null ||  customerActivities.isEmpty();
    }


    @Override
    public String toString() {
        return "Route{" +
                "finishPointId=" + finishPointId +
                ", startPointId=" + startPointId +
                ", routeName='" + routeName + '\'' +
                ", groupId=" + groupId +
                ", agentId=" + agentId +
                ", tsmId=" + tsmId +
                ", routeNumber=" + routeNumber +
                ", activityId=" + activityId +
                ", regionId=" + regionId +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id) && Objects.equals(regionId, route.regionId) && Objects.equals(activityId, route.activityId) && Objects.equals(routeNumber, route.routeNumber) && Objects.equals(tsmId, route.tsmId) && Objects.equals(agentId, route.agentId) && Objects.equals(groupId, route.groupId) && Objects.equals(routeName, route.routeName) && Objects.equals(startPointId, route.startPointId) && Objects.equals(finishPointId, route.finishPointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, regionId, activityId, routeNumber, tsmId, agentId, groupId, routeName, startPointId, finishPointId);
    }
}
