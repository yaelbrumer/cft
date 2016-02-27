import datasets.CftDataset;
import impl.CostCalculatorImpl;
import interfaces.CostCalculator;
import org.junit.Test;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CftClassifierTest {

    @Test
    public void testTrain() throws Exception {
        CostCalculator costCalculator = new CostCalculatorImpl();
        Classifier classifier = new Logistic();
        CftClassifier cftClassifier = new CftClassifier(costCalculator, classifier, 8);
        final CftDataReader cftDataReader = new CftDataReader();


        final int numOfLables = 6;
        final String filePath = CftClassifier.class.getClassLoader().getResource("emotions-train.arff").getPath();
        final CftDataset cftDataset = cftDataReader.readData(filePath, numOfLables);

        cftClassifier.buildClassifier(cftDataset);

        Instances instances = cftDataset.getInstances();

        for(int i=0; i<instances.numInstances(); i++){
            Instance instance = instances.instance(i);
            double[] result = cftClassifier.distributionForInstance(instance);
        }

        final String testFilePath = CftClassifier.class.getClassLoader().getResource("emotions-test.arff").getPath();
        final CftDataset cftTestDataset = cftDataReader.readData(filePath, numOfLables);

    }

}