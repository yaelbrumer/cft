package weka.classifiers.functions.cft.impl;

import weka.classifiers.functions.cft.interfaces.CostCalculator;

/**
 * An implementation of the costCalculator.
 * Calculates the cost of a miss-classification according to the Hamming Distance
 * of the two class.
 *
 * e.g for 100 and 000 the hamming distance would be 1 and the cost would be 1/3.
 */
public final class HammingLossCostCalculator implements CostCalculator {

    public final double getCost(final String predicted, final String actual) {

        if (predicted == null || actual == null) {
            throw new IllegalArgumentException("Predicted or Actual values must not be null");
        }

        if (predicted.length() != actual.length()) {
            throw new IllegalArgumentException("Strings length are not equal");
        }

        char[] s1 = predicted.toCharArray();
        char[] s2 = actual.toCharArray();

        int result = 0;
        for (int i = 0; i < s1.length; i++) {
            if (s1[i] != s2[i]) {
                result++;
            }
        }

        return result / (double) s1.length;
    }
}
