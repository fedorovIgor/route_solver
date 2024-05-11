package com.fedorov.tsp.solver;

import org.openstreetmap.osmosis.core.Osmosis;
import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
