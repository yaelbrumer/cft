import datasets.CftDataset;
import datasets.CftInstance;
import interfaces.CostCalculator;

public class CftEvaluator {

    private final CostCalculator costCalculator;
    private final CftDataset dataset;

    public CftEvaluator(final CostCalculator costCalculator, CftDataset dataset) {
        this.costCalculator = costCalculator;
        this.dataset = dataset;
    }

    public double calculateHammingLoss(CftClassifier cftClassifier) throws Exception {
        double cost = 0;
        int n = dataset.getInstances().numInstances();

        for (CftInstance cftInstance : dataset) {

            String yPredicted = null;
            final String yActual = cftInstance.getYactual();

            //get prediction list for instance
            final double[] yPredictedList = cftClassifier.distributionForInstance(cftInstance.getInstance());
            for (int i = 0; i < yPredictedList.length ; i++) {
                if(yPredictedList[i] == 1.0)
                {
                    yPredicted = toBinaryString(i);
                    break;
                }
            }

            cost = cost + costCalculator.getCost(yPredicted, yActual);
        }

        return cost / n;
    }

    final String toBinaryString(int index)
    {
        String result = Integer.toBinaryString(index);
        int k = dataset.getNumOfLables();

        if(result.length() != k)
        {
            int len = k - result.length(); //TODO - check indexing
            for (int i = 0; i < len; i++) {
                result = "0" + result;
            }
        }
        return result;
    }

}
