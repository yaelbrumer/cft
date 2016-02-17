import datasets.MultiLabelDataset;
import mulan.core.ArgumentNullException;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by eyapeleg on 2/17/2016.
 */
public final class CftDataReader {

    private final TreeMap<Integer,String> CreateLabelValue(final Instances dataSet, int L) {

        TreeMap<Integer, String> result = new TreeMap<Integer, String>();

        for(int i = 0; i< dataSet.numInstances(); ++i)
        {
            String label = toBitString(dataSet.instance(i), L);
            result.put(i, label);
        }

        return result;

    }

    private final TreeMap<Integer, List<String>> CreatePredictedList(TreeMap<Integer, String> yActualList) {

        TreeMap<Integer, List<String>> result = new TreeMap<Integer, List<String>>();

        for(Map.Entry<Integer,String> entry: yActualList.entrySet())
        {
            result.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }
        return result;
    }


    private String toBitString(final Instance x,final int L) {
        StringBuilder sb = new StringBuilder(L);
        int numOfAtt = x.numAttributes();

        int startIndex = numOfAtt - L;
        for(int i = startIndex; i < numOfAtt; i++) {
            sb.append((int)Math.round(x.value(i)));
        }
        return sb.toString();
    }

    private Instances loadInstances(final String arffFilePath) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(arffFilePath);
        Instances data = source.getDataSet();

        return data;
    }


    //todo - check if required...
    private  Instances LoadData(String resourceName) throws Exception {

        String filePath = CftDataReader.class.getClassLoader().getResource(resourceName).getPath();
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(filePath);
        Instances data = source.getDataSet();

        return data;
    }

    /// API
    public final MultiLabelDataset readData(final String arffFilePath,final int numLabelAttributes) throws Exception {

        if (arffFilePath == null) {
            throw new ArgumentNullException("arffFilePath");
        } else if (numLabelAttributes < 2) {
            throw new IllegalArgumentException("The number of label attributes must me at least 2 or higher.");
        } else {

            Instances data = loadInstances(arffFilePath);
            TreeMap<Integer, String> yActualList = CreateLabelValue(data, numLabelAttributes);
            TreeMap<Integer, List<String>> yPredictedList = CreatePredictedList(yActualList);
            CreatePredictedList(yActualList);

            return new MultiLabelDataset(numLabelAttributes, data, yPredictedList, yActualList);
        }
    }

}

