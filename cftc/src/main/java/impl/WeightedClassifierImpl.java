package impl;

import exceptions.NotImplementedException;
import interfaces.WeightedClassifier;

import weka.core.Instance;
import weka.core.Instances;


/**
 * Created by eyapeleg on 2/13/2016.
 */
public class WeightedClassifierImpl implements WeightedClassifier {

    WeightedClassifier weightedClassifier;

    public void train(Instances dataSet) throws Exception {
        throw new NotImplementedException("convert the double result into a t0/t1");
    }

    public String classify(Instance instance) throws Exception {
        throw new NotImplementedException("convert the double result into a t0/t1");
    }
}
