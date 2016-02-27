import model.CftDataset;
import model.CftInstance;
import model.Classification;
import weka.classifiers.Classifier;

/**
 * Created by eyapeleg on 2/12/2016.
 */
final class LayerClassifier {

    private final Classifier classifier;
    private final LayerClassifier prevLayerClassifier;

    //// constructors
    LayerClassifier(final Classifier classifier){
        if (classifier ==null)
            throw new IllegalArgumentException("constructor values are null");

        this.classifier = classifier;
        this.prevLayerClassifier =null;
    }

    LayerClassifier(final Classifier classifier, final LayerClassifier prevLayerClassifier){
        if (classifier ==null || prevLayerClassifier ==null)
            throw new NullPointerException("constructor values are null");

        this.classifier = classifier;
        this.prevLayerClassifier = prevLayerClassifier;
    }

    //// api
    final String classifyCftInstance(final CftInstance cftInstance) throws Exception {
        if (cftInstance==null)
            throw new NullPointerException("cftInstance is null");

        double result = classifier.classifyInstance(cftInstance.getInstance());
        String classification = String.valueOf((int) result);

        if (prevLayerClassifier != null)
        {
            if (classification.equals(Classification.LEFT_CHILD))
                cftInstance.setTtoLeftChild();
            else
                cftInstance.setTtoRightChild();

            return prevLayerClassifier.classifyCftInstance(cftInstance);
        }
        else
        {
            return  cftInstance.getT() + classification;
        }
    }

    final void train(final CftDataset cftDataset) throws Exception {
        if (cftDataset==null)
            throw new NullPointerException("training set must not be null!!");

        classifier.buildClassifier(cftDataset.getInstances());
    }

    final public String toString(){
        return classifier.toString();
    }

    final public LayerClassifier getPrevLayerClassifier(){
        return prevLayerClassifier;
    }
}
