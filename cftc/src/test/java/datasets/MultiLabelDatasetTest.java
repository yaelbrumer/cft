package datasets;

import core.FileUtils;

import static org.junit.Assert.*;

/**
 * Created by ybrumer on 2/13/2016.
 */
public class MultiLabelDatasetTest {


    @org.junit.Test
    public void testMultiLabelDataset() throws Exception {
        String filePath = FileUtils.class.getClassLoader().getResource("emotions.arff").getPath();
        MultiLabelDataset multiLabelDataset = new MultiLabelDataset(filePath, 6);
    }

    @org.junit.Test
    public void testGetNumLabels() throws Exception {

    }

    @org.junit.Test
    public void testGetNumInstances() throws Exception {

    }

    @org.junit.Test
    public void testToBitString() throws Exception {

    }
}