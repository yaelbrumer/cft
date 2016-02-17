package core;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * Created by ybrumer on 2/17/2016.
 */
public class MLUtils {
    /**
     * ToBitString - returns a String representation of x = [0,0,1,0,1,0,0,0], e.g., "000101000".
     */
    public static String toBitString(Instance x, int L) {
        StringBuilder sb = new StringBuilder(L);
        int numOfAtt = x.numAttributes();

        int startIndex = numOfAtt - L - 1;
        for(int i = startIndex; i < numOfAtt - 1; i++) {
            sb.append((int)Math.round(x.value(i)));
        }
        return sb.toString();
    }

    /**
     * Load dataset from artff file
     */
    public static Instances loadInstances(String arffFilePath) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(arffFilePath);
        Instances data = source.getDataSet();

        return data;
    }
}
