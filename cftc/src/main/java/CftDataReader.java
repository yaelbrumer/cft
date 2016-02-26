import datasets.CftDataset;
import datasets.Classification;
import mulan.core.ArgumentNullException;
import weka.core.Attribute;
import weka.core.FastVector;
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
final class CftDataReader {

    private final TreeMap<Integer,String> CreateLabelValue(final Instances dataSet, int L) {

        TreeMap<Integer, String> result = new TreeMap<Integer, String>();

        for(int i = 0; i< dataSet.numInstances(); ++i)
        {
            String label = toBitString(dataSet.instance(i), L);
            result.put(i, label);
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


    /// API
    final CftDataset readData(final String arffFilePath, final int numLabelAttributes) throws Exception {

        if (arffFilePath == null) {
            throw new ArgumentNullException("arffFilePath");
        } else if (numLabelAttributes < 2) {
            throw new IllegalArgumentException("The number of label attributes must me at least 2 or higher.");
        } else {

            // read data
            Instances data = loadInstances(arffFilePath);

            // create actual and predicted lists
            TreeMap<Integer, String> yActualList = CreateLabelValue(data, numLabelAttributes);
            TreeMap<Integer, String> yPredictedList = (TreeMap<Integer, String>)yActualList.clone();


            //remove lables
            for(int i=0; i<numLabelAttributes;i++){
                data.deleteAttributeAt(data.numAttributes()-1);
            }

            // set t attribute
            FastVector tValues = new FastVector();
            generateTvalues(tValues,"",0,numLabelAttributes);
            data.insertAttributeAt(new Attribute("t", tValues), data.numAttributes());


            // set b attribute
            FastVector bValues = new FastVector();
            bValues.addElement(Classification.RIGHT_CHILD);
            bValues.addElement(Classification.LEFT_CHILD);
            bValues.addElement(Classification.NONE);
            data.insertAttributeAt(new Attribute("b", bValues), data.numAttributes());

            // set class index
            data.setClassIndex(73);
            // create a CftDataset
            return new CftDataset(numLabelAttributes, data, yPredictedList, yActualList);
        }
    }

    private void generateTvalues(FastVector vector, String t,int i, int k){
        vector.addElement(t);
        if (!(i<k-1))
            return;
        generateTvalues(vector,t+Classification.LEFT_CHILD,i+1,k);
        generateTvalues(vector,t+Classification.RIGHT_CHILD,i+1,k);
    }


}

