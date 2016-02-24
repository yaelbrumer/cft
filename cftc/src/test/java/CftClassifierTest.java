import datasets.CftDataset;
import impl.CostCalculatorImpl;
import impl.WeightedClassifierImpl;
import interfaces.CostCalculator;
import interfaces.WeightedClassifier;
import org.junit.BeforeClass;
import org.junit.Test;
import weka.core.Instances;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CftClassifierTest {

    @Test
    public void testTrain() throws Exception {
        CostCalculator costCalculator = new CostCalculatorImpl();
        WeightedClassifier weightedClassifier = new WeightedClassifierImpl();
        CftClassifier cftClassifier = new CftClassifier(costCalculator, weightedClassifier, 5);
        final CftDataReader cftDataReader = new CftDataReader();


        final int numOfLables = 6;
        final String filePath = CftClassifier.class.getClassLoader().getResource("emotions.arff").getPath();
        final CftDataset cftDataset = cftDataReader.readData(filePath, numOfLables);

        cftClassifier.buildClassifier(filePath,6);

        //remove lables
        Instances instances = cftDataset.getInstances();
        for(int i=0; i< numOfLables -1 ;i++){
            instances.deleteAttributeAt(instances.numAttributes() - 1);
        }

        final String s = cftClassifier.classifyInstance(instances.instance(0));
    }

}