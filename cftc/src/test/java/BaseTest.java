import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * Created by t-ybrum on 2/27/2016.
 */
public class BaseTest {

    protected Instances loadInstances(final String arffFilePath) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(arffFilePath);
        Instances data = source.getDataSet();

        return data;
    }
}
