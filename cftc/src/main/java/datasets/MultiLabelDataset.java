package datasets;

import classifiers.Classification;
import core.MLUtils;
import exceptions.NotImplementedException;
import mulan.core.ArgumentNullException;
import mulan.data.*;
import weka.core.*;
import weka.core.converters.ConverterUtils;
import java.util.*;

/**
 * Created by eyapeleg on 2/12/2016.
 */
public class MultiLabelDataset implements Iterable<CftInstance> {

    private Instances dataSet;
    private int k;
    private String Label;
    private List<String> yPredictedList;
    private List<String> yActualList;
    private Set<String> classes; //todo - remove

    public Set<String> getClasses() {
        return classes;
    }

    public int getK() {
        return k;
    }

    public MultiLabelDataset(String arffFilePath, int numLabelAttributes) throws Exception {

        if(arffFilePath == null) {
            throw new ArgumentNullException("arffFilePath");
        } else if(numLabelAttributes < 2) {
            throw new IllegalArgumentException("The number of label attributes must me at least 2 or higher.");
        } else {
            Instances data = MLUtils.loadInstances(arffFilePath);
            this.k = numLabelAttributes;
            this.dataSet = data;
        }
        classes = new TreeSet<String>();
        insertLabelToDataset();
    }

    private void insertLabelToDataset() throws Exception {

        int lastIndex = dataSet.numAttributes();
        Attribute att = new Attribute("Class",(FastVector) null);
        dataSet.insertAttributeAt(att, lastIndex);

        //Save the combination of possible classes
        for(int i = 0; i< dataSet.numInstances(); ++i)
        {
            String label = MLUtils.toBitString(dataSet.instance(i), k);
            this.classes.add(label);
            dataSet.instance(i).setValue(lastIndex, label);
        }

        dataSet.setClassIndex(lastIndex);
    }

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