package weka.classifiers.functions.cft;

import org.junit.Ignore;
import weka.classifiers.functions.cft.BaseTest;
import weka.classifiers.functions.cft.CftClassifier;
import weka.classifiers.functions.cft.impl.HammingLossCostCalculator;
import weka.classifiers.functions.cft.interfaces.CostCalculator;
import org.junit.Test;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CftClassifierTest extends BaseTest {

    @Ignore
    @Test
    public void TestBuildClassifierWithoutCostFunction()throws Exception{
        Classifier classifier = new Logistic();
        CftClassifier cftClassifier = new CftClassifier(classifier);

        String[] options = {"-L","6","-M","5"};
        cftClassifier.setOptions(options);

        final Instances instances = loadInstances(CftClassifier.class.getClassLoader().getResource("emotions.arff").getPath());
        cftClassifier.buildClassifier(instances);


    }

    @Ignore
    @Test
    public void TestBuildClassifierWithCostFunction() throws Exception {
        CostCalculator costCalculator = new HammingLossCostCalculator();
        Classifier classifier = new Logistic();
        CftClassifier cftClassifier = new CftClassifier(costCalculator, classifier);

        String[] options = {"-L","6","-M","5"};
        cftClassifier.setOptions(options);

        final Instances instances = loadInstances(CftClassifier.class.getClassLoader().getResource("emotions.arff").getPath());
        cftClassifier.buildClassifier(instances);
    }

}