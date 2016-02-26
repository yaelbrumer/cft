package impl;

import interfaces.WeightedClassifier;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public final class SvmWeightedClassifierImpl implements WeightedClassifier {

    private final LibSVM classifier;

    public SvmWeightedClassifierImpl(){
        classifier = new LibSVM();
    }

    public WeightedClassifier clone(){
        return new SvmWeightedClassifierImpl();
    }

    public final void train(final Instances dataSet) throws Exception {
        classifier.buildClassifier(dataSet);
    }

    public final String classify(final Instance instance) throws Exception {
        double value = classifier.classifyInstance(instance);
        return String.valueOf((int) value);
    }
}
