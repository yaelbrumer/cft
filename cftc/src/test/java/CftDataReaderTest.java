import datasets.CftDataset;
import org.junit.Test;

/**
 * Created by eyapeleg on 2/17/2016.
 */
public class CftDataReaderTest {

    @Test
    public void testLoadData() throws Exception {
        final CftDataReader cftDataReader = new CftDataReader();
        final int numOfLables = 6;
        final String filePath = CftDataReader.class.getClassLoader().getResource("emotions-train.arff").getPath();

        final CftDataset cftDataset = cftDataReader.readData(filePath, numOfLables);
    }
}