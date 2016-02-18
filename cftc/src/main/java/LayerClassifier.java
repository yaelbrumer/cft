import datasets.CftInstance;
import datasets.Classification;
import interfaces.WeightedClassifier;
import weka.core.Instances;

import java.util.List;

/**
 * Created by eyapeleg on 2/12/2016.
 */
final class LayerClassifier {

    private final WeightedClassifier weightedClassifier;
    private final LayerClassifier prevLayerClassifier;
    //private final int level;

    //// constructors
    LayerClassifier(final WeightedClassifier weightedClassifier){
        if (weightedClassifier ==null)
            throw new IllegalArgumentException("constructor values are null");

        this.weightedClassifier = weightedClassifier;
        this.prevLayerClassifier =null;
       //this.level=1;
    }

    private LayerClassifier(final WeightedClassifier weightedClassifier, final LayerClassifier prevLayerClassifier){
        if (weightedClassifier ==null || prevLayerClassifier ==null)
            throw new IllegalArgumentException("constructor values are null");

        this.weightedClassifier = weightedClassifier;
        this.prevLayerClassifier = prevLayerClassifier;
      //  this.level = prevLayerClassifier.level+1;
    }

    //// api
    final String classify(final CftInstance cftInstance) throws Exception {
        if (cftInstance==null)
            throw new IllegalArgumentException("cftInstance is null");

        String classification = weightedClassifier.classify(cftInstance.getInstance());

        if (classification== Classification.LEFT_CHILD)
            cftInstance.seTtoLeftChild();
        else if (classification==Classification.RIGHT_CHILD)
            cftInstance.seTtoRightChild();

        if (prevLayerClassifier !=null)
            return prevLayerClassifier.classify(cftInstance);
        else
            return cftInstance.getT();
    }

    final LayerClassifier train(final List<CftInstance> trainingSet) throws Exception {
        if (trainingSet==null || trainingSet.size()==0)
            throw new IllegalArgumentException("training set must not be null or size 0");

        Instances instances = trainingSet.get(0).getInstance().dataset();
        weightedClassifier.train(instances);

        return new LayerClassifier(weightedClassifier, this);
    }
}
