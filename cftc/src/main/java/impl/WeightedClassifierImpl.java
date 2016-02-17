package impl;

import exceptions.NotImplementedException;
import interfaces.WeightedClassifier;

import weka.core.Instance;
import weka.core.Instances;


/**
 * Created by eyapeleg on 2/13/2016.
 */
public final class WeightedClassifierImpl implements WeightedClassifier {

    public final void train(final Instances dataSet) throws Exception {
        throw new NotImplementedException("convert the double result into a t0/t1");
    }

    public final String classify(final Instance instance) throws Exception {
        throw new NotImplementedException("convert the double result into a t0/t1");
    }
}
