import datasets.CftInstance;
import datasets.CftDataset;
import datasets.Classification;
import interfaces.CostCalculator;
import weka.classifiers.Classifier;
import weka.classifiers.SingleClassifierEnhancer;
import weka.classifiers.functions.Logistic;
import weka.classifiers.meta.MultiClassClassifier;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Option;
import weka.core.Utils;

import java.util.Enumeration;

final public class CftClassifier extends MultiClassClassifier{

    private int M;
    private int k;

    private final CostCalculator costCalculator;
    private Classifier classifier;
    private String baseClassifierName;

    private LayerClassifier layerClassifier;
    private CftDataReader cftDataReader;

    public CftClassifier(final CostCalculator costCalculator, final Classifier classifier, final int M) {
        this.costCalculator = costCalculator;
        this.classifier = classifier;
        this.M = M;
        this.cftDataReader=new CftDataReader();
    }

    public String globalInfo() {
        return "a multi-label meta classifier.\nThe labels are converted into classes according to their binary representation.\nAn implementation of a base binary classifier is required.";
    }

    public String tipText(){
        return "a multi-label\nmeta classifier\n";
    }

    public Enumeration listOptions() {
        FastVector newVector = new FastVector(3);
        newVector.addElement(new Option("\tTurn on debugging output.", "D", 0, "-D"));
        newVector.addElement(new Option("\tSet the number of iterations (default 4).", "M", 1, "-M <number>"));
        newVector.addElement(new Option("\tSet the name of the base classifier.", "W", 1, "-W <classifier name>"));
        return newVector.elements();
    }

    public void setOptions(String[] options) throws Exception {
        this.setDebug(Utils.getFlag('D', options));
        String M = Utils.getOption('M', options);
        if(M.length()== 0 || !M.matches("^-?\\d+$"))
            throw new IllegalArgumentException("M option must be an integer greater than zero.");
        else
            this.M = Integer.parseInt(M);
        baseClassifierName = Utils.getOption("W",options);
    }

    public String[] getOptions() {
        String[] options = new String[3];
        int current = 0;
        if(this.getDebug()) {
            options[current++] = "-D";
        }

        options[current++] = "-M";
        options[current++] = "-W";
        return options;
    }

    public String toString(){
        LayerClassifier currentLayer =this.layerClassifier;
        String output ="";
        while(currentLayer!=null){
            output = currentLayer.toString()+"\n\n";
            currentLayer = currentLayer.getPrevLayerClassifier();
        }

        return output;
    }


    public void setClassifier(Classifier classifier){
        this.classifier =classifier;
    }

    public Classifier getClassifier(){
        return classifier;
    }

    public String defaultClassifierString(){
        if (baseClassifierName!=null)
            return baseClassifierName;
        else if (classifier!=null)
            return classifier.toString();
        else return "Base classifier not set.";
    }

    private LayerClassifier buildTreeClassifier(final CftDataset dataset) throws Exception {

        LayerClassifier layerClassifier = new LayerClassifier(classifier.getClass().newInstance());

        for (int level = k; level > 0; level--)
        {
            for (CftInstance cftInstance : dataset) {
                String class0;
                String class1;
                cftInstance.setTtoLevel(level);

                if(level != k)
                {
                    cftInstance.setTtoLeftChild();
                    class0 = layerClassifier.classifyCftInstance(cftInstance);
                }
                else{
                    class0 = cftInstance.getT() + Classification.LEFT_CHILD;
                }

                cftInstance.setTtoLevel(level);
                if(level != k)
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

            if (level!=k) {
                layerClassifier = new LayerClassifier(classifier.getClass().newInstance(), layerClassifier);
            }

            layerClassifier.train(dataset);
        }
        return layerClassifier;
    }

    // API
    final public void buildClassifier(final String arffFilePath,final int numLabelAttributes) throws Exception {
        CftDataset cftDataset = cftDataReader.readData(arffFilePath, numLabelAttributes);
        buildClassifier(cftDataset);

    }

    public void buildClassifier(final CftDataset dataset) throws Exception {

        k = dataset.getNumOfLables();
        for (int i = 0; i < M; i++) {
            this.layerClassifier = buildTreeClassifier(dataset);

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

    public double[] distributionForInstance(Instance instance) throws Exception {
        double[] result = new double[(int)Math.pow(2,k)];
        CftInstance cftInstance = new CftInstance((Instance)instance.copy(),Classification.NONE,Classification.NONE,-1);
        cftInstance.setTtoLevel(1);
        String classification =  layerClassifier.classifyCftInstance(cftInstance);
        result[Integer.parseInt(classification, 2)]=1.0;
        return result;
    }
}
