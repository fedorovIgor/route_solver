package com.fedorov.tsp.solver.basicRoutes.models.database;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TSM")
@Data
public class TSM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "REGION_ID")
    private Long regionId;
    @Column(name = "TSM_NAME")
    private String tsmName;
    @Column(name = "TERR_ID")
    private Long territoryId;
    @Column(name = "ACTIVE")
    private Integer active;
    @Column(name = "START_POINT_ID")
    private Long startPointId;
    @Column(name = "FINISH_POINT_ID")
    private Long finishPointId;
    @Column(name = "WORK_TIME")
    private Long workTime;
    @Column(name = "NO_OF_AGENTS")
    private Integer noOfAgents;
    @Column(name = "PREFIX")
    private String prefix;
    @Column(name = "TOTAL_WORK_TIME")
    private Long totalWorkTime;


}
