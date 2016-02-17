import core.FileUtils;
import datasets.MultiLabelDataset;
import impl.CostCalculatorImpl;
import interfaces.CostCalculator;

import static org.junit.Assert.*;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CftTrainerTest {

    @org.junit.Test
    public void testTrain() throws Exception {
        CostCalculatorImpl costCalculator = new CostCalculatorImpl();
        MultiLabelDataset multiLabelDataset= readDataSet();
        costCalculator.genereteHammingDistanceCosts(multiLabelDataset.getClasses());
    }

    private MultiLabelDataset readDataSet() throws Exception {
        String filePath = FileUtils.class.getClassLoader().getResource("emotions.arff").getPath();
        MultiLabelDataset multiLabelDataset = new MultiLabelDataset(filePath, 6);
        return multiLabelDataset;
    }
}