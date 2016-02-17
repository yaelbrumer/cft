package datasets;

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
    private TreeMap<Integer, List<String>> yPredictedList;
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
            this.yPredictedList = MLUtils.CreatePredictedList(yActualList);
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

    public Iterator<CftInstance> iterator(){
        return new Iterator<CftInstance>() {

            private int position = 0;
            private int predictionPosition = 0;

            public boolean hasNext() {

                return (yPredictedList.size() > position);

                //if(yPredictedList.get(position).size() > predictionPosition)
              //      return true;

             //   return
            } //todo - verify indexing

            public CftInstance next() {
                Instance instance = dataSet.instance(position);

                String prediction;
                prediction=yPredictedList.get(position).get(predictionPosition);

                if(!(yPredictedList.get(position).size() > predictionPosition)){
                    prediction=yPredictedList.get(position).get(predictionPosition);
                    predictionPosition++;
                }
                else {
                    predictionPosition=0;
                    prediction=yPredictedList.get(position).get(predictionPosition);
                    predictionPosition++;
                }

                if(!(yPredictedList.get(position).size() > predictionPosition)){
                    position++;


                return new CftInstance(instance,prediction);
            }
        }
    }
}