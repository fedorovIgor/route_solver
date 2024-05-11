package com.fedorov.tsp.solver.basicRoutes.models.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "CUST_ACTIVITY")
@Data
public class CustomerActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "CUST_ID")
    private Long customerId;
    @Column(name = "ACTIVITY_ID")
    private Long activityId;
    @Column(name = "CUST_STRID")
    private Long customerStrId;
    @Column(name = "TSM_ID")
    private Long tsmId;
    @Column(name = "FREQ")
    private Integer frequency;
    @Column(name = "T2V")
    private Long t2V;
    @Column(name = "RULE_CODE")
    private Integer ruleCode;
    @Column(name = "CATEGORY_ID")
    private Integer categoryId;
    @Column(name = "SALES")
    private Double sales;
    @Column(name = "NOVISIT")
    private Integer noVisit;
    @Column(name = "VISIT_PATTERN_ID")
    private Integer visitPatternId;
    @Column(name = "FREQ_MOD")
    private Integer frequencyMode;
    @Column(name = "T2V_MOD")
    private Integer t2VMod;
    @Column(name = "TIME_BEG")
    private LocalDateTime timeBegin;
    @Column(name = "TIME_END")
    private LocalDateTime timeEnd;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTE_ID")
    private Route route;

    @Override
    public String toString() {
        return "CustomerActivity{" +
                "timeEnd=" + timeEnd +
                ", timeBegin=" + timeBegin +
                ", t2VMod=" + t2VMod +
                ", frequencyMode=" + frequencyMode +
                ", visitPatternId=" + visitPatternId +
                ", noVisit=" + noVisit +
                ", sales=" + sales +
                ", categoryId=" + categoryId +
                ", ruleCode=" + ruleCode +
                ", t2V=" + t2V +
                ", frequency=" + frequency +
                ", tsmId=" + tsmId +
                ", customerStrId=" + customerStrId +
                ", activityId=" + activityId +
                ", customerId=" + customerId +
                ", id=" + id +
                '}';
    }
}
