package interfaces;

import classifiers.LayerClassifier;
import datasets.DatasetLayerK;

/**
 * Created by eyapeleg on 2/12/2016.
 */
public interface WeightedClassifier {

    LayerClassifier train(DatasetLayerK dataset);
}
