package impl;

import interfaces.CostCalculator;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CostCalculatorImpl implements CostCalculator {

    private Map<String,Map<String,Double>> costMatrix;


    public void genereteHammingDistanceCosts(List<String> classes){
        Map<String,Map<String,Double>> costMatrix = new TreeMap<String, Map<String, Double>>();
        for (String c1: classes) {
            Map<String,Double> costVector = new TreeMap<String, Double>();
            for (String c2: classes) {
                costVector.put(c2,getHammingDistance(c1,c2));
            }
            costMatrix.put(c1,costVector);
        }
        this.costMatrix=costMatrix;
    }

    private double getHammingDistance(String sequence1, String sequence2) {
        char[] s1 = sequence1.toCharArray();
        char[] s2 = sequence2.toCharArray();

        if(s1.length!= s2.length)
            throw new IllegalArgumentException("Strings length are not equal");

        int result = 0;
        for (int i=0; i<s1.length; i++) {
            if (s1[i] != s2[i]) result++;
        }
        return result/s1.length;
    }

}
