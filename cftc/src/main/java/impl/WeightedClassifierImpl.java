package impl;

import exceptions.NotImplementedException;
import interfaces.WeightedClassifier;

import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;


/**
 * Created by eyapeleg on 2/13/2016.
 */
public final class WeightedClassifierImpl implements WeightedClassifier {

    private final weka.classifiers.functions.LibSVM libSVM;

    public WeightedClassifierImpl(){
        libSVM = new LibSVM();
    }

    public final void train(final Instances dataSet) throws Exception {
        libSVM.buildClassifier(dataSet);
    }

    public final String classify(final Instance instance) throws Exception {
        libSVM.classifyInstance(instance);
        return new String(); //todo - modify prediction to fit classes
    }
}
