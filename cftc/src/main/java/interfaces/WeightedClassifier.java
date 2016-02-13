package interfaces;

import classifiers.LayerClassifier;
import datasets.MultiLabelDatasetLayerK;

/**
 * Created by eyapeleg on 2/12/2016.
 */
public interface WeightedClassifier {

    LayerClassifier train(MultiLabelDatasetLayerK dataset);
}
