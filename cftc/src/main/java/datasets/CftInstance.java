package datasets;

import weka.core.Instance;

/**
 * Created by eyapeleg on 2/14/2016.
 */
public class CftInstance implements Cloneable {

    private Instance instance;
    private String yPredicted;

    public CftInstance(Instance instance, String yPredicted) {
        this.instance = instance;
        this.yPredicted = yPredicted;
    }

    public Instance getInstance() {
        return instance;
    }

    public String getYPredicted() {
        return yPredicted;
    }

    public CftInstance clone(){
        return new CftInstance(instance,yPredicted);
    }
}
