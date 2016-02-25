package impl;

import exceptions.NotImplementedException;
import interfaces.WeightedClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;


/**
 * Created by eyapeleg on 2/13/2016.
 */
public final class WeightedClassifierImpl implements WeightedClassifier {

    //private final weka.classifiers.functions.LibSVM libSVM;
    private final weka.classifiers.trees.J48 classifier;

    public WeightedClassifierImpl(){
        //libSVM = new LibSVM();
        classifier = new weka.classifiers.trees.J48();
    }

    public final void train(final Instances dataSet) throws Exception {
        //libSVM.buildClassifier(dataSet);
        classifier.buildClassifier(dataSet);
    }

    public final String classify(final Instance instance) throws Exception {
        //libSVM.classifyInstance(instance);
        double value = classifier.classifyInstance(instance);
        return String.valueOf((int) value);
    }
}
