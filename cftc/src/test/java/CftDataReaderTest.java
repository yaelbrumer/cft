import datasets.MultiLabelDataset;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by eyapeleg on 2/17/2016.
 */
public class CftDataReaderTest {

    @Test
    public void testLoadData() throws Exception {
        final CftDataReader cftDataReader = new CftDataReader();
        final int numOfLables = 6;

        final String filePath = CftDataReader.class.getClassLoader().getResource("emotions.arff").getPath();
        final MultiLabelDataset multiLabelDataset = cftDataReader.readData(filePath,numOfLables);
    }
}