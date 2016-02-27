import impl.HammingLossCostCalculator;
import interfaces.CostCalculator;
import org.junit.Test;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CftClassifierTest extends BaseTest {

    @Test
    public void TestBuildClassifierWithoutCostFunction()throws Exception{
        Classifier classifier = new Logistic();
        CftClassifier cftClassifier = new CftClassifier(classifier);

        String[] options = {"-L","6","-M","5"};
        cftClassifier.setOptions(options);

        final Instances instances = loadInstances(CftClassifier.class.getClassLoader().getResource("emotions.arff").getPath());
        cftClassifier.buildClassifier(instances);


    }

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