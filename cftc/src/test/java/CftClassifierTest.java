import datasets.CftDataset;
import impl.CostCalculatorImpl;
import impl.WeightedClassifierImpl;
import interfaces.CostCalculator;
import interfaces.WeightedClassifier;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CftClassifierTest {

    @Test
    public void testTrain() throws Exception {
        CostCalculator costCalculator = new CostCalculatorImpl();
        WeightedClassifier weightedClassifier = new WeightedClassifierImpl();
        CftClassifier cftClassifier = new CftClassifier(costCalculator, weightedClassifier, 5);

        final int numOfLables = 6;
        final String filePath = CftClassifier.class.getClassLoader().getResource("emotions.arff").getPath();

        cftClassifier.buildClassifier(filePath,6);
    }

}