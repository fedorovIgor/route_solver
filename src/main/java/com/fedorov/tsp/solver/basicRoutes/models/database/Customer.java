package com.fedorov.tsp.solver.basicRoutes.models.database;

import com.fedorov.tsp.solver.basicRoutes.models.VisitPoint;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "CUSTOMERS")
@Data
@EqualsAndHashCode(callSuper=false)
public class Customer extends VisitPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "REGION_ID")
    private Long regionId;
    @Column(name = "CUST_STRID")
    private Long customerStrId;
    @Column(name = "CUST_STRID2")
    private Long customerStrId2;
    @Column(name = "CUST_NAME")
    private String customerName;
    @Column(name = "ADDR")
    private String address;
    @Column(name = "LON_X")
    private Double lonX;
    @Column(name = "LAT_Y")
    private Double latY;
    @Column(name = "LINKED")
    private Integer link;

    @Override
    public Long getCustomerId() {
        return id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", regionId=" + regionId +
                ", customerStrId=" + customerStrId +
                ", customerStrId2=" + customerStrId2 +
                ", customerName='" + customerName + '\'' +
                ", address='" + address + '\'' +
                ", lonX=" + lonX +
                ", latY=" + latY +
                ", link=" + link +
                '}';
    }
}
