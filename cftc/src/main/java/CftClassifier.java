import datasets.CftInstance;
import datasets.CftDataset;
import datasets.Classification;
import interfaces.CostCalculator;
import weka.classifiers.Classifier;
import weka.classifiers.meta.MultiClassClassifier;
import weka.core.Instance;

final public class CftClassifier extends MultiClassClassifier{

    private final int M;
    private int k;

    private final CostCalculator costCalculator;
    private Classifier classifier;

    private LayerClassifier layerClassifier;
    private CftDataReader cftDataReader;

    public CftClassifier(final CostCalculator costCalculator, final Classifier classifier, final int M) {
        this.costCalculator = costCalculator;
        this.classifier = classifier;
        this.M = M;
        this.cftDataReader=new CftDataReader();
    }

    public void setClassifier(Classifier classifier){
        this.classifier =classifier;
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
        double miss = 0;

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
                    miss++; //TODO - remove that validation
                    CftInstance cftInstanceMissClassified = cftInstance.copy();
                    cftInstanceMissClassified.setYPredicted(currentClassification);
                    dataset.addMisclassified(cftInstanceMissClassified);
                }
            }
        }

        //TODO - remove that validation
        int n = dataset.getInstances().numInstances();
        double acc = 1 - (miss/n);
        System.out.println("Accuracy During Training= " + acc);
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
