import datasets.CftDataset;
import impl.CostCalculatorImpl;
import interfaces.CostCalculator;
import org.junit.Test;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CftClassifierTest extends BaseTest {

    @Test
    public void testTrain() throws Exception {
        CostCalculator costCalculator = new CostCalculatorImpl();
        Classifier classifier = new Logistic();
        CftClassifier cftClassifier = new CftClassifier(costCalculator, classifier);

        String[] optionsBefore = cftClassifier.getOptions();
        String[] options = {"-L","6","-M","5"};
        cftClassifier.setOptions(options);
        String[] optionsAfter = cftClassifier.getOptions();


        final Instances instances = loadInstances(CftClassifier.class.getClassLoader().getResource("emotions.arff").getPath());
        cftClassifier.buildClassifier(instances);

        for(int i=0; i<instances.numInstances(); i++){
            Instance instance = instances.instance(i);
            double[] result = cftClassifier.distributionForInstance(instance);
        }

    }

}