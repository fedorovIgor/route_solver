package com.fedorov.tsp.solver.basicRoutes.services.map;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.util.GHUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GraphHopperService implements GraphService{

    private Map<String, GraphHopper> graphHoppers;
    @Value("${constant.map.mapFolder}")
    private String mapPath;
    @Value("${constant.map.cashFolder}")
    private String cashPath;

    public GraphHopperService() {
        this.graphHoppers = new ConcurrentHashMap<String, GraphHopper>();
    }

    public GraphHopper getGraph(String mapName) {

        if (mapName.isEmpty())
            throw new RuntimeException("map name can`t be empty");

        if (!this.graphHoppers.containsKey(mapName))
            this.createHopperInstance(mapName);

        return graphHoppers.get(mapName);
    }

    private void createHopperInstance(String mapName) {

        var hopper = new GraphHopper();

        var fileString = mapPath + mapName + ".osm.pbf";

        File f = new File(fileString);
        if(!f.exists()) {
            throw new RuntimeException("Cant find map by path: " + fileString);
        }

        hopper.setOSMFile(fileString);
        hopper.setGraphHopperLocation(cashPath);
        hopper.setEncodedValuesString("car_access, car_average_speed");
        hopper.setProfiles(new Profile("car_custom").setCustomModel(GHUtility.loadCustomModelFromJar("car.json")));
        hopper.importOrLoad();

        graphHoppers.put(mapName, hopper);
    }
}
