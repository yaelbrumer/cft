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

    static CftDataset cftDataset;

    @BeforeClass
    public static void setUp() throws Exception {
        final CftDataReader cftDataReader = new CftDataReader();
        final int numOfLables = 6;

        final String filePath = CftDataReader.class.getClassLoader().getResource("emotions.arff").getPath();
        cftDataset = cftDataReader.readData(filePath, numOfLables);
    }

    @Test
    public void testTrain() throws Exception {
        CostCalculator costCalculator = new CostCalculatorImpl();
        WeightedClassifier weightedClassifier = new WeightedClassifierImpl();
        CftClassifier cftClassifier = new CftClassifier(costCalculator, weightedClassifier, 5);
        cftClassifier.buildClassifier(cftDataset);
    }

}