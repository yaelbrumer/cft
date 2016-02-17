package impl;

import exceptions.CostCalculatorException;
import interfaces.CostCalculator;
import weka.classifiers.CostMatrix;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CostCalculatorImpl implements CostCalculator {

    //private Map<String, Map<String, Double>> costMatrix;
    private CostMatrix wekaCostMatrix;
/*
    public void genereteHammingDistanceCosts(Set<String> classes) {
        Map<String, Map<String, Double>> costMatrix = new TreeMap<String, Map<String, Double>>();
        for (String c1 : classes) {
            Map<String, Double> costVector = new TreeMap<String, Double>();
            for (String c2 : classes) {
                costVector.put(c2, getHammingDistance(c1, c2));
            }
            costMatrix.put(c1, costVector);
        }
        this.costMatrix = costMatrix;
    }*/

    public double getCost(String predicted, String actual) {
/*
        if (costMatrix == null) {
            throw new CostCalculatorException("costMatrix wasn't initialized");
*/

        try {
            return getHammingDistance(predicted,actual);
        } catch (NullPointerException e) {
            throw new CostCalculatorException("matrix does not contain on of the required class: " + predicted + "," + actual, e);
        }
    }

    private double getHammingDistance(String sequence1, String sequence2) {
        try {
            if (sequence1.length() != sequence2.length()) {
                throw new IllegalArgumentException("Strings length are not equal");
            }

            char[] s1 = sequence1.toCharArray();
            char[] s2 = sequence2.toCharArray();

            int result = 0;
            for (int i = 0; i < s1.length; i++) {
                if (s1[i] != s2[i]) {
                    result++;
                }
            }
            return result / (double) s1.length;
        } catch (Exception e) {
            throw new CostCalculatorException(e);
        }
    }

    public CostMatrix getWekaCostMatrix(){
        return null;
    }

}
