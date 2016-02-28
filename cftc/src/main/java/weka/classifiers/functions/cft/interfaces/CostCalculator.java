package weka.classifiers.functions.cft.interfaces;

/**
 * A cost calculation interface for a miss-classification of one class to another.
 */

public interface CostCalculator {

    /**
     * get the cost of a miss-classification of one class to another.
     * @param predicted predicted class
     * @param actual actual class
     * @return the cost predicting the "predicted" instead of the "actual".
     */
    double getCost(String predicted, String actual);
}
