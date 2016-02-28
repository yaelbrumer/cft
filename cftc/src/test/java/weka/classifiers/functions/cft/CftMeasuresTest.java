package weka.classifiers.functions.cft;

import org.junit.Ignore;
import weka.classifiers.functions.cft.BaseTest;
import weka.classifiers.functions.cft.CftClassifier;
import weka.classifiers.functions.cft.CftEvaluator;
import weka.classifiers.functions.cft.impl.HammingLossCostCalculator;
import weka.classifiers.functions.cft.interfaces.CostCalculator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.J48;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CftMeasuresTest extends BaseTest {

    private static Classifier[] classifiers;
    private static int M;
    private static CostCalculator costCalculator;
    private static String dataSetFileName;

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static FileWriter fileWriter;
    private static NumberFormat formatter;

    @Before
    public void init() throws IOException {
        classifiers = new Classifier[]{new J48(), new Logistic(), new NaiveBayes()};
        M=2;
        costCalculator = new HammingLossCostCalculator();
        dataSetFileName = "dataset.csv";

        File outputFile = new File("output.csv");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        fileWriter = new FileWriter(outputFile);

        fileWriter.append("Dataset ,Classifier,Cost Function,M ,Train/Test, Hamming-Loss, Accuracy, Execution Time\n");
        formatter = new DecimalFormat("#0.00000");
    }

    @Ignore
    @Test
    public void testTrain() throws Exception {

        for(Classifier classifier : classifiers) {
            System.out.println(classifier.getClass().toString());

            for (int m = 1; m <= M; m++) {
                System.out.println(Integer.toString(m));
                CftClassifier cftClassifier = new CftClassifier(costCalculator, classifier);
                String dataSetFile = this.getClass().getResource(dataSetFileName).getPath();
                BufferedReader br = null;
                String line = "";
                int numOfLables;
                String trainFilePath;
                String testFilePath;

                try {

                    br = new BufferedReader(new FileReader(dataSetFile));
                    while ((line = br.readLine()) != null) {
                        String[] dataset = line.split(COMMA_DELIMITER);
                        numOfLables = Integer.valueOf(dataset[2]);
                        trainFilePath = this.getClass().getResource(dataset[0]).getPath();
                        testFilePath = this.getClass().getResource(dataset[1]).getPath();
                        System.out.println(dataset[0]);

                        String[] options = {"-L", Integer.toString(numOfLables), "-M", Integer.toString(m)};
                        cftClassifier.setOptions(options);

                        long startTime = System.currentTimeMillis();

                        cftClassifier.buildClassifier(loadInstances(trainFilePath));
                        long stopTime = System.currentTimeMillis();


                        CftEvaluator cftTestEvaluator = new CftEvaluator(loadInstances(testFilePath),
                                                                         numOfLables,
                                                                         costCalculator,
                                                                         cftClassifier);
                        CftEvaluator cftTrainEvaluator = new CftEvaluator(loadInstances(trainFilePath),
                                                                          numOfLables,
                                                                          costCalculator,
                                                                          cftClassifier);



                        // train
                        fileWriter.append(dataset[0]);
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(classifier.getClass().toString());
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append("Hamming Distance");
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(Integer.toString(m));
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append("Train");
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(Double.toString(cftTrainEvaluator.calculateHammingLoss()));
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(Double.toString(cftTrainEvaluator.calculateAccuracy()));
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(formatter.format((stopTime - startTime) / 1000d));
                        fileWriter.append(NEW_LINE_SEPARATOR);


                        // Test
                        fileWriter.append(dataset[0]);
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(classifier.getClass().toString());
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append("Hamming Distance");
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(Integer.toString(m));
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append("Test");
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(Double.toString(cftTestEvaluator.calculateHammingLoss()));
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(Double.toString(cftTestEvaluator.calculateAccuracy()));
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(formatter.format((stopTime - startTime) / 1000d));
                        fileWriter.append(NEW_LINE_SEPARATOR);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @After
    public void cleanup() throws IOException {
        fileWriter.close();
    }
}