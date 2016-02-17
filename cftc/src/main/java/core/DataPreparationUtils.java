package core;

import weka.core.Instance;

/**
 * Created by ybrumer on 2/12/2016.
 */
public class DataPreparationUtils {

    //todo - check if still needed
    public static String toBitString(Instance x, int L) {
        StringBuilder sb = new StringBuilder(L);
        int firstLabelIndex = x.numAttributes() - L;
        for(int i = firstLabelIndex; i < x.numAttributes(); ++i) {
            sb.append((int)Math.round(x.value(i)));
        }

        return sb.toString();
    }

    /**
     * Convert a multi-label instance into a multi-class instance, according to a template.
     */
}
