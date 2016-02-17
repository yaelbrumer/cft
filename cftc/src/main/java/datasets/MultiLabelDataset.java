package datasets;

import classifiers.Classification;
import core.MLUtils;
import exceptions.NotImplementedException;
import mulan.core.ArgumentNullException;
import weka.core.*;
import java.util.*;

/**
 * Created by eyapeleg on 2/12/2016.
 */
public class MultiLabelDataset implements Iterable<CftInstance> {

    private Instances dataSet;
    private int k;
    private TreeMap<Integer, String> yPredictedList;
    private TreeMap<Integer, String> yActualList;
    private HashSet<String> classes; //todo - remove

    public MultiLabelDataset(String arffFilePath, int numLabelAttributes) throws Exception {

        if(arffFilePath == null) {
            throw new ArgumentNullException("arffFilePath");
        } else if(numLabelAttributes < 2) {
            throw new IllegalArgumentException("The number of label attributes must me at least 2 or higher.");
        } else {
            Instances data = MLUtils.loadInstances(arffFilePath);
            this.k = numLabelAttributes;
            this.dataSet = data;
            this.yActualList = MLUtils.CreateLabelValue(dataSet, k);
            this.yPredictedList = new TreeMap<>(yActualList);
            this.classes = MLUtils.GetDistinctValues(yActualList);
        }
    }

    public int getK() {
        return k;
    }

//    private void insertLabelToDataset() throws Exception {
//
//        int lastIndex = dataSet.numAttributes();
//        Attribute att = new Attribute("Class",(FastVector) null);
//        dataSet.insertAttributeAt(att, lastIndex);
//        dataSet.setClassIndex(lastIndex);
//    }

    private MultiLabelDataset createInitialPredictionDataSet()
    {
        throw new NotImplementedException();
    }

    public MultiLabelDataset addClassificationToDataset(Classification Classification)
    {
        throw new NotImplementedException();
    }

    public Iterator<CftInstance> iterator(){
        return new Iterator<CftInstance>() {

            private int position = 0;

            public boolean hasNext() {
                return (dataSet.numInstances()>position);
            } //todo - verify indexing

            public CftInstance next() {
                Instance instance = dataSet.instance(position);
                String yPredicted = yPredictedList.get(position);
                position++;
                return new CftInstance(instance,yPredicted);
            }
        };
    }
}