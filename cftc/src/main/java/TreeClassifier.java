import datasets.CftInstance;
import interfaces.WeightedClassifier;
import weka.core.Instances;

import java.util.List;

/**
 * Created by eyapeleg on 2/12/2016.
 */
final class TreeClassifier {

    private final WeightedClassifier weightedClassifier;
    private final TreeClassifier prevTreeClassifier;
    private final int level;

    //// constructors
    public TreeClassifier(final WeightedClassifier weightedClassifier){
        if (weightedClassifier ==null)
            throw new IllegalArgumentException("constructor values are null");

        this.weightedClassifier = weightedClassifier;
        this.prevTreeClassifier=null;
        this.level=1;
    }

    public TreeClassifier(final WeightedClassifier weightedClassifier, final TreeClassifier prevTreeClassifier){
        if (weightedClassifier ==null || prevTreeClassifier==null)
            throw new IllegalArgumentException("constructor values are null");

        this.weightedClassifier = weightedClassifier;
        this.prevTreeClassifier = prevTreeClassifier;
        this.level = prevTreeClassifier.level+1;
    }

    //// api
    public final String classify(final CftInstance cftInstance) throws Exception {
        if (cftInstance==null)
            throw new IllegalArgumentException("cftInstance is null");

        String classification = weightedClassifier.classify(cftInstance.getInstance());
        CftInstance prediction =  cftInstance.getPredictedChild(classification);

        if (prevTreeClassifier !=null)
            return prevTreeClassifier.classify(prediction);
        else
            return prediction.getT();
    }

    public final TreeClassifier train(final List<CftInstance> trainingSet) throws Exception {
        if (trainingSet==null || trainingSet.size()==0)
            throw new IllegalArgumentException("training set must not be null or size 0");

        for(CftInstance cftInstance: trainingSet){
            cftInstance.updateDataValues();
        }
        Instances instances = trainingSet.get(0).getInstance().dataset();
        weightedClassifier.train(instances);

        return new TreeClassifier(weightedClassifier,this);
    }
}
