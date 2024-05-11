package com.fedorov.tsp.solver.basicRoutes.services.map;

import com.graphhopper.GraphHopper;

public interface GraphService {

    GraphHopper getGraph(String mapName);
}
