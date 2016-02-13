package datasets;

import classifiers.Classification;
import exceptions.NotImplementedException;
import mulan.core.ArgumentNullException;
import mulan.data.*;
import weka.core.*;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;

import java.util.*;

/**
 * Created by eyapeleg on 2/12/2016.
 */
public class MultiLabelDataset {

    public Instances getDataSet() {
        return dataSet;
    }

    public void setDataSet(Instances dataSet) {
        this.dataSet = dataSet;
    }

    public LabelsMetaData getLabelsMetaData() {
        return labelsMetaData;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    private Instances dataSet;
    private final LabelsMetaData labelsMetaData;
    private String Label;

    public Set<String> getClasses() {
        return classes;
    }

    private Set<String> classes = new TreeSet<String>();

    public MultiLabelDataset(String arffFilePath, int numLabelAttributes) throws Exception {
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

    public int getNumLabels() {
        return this.labelsMetaData.getNumLabels();
    }

    public int getNumInstances() {
        return this.dataSet.numInstances();
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

    public MultiLabelDataset addClassificationToDataset(Classification Classification)
    {
        throw new NotImplementedException();
    }
}