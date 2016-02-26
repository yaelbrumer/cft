import datasets.CftDataset;
import datasets.CftInstance;
import datasets.Classification;
import interfaces.WeightedClassifier;

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

    LayerClassifier(final WeightedClassifier weightedClassifier, final LayerClassifier prevLayerClassifier){
        if (weightedClassifier ==null || prevLayerClassifier ==null)
            throw new NullPointerException("constructor values are null");

        this.weightedClassifier = weightedClassifier;
        this.prevLayerClassifier = prevLayerClassifier;
      //  this.level = prevLayerClassifier.level+1;
    }

    //// api
    final String classify(final CftInstance cftInstance) throws Exception {
        if (cftInstance==null)
            throw new NullPointerException("cftInstance is null");

        String classification = weightedClassifier.classify(cftInstance.getInstance());

        if (prevLayerClassifier != null)
        {
            if (classification.equals(Classification.LEFT_CHILD))
                cftInstance.setTtoLeftChild();
            else
                cftInstance.setTtoRightChild();

            return prevLayerClassifier.classify(cftInstance);
        }
        else
        {
            return  cftInstance.getT() + classification;
        }
    }

    final void train(final CftDataset cftDataset) throws Exception {
        if (cftDataset==null)
            throw new NullPointerException("training set must not be null!!");

        weightedClassifier.train(cftDataset.getInstances());
    }
}
