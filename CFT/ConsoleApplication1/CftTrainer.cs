using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Classification.CFT
{
    public class CftTrainer
    {
        private DatasetPrediction datasetPrediction;
        private int M;

        private TreeClassifier treeClassifier;

        private ICostCalculator costCalculator;
        private IWeightedClassifier weightedClassifier;

        public CftTrainer(ICostCalculator costCalculator, IWeightedClassifier trainer, int M)
        {
            this.costCalculator = costCalculator;
            this.weightedClassifier = trainer;
            this.M = M;
        }

        private DatasetPrediction createInitialPredictionDataSet(Dataset dataset)
        {
            throw new NotImplementedException();
        }

        private int caluculateK(Dataset dataset)
        {
            throw new NotImplementedException();
        }

        private DatasetLayerK dataPreparation(DatasetPrediction dp)
        {
            throw new NotImplementedException();
        }

        private TreeClassifier createTreeClassifier(DatasetPrediction datasetPrediction, int k)
        {
            for (int i = k; i > 0; i++)//todo - verify indexing 
            {
                DatasetLayerK datasetLayerK = dataPreparation(datasetPrediction);
                LayerClassifier layerClassifier = weightedClassifier.train(datasetLayerK);
                treeClassifier.addLayer(layerClassifier);
            }
            return treeClassifier;
        }

        private DatasetPrediction addClassificationToDataset(Classification Classification, DatasetPrediction datasetPrediction)
        {
            throw new NotImplementedException();
        }

        private CftClassifier createCftClassifier(TreeClassifier treeClassifier)
        {
            throw new NotImplementedException();
        }

        public CftClassifier train(Dataset dataset)
        {
            this.datasetPrediction = createInitialPredictionDataSet(dataset);
            int k = caluculateK(dataset);
            for (int i = 0; i<M; i++)
            {
                this.treeClassifier = createTreeClassifier(datasetPrediction,k);
                Classification classification = treeClassifier.classify(dataset);
                this.datasetPrediction = addClassificationToDataset(classification, this.datasetPrediction);
            }
            return createCftClassifier(this.treeClassifier);
        }
    }
        
    
}
