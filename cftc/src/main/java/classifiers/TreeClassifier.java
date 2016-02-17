package classifiers;

import datasets.CftInstance;
import interfaces.CostClassifier;
import weka.core.Instance;

/**
 * Created by eyapeleg on 2/12/2016.
 */
public class TreeClassifier {

    private CostClassifier costClassifier;
    private TreeClassifier nextTreeClassifier;

    public Classification classify(CftInstance cftInstance) throws Exception {

        Instance instance = cftInstance.getInstance();
        CftInstance cftInstance0;
        CftInstance cftInstance1;

        Classification classification = costClassifier.classify(instance);
/*        instance.insertAttributeAt(instance.numAttributes()+1); // todo - check if we want to modify instnace /make a copy
        instance.setValue(instance.numAttributes(),(double)classification.getT()); //todo - check if casting is legal*/
        if (nextTreeClassifier !=null)
            return nextTreeClassifier.classify(instance);
        else
            return classification;
    }
}
