import classifiers.TreeClassifier;
import datasets.CftInstance;
import datasets.MultiLabelDataset;
import interfaces.CostCalculator;
import interfaces.WeightedClassifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by eyapeleg on 2/12/2016.
 */
final public class CftTrainer {

    private final int M;
    private final CostCalculator costCalculator;
    private final WeightedClassifier weightedClassifier;

    public CftTrainer(CostCalculator costCalculator, WeightedClassifier trainer, int M) {
        this.costCalculator = costCalculator;
        this.weightedClassifier = trainer;
        this.M = M;
    }

    private TreeClassifier buildTreeClassifier(final MultiLabelDataset dataset, TreeClassifier treeClassifier, final int k)
            throws Exception {

        List<CftInstance> trainingSet;
        for (int i = k; i > 0; i++)//todo - verify indexing
        {
            trainingSet = new ArrayList<CftInstance>();
            for (CftInstance cftInstance : dataset) {
                CftInstance cftInstanceParent = cftInstance.getParent(); //todo- add a validation when it's allowed to take a parent.
                CftInstance cftInstance0 = cftInstanceParent.getLeftChild(); //todo- add a validation when it's allowed to take a child.
                CftInstance cftInstance1 = cftInstanceParent.getRightChild();

                String class0 = treeClassifier.classify(cftInstance0);
                String class1 = treeClassifier.classify(cftInstance1);

                Double costClass0 = costCalculator.getCost(class0, cftInstance.getYactual());
                Double costClass1 = costCalculator.getCost(class1, cftInstance.getYactual());
                cftInstanceParent.setBn(costClass0, costClass1);
                cftInstanceParent.setWn(Math.abs(costClass0-costClass1));

                trainingSet.add(cftInstanceParent);
            }

            treeClassifier= treeClassifier.train(trainingSet);
        }
        return treeClassifier;
    }

    final public CftClassifier train(final MultiLabelDataset dataset) throws Exception {
        final int k = dataset.getNumLabels();
        TreeClassifier treeClassifier = new TreeClassifier(weightedClassifier);

        for (int i = 0; i < M; i++) {
            treeClassifier = buildTreeClassifier(dataset, treeClassifier, k);

            Set<CftInstance> cftInstances = new HashSet<CftInstance>();
            for(CftInstance cftInstance: dataset){
                CftInstance cftInstanceRoot = cftInstance.getRoot();
                String classification = treeClassifier.classify(cftInstanceRoot);
                if (classification!=cftInstanceRoot.getYactual()){
                    dataset.addMisclassified(cftInstanceRoot, classification);
                }
            }

            // dataset.addClassificationToDataset(classification);
        }

        return new CftClassifier(treeClassifier);
    }
}