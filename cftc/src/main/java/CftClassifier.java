import datasets.CftInstance;
import datasets.CftDataset;
import exceptions.NotImplementedException;
import interfaces.CostCalculator;
import interfaces.WeightedClassifier;
import weka.core.Instance;

import java.util.ArrayList;
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

        List<CftInstance> trainingSet;
        for (int i = k; i > 0; i++)//todo - verify indexing
        {
            trainingSet = new ArrayList<CftInstance>();
            for (CftInstance cftInstance : dataset) { //todo - modify to have a single cftInstance where we always modifyT
                CftInstance cftInstanceParent = cftInstance.getParent(); //todo- add a validation when it's allowed to take a parent.
                CftInstance cftInstance0 = cftInstanceParent.getLeftChild(); //todo- add a validation when it's allowed to take a child.
                CftInstance cftInstance1 = cftInstanceParent.getRightChild();

                String class0 = layerClassifier.classify(cftInstance0);
                String class1 = layerClassifier.classify(cftInstance1);

                Double costClass0 = costCalculator.getCost(class0, cftInstance.getYactual());
                Double costClass1 = costCalculator.getCost(class1, cftInstance.getYactual());
                cftInstanceParent.setBn(costClass0, costClass1);
                cftInstanceParent.setWn(Math.abs(costClass0 - costClass1));

                trainingSet.add(cftInstanceParent);
            }
            layerClassifier = layerClassifier.train(trainingSet);
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
                CftInstance cftInstanceRoot = cftInstance.getRoot();
                String classification = layerClassifier.classify(cftInstanceRoot);
                if (classification != cftInstanceRoot.getYactual()) {
                    dataset.addMisclassified(cftInstanceRoot, classification);
                }
            }
        }
    }

    public String classifyInstance(Instance instance){
        throw new NotImplementedException();
    }
}