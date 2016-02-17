package classifiers;

import datasets.CftInstance;
import interfaces.CostClassifier;
import weka.core.Instance;

/**
 * Created by eyapeleg on 2/12/2016.
 */
public final class TreeClassifier {

    private final CostClassifier costClassifier;
    private final TreeClassifier prevTreeClassifier;
    private final int level;

    //// constructors
    public TreeClassifier(final CostClassifier costClassifier){
        if (costClassifier==null)
            throw new IllegalArgumentException("constructor values are null");

        this.costClassifier=costClassifier;
        this.prevTreeClassifier=null;
        this.level=0;
    }

    public TreeClassifier(final CostClassifier costClassifier, final TreeClassifier prevTreeClassifier){
        if (costClassifier==null || prevTreeClassifier==null)
            throw new IllegalArgumentException("constructor values are null");

        this.costClassifier=costClassifier;
        this.prevTreeClassifier = prevTreeClassifier;
        this.level = prevTreeClassifier.getLevel()+1;
    }

    //// accessors
    public int getLevel(){
        return level;
    }

    //// api
    public String classify(final CftInstance cftInstance) throws Exception {
        if (cftInstance==null)
            throw new IllegalArgumentException("cftInstance is null");

        CftInstance cftInstanceCopy = cftInstance.clone();
        Instance instance = cftInstanceCopy.getInstance();
        String classification = costClassifier.classify(instance);
        cftInstanceCopy.updateT(classification);

        if (prevTreeClassifier !=null)
            return prevTreeClassifier.classify(cftInstanceCopy);
        else
            return cftInstanceCopy.getT();
    }
}
