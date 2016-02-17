import classifiers.TreeClassifier;
import datasets.CftInstance;
import datasets.MultiLabelDataset;
import interfaces.CostCalculator;
import interfaces.CostClassifier;

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


    private TreeClassifier buildTreeClassifier(final MultiLabelDataset dataset, final TreeClassifier treeClassifier, final int k)
    {
        for (int i = k; i > 0; i++)//todo - verify indexing
        {
            int j=1;
            for(CftInstance cftInstance:dataset){
                //Instance instance = cftInstance.getInstance();
                String yPredicted = cftInstance.getYPredicted();
                String t = yPredicted.substring(0,yPredicted.length()-j); //todo - verify indexing, move to utils
                cftInstance.setT(t);
                String t0 = t+"0"; //todo - move to utils
                String t1 = t +"1"; //todo - move to utils
              //  treeClassifier.classify(instance)
              //  instance.setValue(9999,t); //set t, todo - replace 999 with the relevant attribute/index
               // j++;
            }
        }
        return treeClassifier;
    }
/*
    public CftClassifier train(MultiLabelDataset dataset)
    {
       // int k = dataset.getNumLabels();
       // TreeClassifier treeClassifier = new TreeClassifier();

        for (int i = 0; i<M; i++)
        {
           // treeClassifier = buildTreeClassifier(dataset, treeClassifier, k);
          //  Classification classification = treeClassifier.classify(dataset);
         //   dataset.addClassificationToDataset(classification);
        }

       // return new CftClassifier(treeClassifier);
    }*/
}