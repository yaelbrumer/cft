package weka.classifiers.functions.cft;

import weka.classifiers.functions.cft.model.CftInstance;
import weka.classifiers.functions.cft.model.CftDataset;
import weka.classifiers.functions.cft.model.Classification;
import weka.classifiers.functions.cft.impl.HammingLossCostCalculator;
import weka.classifiers.functions.cft.interfaces.CostCalculator;
import weka.classifiers.Classifier;
import weka.classifiers.meta.MultiClassClassifier;
import weka.core.*;

import java.util.Enumeration;

final public class CftClassifier extends MultiClassClassifier{

    private int M;
    private int K;

    private final CostCalculator costCalculator;
    private Classifier classifier;
    private String baseClassifierName;

    private LayerClassifier layerClassifier;

    private LayerClassifier buildSingleTreeClassifier(final CftDataset dataset) throws Exception {

        LayerClassifier layerClassifier = new LayerClassifier(classifier.getClass().newInstance());

        for (int level = K; level > 0; level--)
        {
            for (CftInstance cftInstance : dataset) {
                String class0;
                String class1;
                cftInstance.setTtoLevel(level);

                if(level != K)
                {
                    cftInstance.setTtoLeftChild();
                    class0 = layerClassifier.classifyCftInstance(cftInstance);
                }
                else{
                    class0 = cftInstance.getT() + Classification.LEFT_CHILD;
                }

                cftInstance.setTtoLevel(level);
                if(level != K)
                {
                    cftInstance.setTtoRightChild();
                    class1 = layerClassifier.classifyCftInstance(cftInstance);
                } else {
                    class1 = cftInstance.getT() + Classification.RIGHT_CHILD;
                }

                Double costClass0 = costCalculator.getCost(class0, cftInstance.getYactual());
                Double costClass1 = costCalculator.getCost(class1, cftInstance.getYactual());

                String b = (costClass0<costClass1)? Classification.LEFT_CHILD:Classification.RIGHT_CHILD;
                cftInstance.setBn(b);
                cftInstance.setWn(Math.abs(costClass0 - costClass1));
                cftInstance.setTtoLevel(level);
            }

            if (level!= K) {
                layerClassifier = new LayerClassifier(classifier.getClass().newInstance(), layerClassifier);
            }

            layerClassifier.train(dataset);
        }
        return layerClassifier;
    }

    private void buildMultipleTreeClassifier(final CftDataset dataset) throws Exception {

        K = dataset.getNumOfLables();

        for (int i = 0; i < M; i++) {
            this.layerClassifier = buildSingleTreeClassifier(dataset);

            int numInstances = dataset.getInstances().numInstances();
            int j=0;

            for (CftInstance cftInstance : dataset) {

                if (j>=numInstances)
                    break;
                j++;

                cftInstance.setTtoLevel(1);
                String currentClassification = layerClassifier.classifyCftInstance(cftInstance);
                String prevClassification = cftInstance.getYpredicted();
                if (!(currentClassification.equals(prevClassification))) {
                    CftInstance cftInstanceMissClassified = cftInstance.copy();
                    cftInstanceMissClassified.setYPredicted(currentClassification);
                    dataset.addMisclassified(cftInstanceMissClassified);
                }
            }
        }
    }

    // -------- API ----------------

    /**
     * class constructor.
     * @param costCalculator - an object that calculates the cost of a misclassification.
     * @param classifier - a binary classifier.
     */
    public CftClassifier(final CostCalculator costCalculator, final Classifier classifier) {
        this.costCalculator = costCalculator;
        this.classifier = classifier;
        this.M=4;
    }

    /**
     *  class constructor.
     *  the classifier would use the default hamming loss implementation as the cost function
     * @param classifier - a binary classifier.
     */
    public CftClassifier(final Classifier classifier) {
        this.costCalculator = new HammingLossCostCalculator();
        this.classifier = classifier;
        this.M=4;
    }

    /**
     * Weka's API
     */
    public String globalInfo() {
        return "a multi-label meta classifier.\nThe labels are converted into classes according to their binary representation.\nAn implementation of a base binary classifier is required.";
    }

    /**
     * Weka's API
     */
    public String tipText(){
        return "a multi-label\nmeta classifier\n";
    }

    /**
     * Weka's API
     */
    public Enumeration listOptions() {
        FastVector newVector = new FastVector(3);
        newVector.addElement(new Option("\tTurn on debugging output.", "D", 0, "-D"));
        newVector.addElement(new Option("\tSet the number of labels (obligatory).", "L", 1, "-L"));
        newVector.addElement(new Option("\tSet the number of iterations (default 4 iterations)", "M", 1, "-M <number>"));
        newVector.addElement(new Option("\tSet the name of the base classifier.", "W", 1, "-W <classifier name>"));
        return newVector.elements();
    }

    /**
     * Weka's API
     */
    public void setOptions(String[] options) throws Exception {

        //D
        this.setDebug(Utils.getFlag('D', options));

        //L
        String K = Utils.getOption('L', options);
        if(K.length()== 0 || !K.matches("^-?\\d+$"))
            throw new IllegalArgumentException("L option must be an integer greater than zero.");
        else
            this.K = Integer.parseInt(K);

        //M
        String M = Utils.getOption('M', options);
        if(M.length()== 0 || !M.matches("^-?\\d+$"))
            throw new IllegalArgumentException("M option must be an integer greater than zero.");
        else
            this.M = Integer.parseInt(M);

        //W
        this.baseClassifierName = Utils.getOption("W",options);
    }

    /**
     * Weka's API
     */
    public String[] getOptions() {
        String[] options = new String[7];

        int current = 0;
        options[current++] = "D";
        options[current++] = "L";
        options[current++] = Integer.toString(K);
        options[current++] = "M";
        options[current++] = Integer.toString(M);
        options[current++] = "W";
        options[current++] = baseClassifierName;

        return options;
    }

    /**
     * Weka's API
     */
    public String toString(){
        LayerClassifier currentLayer =this.layerClassifier;
        String output ="";
        while(currentLayer!=null){
            output = currentLayer.toString()+"\n\n";
            currentLayer = currentLayer.getPrevLayerClassifier();
        }

        return output;
    }

    /**
     * Weka's API
     */
    public void setClassifier(Classifier classifier){
        this.classifier =classifier;
    }

    /**
     * Weka's API
     */
    public Classifier getClassifier(){
        return classifier;
    }

    /**
     * Weka's API
     */
    public String defaultClassifierString(){
        if (baseClassifierName!=null)
            return baseClassifierName;
        else if (classifier!=null)
            return classifier.toString();
        else return "Base classifier not set.";
    }


    /**
     * Weka's API
     */
    public void buildClassifier(Instances instances) throws Exception {
        if (K==0 || M==0)
            throw new IllegalArgumentException("Options -L and -M must be set to an integer greater than zero.");

        CftDataReader cftDataReader = new CftDataReader();
        CftDataset cftDataset = cftDataReader.readData(instances, K);
        buildMultipleTreeClassifier(cftDataset);
    }

    /**
     * Weka's API
     */
    public double[] distributionForInstance(Instance instance) throws Exception {
        double[] result = new double[(int)Math.pow(2, K)];
        CftInstance cftInstance = new CftInstance((Instance)instance.copy(),Classification.NONE,Classification.NONE,-1);
        cftInstance.setTtoLevel(1);
        String classification =  layerClassifier.classifyCftInstance(cftInstance);
        result[Integer.parseInt(classification, 2)]=1.0;
        return result;
    }
}
