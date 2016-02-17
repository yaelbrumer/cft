package datasets;

import weka.core.Instance;

/**
 * Created by eyapeleg on 2/14/2016.
 */
public class CftInstance implements Cloneable {

    private final Instance instance;
    private final String yPredicted;
    private final String t;
    private static int tMax; //todo - implement

    public CftInstance(final Instance instance,final String yPredicted) {
        this.instance = instance;
        this.yPredicted = yPredicted;
        this.t=yPredicted;
    }

    private CftInstance(final Instance instance,final String yPredicted,final String t){
        this.instance = instance;
        this.yPredicted = yPredicted;
        this.t=t;
    }

    public Instance getInstance() {
        return instance;
    }

    public String getT() {
        return this.t;
    }

    public CftInstance getParent(){
        String tParent=this.t.substring(0,this.t.length()-1);//todo - verify index
        return new CftInstance(instance,yPredicted,tParent);
    }

    public CftInstance getLeftChild(){
        return new CftInstance(instance,yPredicted,this.t+"0");
    }

    public CftInstance getRightChild(){
        return new CftInstance(instance,yPredicted,this.t+"1");
    }
}
