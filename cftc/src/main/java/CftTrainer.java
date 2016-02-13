import classifiers.Classification;
import classifiers.LayerClassifier;
import classifiers.TreeClassifier;
import datasets.MultiLabelDataset;
import datasets.MultiLabelDatasetLayerK;
import exceptions.NotImplementedException;
import interfaces.CostCalculator;
import interfaces.WeightedClassifier;

/**
 * Created by eyapeleg on 2/12/2016.
 */
public class CftTrainer {

    private int M;

    private TreeClassifier treeClassifier;

    private CostCalculator costCalculator;
    private WeightedClassifier weightedClassifier;

    public CftTrainer(CostCalculator costCalculator, WeightedClassifier trainer, int M)
    {
        this.costCalculator = costCalculator;
        this.weightedClassifier = trainer;
        this.M = M;
    }

    private MultiLabelDatasetLayerK dataPreparation(MultiLabelDataset dp)
    {
        throw new NotImplementedException();
    }

    private TreeClassifier createTreeClassifier(MultiLabelDataset MultiLabelDataset, int k)
    {
        for (int i = k; i > 0; i++)//todo - verify indexing
        {
            MultiLabelDatasetLayerK multiLabelDatasetLayerK = dataPreparation(MultiLabelDataset);
            LayerClassifier layerClassifier = weightedClassifier.train(multiLabelDatasetLayerK);
            treeClassifier.addLayer(layerClassifier);
        }
        return treeClassifier;
    }

    private CftClassifier createCftClassifier(TreeClassifier treeClassifier)
    {
        throw new NotImplementedException();
    }

    public CftClassifier train(MultiLabelDataset dataset)
    {
        int k = dataset.getNumLabels();
        for (int i = 0; i<M; i++)
        {
            this.treeClassifier = createTreeClassifier(dataset,k);
            Classification classification = treeClassifier.classify(dataset);
            dataset.addClassificationToDataset(classification);
        }
        return createCftClassifier(this.treeClassifier);
    }
}