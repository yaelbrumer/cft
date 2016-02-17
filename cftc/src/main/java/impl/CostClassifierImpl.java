package impl;

import classifiers.Classification;
import exceptions.NotImplementedException;
import interfaces.CostClassifier;
import weka.classifiers.CostMatrix;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CostClassifierImpl implements CostClassifier {

    CostSensitiveClassifier costSensitiveClassifier;

    public void train(Instances dataSet, CostMatrix costMatrix) throws Exception {

        this.costSensitiveClassifier = new CostSensitiveClassifier();
        costSensitiveClassifier.setCostMatrix(costMatrix);
        this.costSensitiveClassifier.buildClassifier(dataSet);
    }

    public Classification classify(Instance instance) throws Exception {
        double classificationResult = costSensitiveClassifier.classifyInstance(instance);
        throw new NotImplementedException("convert the double result into a t0/t1");
    }
}
