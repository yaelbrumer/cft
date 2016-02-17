package core;

import exceptions.NotImplementedException;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ybrumer on 2/17/2016.
 */
public class MLUtils {
    /**
     * ToBitString - returns a String representation of x = [0,0,1,0,1,0,0,0], e.g., "000101000".
     */
    public static String toBitString(Instance x, int L) {
        StringBuilder sb = new StringBuilder(L);
        int numOfAtt = x.numAttributes();

        int startIndex = numOfAtt - L;
        for(int i = startIndex; i < numOfAtt; i++) {
            sb.append((int)Math.round(x.value(i)));
        }
        return sb.toString();
    }

    /**
     * Load dataset from artff file
     */
    public static Instances loadInstances(String arffFilePath) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(arffFilePath);
        Instances data = source.getDataSet();

        return data;
    }

    public static TreeMap<Integer, String> CreateLabelValue(Instances dataSet, int L) {

        TreeMap<Integer, String> result = new TreeMap<Integer, String>();

        for(int i = 0; i< dataSet.numInstances(); ++i)
        {
            String label = toBitString(dataSet.instance(i), L);
            result.put(i, label);
        }

        return result;

    }

    public static HashSet<String> GetDistinctValues(TreeMap<Integer, String> yActualList) {

        throw new NotImplementedException("take out lambda expressions");
       /* HashSet<String> distinctValues = new HashSet<>();

        yActualList.entrySet().stream().collect(Collectors.groupingBy(
                Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())))
                .forEach((value,keys) -> distinctValues.add(value));

        return distinctValues;*/

    }

    public static TreeMap<Integer, List<String>> CreatePredictedList(TreeMap<Integer, String> yActualList) {

        TreeMap<Integer, List<String>> result = new TreeMap<Integer, List<String>>();

        for(Map.Entry<Integer,String> entry: yActualList.entrySet())
        {
            result.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
        return result;
    }
}
