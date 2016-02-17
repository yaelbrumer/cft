package datasets;

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
    private final LabelsMetaData labelsMetaData;
    private String Label;
    private List<String> yPredictedList;
    private List<String> yActualList;
    private Set<String> classes; //todo - remove

    public Set<String> getClasses() {
        return classes;
    }

    public MultiLabelDataset(String arffFilePath, int numLabelAttributes) throws Exception {

        classes = new TreeSet<String>();

        if(arffFilePath == null) {
            throw new ArgumentNullException("arffFilePath");
        } else if(numLabelAttributes < 2) {
            throw new IllegalArgumentException("The number of label attributes must me at least 2 or higher.");
        } else {
            Instances data = this.loadInstances(arffFilePath);
            LabelsMetaData labelsData = this.loadLabesMeta(data, numLabelAttributes);

            this.dataSet = data;
            this.labelsMetaData = labelsData;
        }

        insertLabelToDataset();
    }

    private void insertLabelToDataset() throws Exception {

        int lastIndex = dataSet.numAttributes();
        int k = labelsMetaData.getNumLabels();
        Attribute att = new Attribute("Class",(FastVector) null);
        dataSet.insertAttributeAt(att, lastIndex);

        //Save the combination of possible classes
        for(int i = 0; i< dataSet.numInstances(); ++i)
        {
            String label = toBitString(dataSet.instance(i), k);
            this.classes.add(label);
            dataSet.instance(i).setValue(lastIndex, label);
        }

        dataSet.setClassIndex(lastIndex);
    }

    /**
     * ToBitString - returns a String representation of x = [0,0,1,0,1,0,0,0], e.g., "000101000".
     */
    public static String toBitString(Instance x, int L) {
        StringBuilder sb = new StringBuilder(L);
        int numOfAtt = x.numAttributes();

        int startIndex = numOfAtt - L - 1;
        for(int i = startIndex; i < numOfAtt - 1; i++) {
            sb.append((int)Math.round(x.value(i)));
        }
        return sb.toString();
    }

    private Instances loadInstances(String arffFilePath) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(arffFilePath);
        Instances data = source.getDataSet();

        return data;
    }

    private LabelsMetaData loadLabesMeta(Instances data, int numLabels) throws InvalidDataFormatException {
        LabelsMetaDataImpl labelsData = new LabelsMetaDataImpl();
        int numAttributes = data.numAttributes();

        for(int index = numAttributes - numLabels; index < numAttributes; ++index) {
            String attrName = data.attribute(index).name();
            labelsData.addRootNode(new LabelNodeImpl(attrName));
        }

        if(labelsData.getNumLabels() < numLabels) {
            throw new InvalidDataFormatException("The names of label attributes are not unique.");
        } else {
            return labelsData;
        }
    }

    private MultiLabelDataset createInitialPredictionDataSet()
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

    public void addClassificationToDataset(){
        throw new NotImplementedException();
    }

    public int getNumLabels() {
        throw new NotImplementedException();
    }
}