package com.fedorov.tsp.solver.basicRoutes.models.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fedorov.tsp.solver.basicRoutes.models.VisitPoint;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "START_FINISH")
@Data
@EqualsAndHashCode(callSuper=false)
public class Warehouse extends VisitPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "REGION_ID")
    private Integer regionId;
    @Column(name = "POINT_NAME")
    private String pointName;
    @Column(name = "POINT_ADDR")
    private String pointAddress;
    @Column(name = "LON_X")
    private Double lonX;
    @Column(name = "LAT_Y")
    private Double latY;
    @Column(name = "POINT_STRID")
    private String pointStrId;
    @Column(name = "COMMENT")
    private String comment;

    @Override
    public Long getCustomerId() {
        return 0L;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", regionId=" + regionId +
                ", pointName='" + pointName + '\'' +
                ", pointAddress='" + pointAddress + '\'' +
                ", lonX=" + lonX +
                ", latY=" + latY +
                ", pointStrId='" + pointStrId + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
