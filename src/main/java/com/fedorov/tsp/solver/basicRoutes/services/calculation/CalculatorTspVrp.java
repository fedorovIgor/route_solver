package com.fedorov.tsp.solver.basicRoutes.services.calculation;

import com.fedorov.tsp.solver.basicRoutes.models.calculation.VisitPointBasic;
import com.graphhopper.ResponsePath;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;

import java.util.List;

public interface CalculatorTspVrp {

    VehicleRoutingProblemSolution calculateVrpByVisits(List<VisitPointBasic> visits, String region);
    ResponsePath calculateTspByVisits(List<VisitPointBasic> visits, String region);
    ResponsePath calculateTspByVisits(VisitPointBasic startVisit, VisitPointBasic stopVisit, String region);
}
