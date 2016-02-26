import datasets.CftInstance;
import datasets.CftDataset;
import datasets.Classification;
import interfaces.CostCalculator;
import interfaces.WeightedClassifier;
import weka.core.Instance;

final public class CftClassifier {

    private final int M;

    private final CostCalculator costCalculator;
    private final WeightedClassifier weightedClassifier;

    private LayerClassifier layerClassifier;
    private CftDataReader cftDataReader;

    public CftClassifier(final CostCalculator costCalculator, final WeightedClassifier trainer, final int M) {
        this.costCalculator = costCalculator;
        this.weightedClassifier = trainer;
        this.M = M;
        this.cftDataReader=new CftDataReader();
    }

    private LayerClassifier buildTreeClassifier(final CftDataset dataset) throws Exception {

        LayerClassifier layerClassifier = new LayerClassifier(weightedClassifier.clone());
        final int k = dataset.getNumOfLables();

        for (int level = k; level > 0; level--)
        {
            for (CftInstance cftInstance : dataset) {
                String class0;
                String class1;
                cftInstance.setTtoLevel(level);

                if(level != k)
                {
                    cftInstance.setTtoLeftChild();
                    class0 = layerClassifier.classify(cftInstance);
                }
                else{
                    class0 = cftInstance.getT() + Classification.LEFT_CHILD;
                }

                cftInstance.setTtoLevel(level);
                if(level != k)
                {
                    cftInstance.setTtoRightChild();
                    class1 = layerClassifier.classify(cftInstance);
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
                layerClassifier = new LayerClassifier(weightedClassifier.clone(), layerClassifier);
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

    private void buildClassifier(final CftDataset dataset) throws Exception {

        for (int i = 0; i < M; i++) {
            this.layerClassifier = buildTreeClassifier(dataset);

            int numInstances = dataset.getInstances().numInstances();
            int j=0;

            for (CftInstance cftInstance : dataset) {

                if (j>=numInstances)
                    break;
                j++;

                cftInstance.setTtoLevel(1);
                String currentClassification = layerClassifier.classify(cftInstance);
                String prevClassification = cftInstance.getYpredicted();
                if (currentClassification != prevClassification) {
                    CftInstance cftInstanceMissClassified = cftInstance.copy();
                    cftInstanceMissClassified.setYPredicted(currentClassification);
                    dataset.addMisclassified(cftInstanceMissClassified);
                }
            }
        }
    }

    public String classifyInstance(Instance instance) throws Exception {
        return weightedClassifier.classify(instance);
    }
}