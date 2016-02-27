import impl.HammingLossCostCalculator;
import interfaces.CostCalculator;
import org.junit.Before;
import org.junit.Test;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.J48;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CftMeasuresTest extends BaseTest{

    private static Classifier[] classifiers;
    private static int M;
    private static CostCalculator costCalculator;
    private static String dataSetFileName;

    @Before
    public void init(){
        classifiers = new Classifier[]{new J48(), new Logistic(), new NaiveBayes()};
        M=8;
        costCalculator = new HammingLossCostCalculator();
        dataSetFileName = "dataset.csv";
    }

    @Test
    public void testTrain() throws Exception {

        for(Classifier classifier : classifiers) {

            System.out.println("-----------------------\nClassifier = "+classifier.getClass());
            for (int m = 1; m <= M; m++) {

                CftClassifier cftClassifier = new CftClassifier(costCalculator, classifier);
                String dataSetFile = this.getClass().getResource(dataSetFileName).getPath();
                BufferedReader br = null;
                String line = "";
                String cvsSplitBy = ",";
                int numOfLables;
                String trainFilePath;
                String testFilePath;

                System.out.println("M= " + m);
                try {

                    br = new BufferedReader(new FileReader(dataSetFile));
                    while ((line = br.readLine()) != null) {
                        String[] dataset = line.split(cvsSplitBy);
                        System.out.println("dataset "+dataset[0]+", lables =" + dataset[2]);
                        numOfLables = Integer.valueOf(dataset[2]);
                        trainFilePath = this.getClass().getResource(dataset[0]).getPath();
                        testFilePath = this.getClass().getResource(dataset[1]).getPath();
                        String[] options = {"-L", Integer.toString(numOfLables), "-M", Integer.toString(m)};
                        cftClassifier.setOptions(options);

                        long startTime = System.currentTimeMillis();

                        cftClassifier.buildClassifier(loadInstances(trainFilePath));
                        long stopTime = System.currentTimeMillis();

                        CftEvaluator cftTestEvaluator = new CftEvaluator(loadInstances(testFilePath),
                                                                         numOfLables,
                                                                         costCalculator,
                                                                         cftClassifier);
                        System.out.println("Test:");
                        System.out.println("1. Hamming-Loss= " + cftTestEvaluator.calculateHammingLoss());
                        System.out.println("2. Accuracy= " + cftTestEvaluator.calculateAccuracy());

                        CftEvaluator cftTrainEvaluator = new CftEvaluator(loadInstances(trainFilePath),
                                                                          numOfLables,
                                                                          costCalculator,
                                                                          cftClassifier);
                        System.out.println("Train:");
                        System.out.println("1. Hamming-Loss= " + cftTrainEvaluator.calculateHammingLoss());
                        System.out.println("2. Accuracy= " + cftTrainEvaluator.calculateAccuracy());

                        NumberFormat formatter = new DecimalFormat("#0.00000");
                        System.out.print("Execution time is " + formatter.format((stopTime - startTime) / 1000d) + " seconds\n");
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
}