package core;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.*;

/**
 * Created by ybrumer on 2/12/2016.
 */
public class FileUtils {

    public FileUtils() {
    }

    public static Instances LoadData(String resourceName) throws Exception {

        String filePath = FileUtils.class.getClassLoader().getResource(resourceName).getPath();
        DataSource source = new DataSource(filePath);
        Instances data = source.getDataSet();

        return data;
    }
}
