import datasets.CftInstance;
import datasets.CftDataset;
import datasets.Classification;
import exceptions.NotImplementedException;
import interfaces.CostCalculator;
import interfaces.WeightedClassifier;
import weka.core.Instance;

import java.util.List;

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

        LayerClassifier layerClassifier = new LayerClassifier(weightedClassifier);
        final int k = dataset.getNumOfLables();

        for (int level = k; level > 0; level--)//todo - verify indexing
        {
            for (CftInstance cftInstance : dataset) { //todo - modify to have a single cftInstance where we always modifyT

                cftInstance.setTtoLevel(level);
                cftInstance.setTtoLeftChild();
                String class0 = (level==k)?cftInstance.getT():layerClassifier.classify(cftInstance);

                cftInstance.setTtoLevel(level);
                cftInstance.setTtoRightChild();
                String class1 = (level==k)?cftInstance.getT():layerClassifier.classify(cftInstance);

                Double costClass0 = costCalculator.getCost(class0, cftInstance.getYactual());
                Double costClass1 = costCalculator.getCost(class1, cftInstance.getYactual());

                String b = (costClass0<costClass1)? Classification.LEFT_CHILD:Classification.RIGHT_CHILD;
                cftInstance.setBn(b);
                cftInstance.setWn(Math.abs(costClass0 - costClass1));
                cftInstance.setTtoLevel(level);
            }

            layerClassifier = layerClassifier.train(dataset);
        }
        return layerClassifier;
    }

    // API
    final public void buildClassifier(final String arffFilePath,final int numLabelAttributes) throws Exception {
        CftDataset cftDataset = cftDataReader.readData(arffFilePath, numLabelAttributes);
        buildClassifier(cftDataset);

    }

    final public void buildClassifier(final CftDataset dataset) throws Exception {

        for (int i = 0; i < M; i++) {
            this.layerClassifier = buildTreeClassifier(dataset);

            for (CftInstance cftInstance : dataset) {
                cftInstance.setTtoLevel(0); //todo - verify indexing
                String classification = layerClassifier.classify(cftInstance);
                if (classification != cftInstance.getYactual()) {
                    dataset.addMisclassified(cftInstance, classification);
                }
            }
        }
    }

    public String classifyInstance(Instance instance){
        throw new NotImplementedException();
    }
}