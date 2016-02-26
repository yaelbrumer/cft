package impl;

import interfaces.WeightedClassifier;

import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public final class LogisticWeightedClassifier implements WeightedClassifier {

    private final Logistic classifier;

    public LogisticWeightedClassifier(){
        classifier = new Logistic();
    }

    public WeightedClassifier clone(){
        return new LogisticWeightedClassifier();
    }

    public final void train(final Instances dataSet) throws Exception {
        classifier.buildClassifier(dataSet);
    }

    public final String classify(final Instance instance) throws Exception {
        double value = classifier.classifyInstance(instance);
        return String.valueOf((int) value);
    }
}
