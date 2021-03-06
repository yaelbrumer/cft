package weka.classifiers.functions.cft;

import weka.classifiers.functions.cft.model.CftDataset;
import weka.classifiers.functions.cft.model.Classification;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

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

    private void generateTvalues(FastVector vector, String t,int i, int k){
        vector.addElement(t);
        if (!(i<k-1))
            return;
        generateTvalues(vector,t+Classification.LEFT_CHILD,i+1,k);
        generateTvalues(vector,t+Classification.RIGHT_CHILD,i+1,k);
    }

    /// API
    final CftDataset readData(Instances instances, int numLabelAttributes ) throws Exception {

    // create actual and predicted lists
        TreeMap<Integer, String> yActualList = CreateLabelValue(instances, numLabelAttributes);
        TreeMap<Integer, String> yPredictedList = (TreeMap<Integer, String>)yActualList.clone();


        //remove lables
        for(int i=0; i<numLabelAttributes;i++){
            instances.deleteAttributeAt(instances.numAttributes()-1);
        }

        // set t attribute
        FastVector tValues = new FastVector();
        generateTvalues(tValues,"",0,numLabelAttributes);
        instances.insertAttributeAt(new Attribute("t", tValues), instances.numAttributes());


        // set b attribute
        FastVector bValues = new FastVector();
        bValues.addElement(Classification.LEFT_CHILD);
        bValues.addElement(Classification.RIGHT_CHILD);
        bValues.addElement(Classification.NONE);
        instances.insertAttributeAt(new Attribute("b", bValues), instances.numAttributes());

        // set class index
        instances.setClassIndex(instances.numAttributes() - 1);

        // create a CftDataset
        return new CftDataset(numLabelAttributes, instances, yPredictedList, yActualList);
    }
}

