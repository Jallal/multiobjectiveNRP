package edu.umich.ISELab.evaluation.factory;

import edu.umich.ISELab.evaluation.Objective;
import edu.umich.ISELab.evaluation.qualityattributes.NumberOfNRPOptimization;

public class ObjectiveFactory {

    public static Objective getObjective(String name) {

        if (name.equalsIgnoreCase("Number Of Refactorings")) {
            return new NumberOfNRPOptimization();
        }
        /*if (name.equalsIgnoreCase("Coupling")) {
            return new Coupling();
        }
        if (name.equalsIgnoreCase("Cohesion")) {
            return new Cohesion();
        }
        if (name.equalsIgnoreCase("Complexity")) {
            return new Complexity();
        }*/

        return null;
    }

}
