package interfaces;

import weka.classifiers.CostMatrix;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by eyapeleg on 2/12/2016.
 */
public interface WeightedClassifier {

    void train(Instances dataSet) throws Exception;

    String classify(Instance instance) throws Exception;
}
