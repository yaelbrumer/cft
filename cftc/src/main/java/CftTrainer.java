import classifiers.Classification;
import classifiers.TreeClassifier;
import datasets.CftInstance;
import datasets.MultiLabelDatasetLayerK;
import interfaces.CostCalculator;
import interfaces.CostClassifier;
import weka.core.Instance;

/**
 * Created by eyapeleg on 2/12/2016.
 */
public class CftTrainer {

    private int M;
    private CostCalculator costCalculator;
    private CostClassifier costClassifier;

    public CftTrainer(CostCalculator costCalculator, CostClassifier trainer, int M)
    {
        this.costCalculator = costCalculator;
        this.costClassifier = trainer;
        this.M = M;
    }


    private TreeClassifier buildTreeClassifier(MultiLabelDatasetLayerK dataset, TreeClassifier treeClassifier, int k)
    {
        for (int i = k; i > 0; i++)//todo - verify indexing
        {
            int j=1;
            for(CftInstance cftInstance:dataset){
                Instance instance = cftInstance.getInstance();
                String yPredicted = cftInstance.getYPredicted();
                String t = yPredicted.substring(0,yPredicted.length()-j); //todo - verify indexing
                treeClassifier.classify(instance)
                String t0;
                String t1;
                instance.setValue(9999,t); //set t, todo - replace 999 with the relevant attribute/index
                j++;
            }
        }
        return treeClassifier;
    }

    public CftClassifier train(MultiLabelDatasetLayerK dataset)
    {
        int k = dataset.getNumLabels();
        TreeClassifier treeClassifier = new TreeClassifier();

        for (int i = 0; i<M; i++)
        {
            treeClassifier = buildTreeClassifier(dataset, treeClassifier, k);
            Classification classification = treeClassifier.classify(dataset);
            dataset.addClassificationToDataset(classification);
        }

        return new CftClassifier(treeClassifier);
    }
}