using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApplication1
{
    class CftTrainer : IClassifier
    {
        ICostCalculator costCalculator;
        IPredictor predictor;
        ITrainer trainer;

        public CftTrainer(ICostCalculator costCalculator, IPredictor predictor, ITrainer trainer)
        {
            this.costCalculator = costCalculator;
            this.predictor = predictor;
            this.trainer = trainer;
        }

        private DatasetPrediction createInitialPredictionDataSet(Dataset dataset)
        {
            throw new NotImplementedException();
            return new DatasetPrediction();
        }

        private DatasetLayerK dataPreparation(DatasetPrediction dp)
        {
            throw new NotImplementedException();
            return new DatasetLayerK();
        }

        private TreeClassifier createLayersClassifiers(DatasetPrediction dataset)
        {
            throw new NotImplementedException();
            return new TreeClassifier();
        }

    }
        
    
}
