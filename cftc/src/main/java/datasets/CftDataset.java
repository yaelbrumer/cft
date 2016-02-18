package datasets;

import exceptions.NotImplementedException;
import weka.core.*;
import java.util.*;

/**
 * Created by eyapeleg on 2/12/2016.
 */
public class CftDataset implements Iterable<CftInstance> {

    private Instances dataSet;
    private int numOfLables;
    private TreeMap<Integer, List<String>> yPredictedList;
    private TreeMap<Integer, String> yActualList;

    public CftDataset(final int numOfLables , Instances dataSet , TreeMap<Integer, List<String>> yPredictedList, TreeMap<Integer, String> yActualList) throws Exception {

        this.numOfLables = numOfLables;
        this.dataSet = dataSet;
        this.yActualList = yActualList;
        this.yPredictedList = yPredictedList;

    }

    public final Iterator<CftInstance> iterator(){
        return new Iterator<CftInstance>() {

            private int position = 0;
            private int predictionPosition = 0;

            public boolean hasNext() {
                return (yPredictedList.size() > position); //todo - verify indexing
            }

            public CftInstance next() {

                Instance instance = dataSet.instance(position);
                String prediction = yPredictedList.get(position).get(predictionPosition);
                String actual = yActualList.get(position);
                predictionPosition++;

                if(!(yPredictedList.get(position).size() > predictionPosition)){
                    predictionPosition=0;
                    position++;
                }

                return new CftInstance(instance,prediction,actual);
            }
        };
    }

    public final int getNumOfLables() {
        return numOfLables;
    }

    public void addMisclassified(CftInstance actual, String missClassified) {
        throw new NotImplementedException();
    }
}