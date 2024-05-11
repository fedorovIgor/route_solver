package com.fedorov.tsp.solver.basicRoutes.models.calculation;

import lombok.Data;

@Data
public class TransportDistanceTime {
    private int fromIndex;
    private int toIndex;
    private String fromId;
    private String toId;
    private double distance;
    private double time;

    public TransportDistanceTime(Long fromIndex, Long toIndex, double distance, double time, String fromId, String toId) {
        this.fromIndex = fromIndex.intValue();
        this.toIndex = toIndex.intValue();
        this.distance = distance;
        this.time = time;
        this.fromId = fromId;
        this.toId = toId;
    }
}
