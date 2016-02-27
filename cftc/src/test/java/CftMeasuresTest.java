import impl.CostCalculatorImpl;
import interfaces.CostCalculator;
import org.junit.Test;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CftMeasuresTest {

    @Test
    public void testTrain() throws Exception {

        for (int m = 1; m <= 1; m++) {

            CostCalculator costCalculator = new CostCalculatorImpl();
            //Classifier classifier = new J48();
            Classifier classifier = new Logistic();
            CftClassifier cftClassifier = new CftClassifier(costCalculator, classifier, m);
            String csvFile = this.getClass().getResource("dataset.csv").getPath();
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";
            int numOfLables;
            String trainFilePath;
            String testFilePath;

            System.out.println("M= " + m);
            try {

                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null) {
                    String[] dataset = line.split(cvsSplitBy);
                    System.out.println("train= " + dataset[0] + " , test=" + dataset[1] + ", lables =" + dataset[2]);
                    numOfLables = Integer.valueOf(dataset[2]);
                    trainFilePath = this.getClass().getResource(dataset[0]).getPath();
                    testFilePath = this.getClass().getResource(dataset[1]).getPath();

                    long startTime = System.currentTimeMillis();
                    cftClassifier.buildClassifier(trainFilePath, numOfLables);
                    long stopTime = System.currentTimeMillis();

                    CftEvaluator cftTestEvaluator = new CftEvaluator(testFilePath, numOfLables, costCalculator, cftClassifier);
                    System.out.println("Test Hamming-Loss= " + cftTestEvaluator.calculateHammingLoss());
                    System.out.println("Test Accuracy= " + cftTestEvaluator.calculateAccuracy());

                    CftEvaluator cftTrainEvaluator = new CftEvaluator(trainFilePath, numOfLables, costCalculator, cftClassifier);
                    System.out.println("Training Hamming-Loss= " + cftTrainEvaluator.calculateHammingLoss());
                    System.out.println("Training Accuracy= " + cftTrainEvaluator.calculateAccuracy());

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