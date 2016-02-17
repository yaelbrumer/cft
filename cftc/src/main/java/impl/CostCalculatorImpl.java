package impl;

import interfaces.CostCalculator;

public final class CostCalculatorImpl implements CostCalculator {

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
