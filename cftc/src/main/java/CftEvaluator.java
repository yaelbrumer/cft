import datasets.CftDataset;
import datasets.CftInstance;
import interfaces.CostCalculator;
import mulan.core.ArgumentNullException;
import weka.classifiers.Classifier;
import weka.core.Instances;

public class CftEvaluator {

    private final CostCalculator costCalculator;
    private final CftDataset dataset;
    private CftDataReader cftDataReader;
    private Classifier classifier;

    public CftEvaluator(Instances instances , int numLabelAttributes, final CostCalculator costCalculator, Classifier classifier) throws Exception {
        this.costCalculator = costCalculator;
        this.cftDataReader = new CftDataReader();
        this.classifier = classifier;

        CftDataset dataset = cftDataReader.readData(instances, numLabelAttributes);
        this.dataset = dataset;
    }

    public double calculateHammingLoss() throws Exception {
        double cost = 0;
        int n = dataset.getInstances().numInstances();

        for (CftInstance cftInstance : dataset) {

            final String yActual = cftInstance.getYactual();
            final String yPredicted = getYPredicted(cftInstance);

            if (yPredicted == null) {
                throw new ArgumentNullException("yPredicted");
            }

            //calculate cost
            cost = cost + costCalculator.getCost(yPredicted, yActual);
        }

        return cost / n;
    }

    public double calculateAccuracy() throws Exception {

        double miss = 0;
        int n = dataset.getInstances().numInstances();

        for (CftInstance cftInstance : dataset) {

            final String yActual = cftInstance.getYactual();
            final String yPredicted = getYPredicted(cftInstance);

            if (yPredicted == null) {
                throw new ArgumentNullException("yPredicted");
            }

            if(!yActual.equals(yPredicted))
            {
                miss++;
            }
        }

        System.out.println("Training - Missing= " + miss);
        System.out.println("Training - Total= " + n);

        return (1 - miss/n);
    }



    private String getYPredicted(CftInstance cftInstance) throws Exception {

        //get prediction list for instance
        final double[] yPredictedList = classifier.distributionForInstance(cftInstance.getInstance());
        for (int i = 0; i < yPredictedList.length ; i++) {
            if(yPredictedList[i] == 1.0)
            {
                return toBinaryString(i);
            }
        }
        return null;
    }

    final String toBinaryString(int index)
    {
        String result = Integer.toBinaryString(index);
        int k = dataset.getNumOfLables();

        if(result.length() != k)
        {
            int len = k - result.length();
            for (int i = 0; i < len; i++) {
                result = "0" + result;
            }
        }
        return result;
    }

}
