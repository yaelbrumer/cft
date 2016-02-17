package datasets;

import exceptions.NotImplementedException;
import weka.core.Instance;

/**
 * Created by eyapeleg on 2/14/2016.
 */
public class CftInstance implements Cloneable {

    private final Instance instance;
    private final String yPredicted;
    private final String yActual;
    private final String t;
    private String bn;
    private double wn;

    private static int tMax; //todo - implement

    public CftInstance(final Instance instance,final String yPredicted,final String yActual) {
        this.instance = instance;
        this.yPredicted = yPredicted;
        this.yActual = yActual;
        this.t=yPredicted;
        this.bn= ""; //todo - check what should be the initialization
    }

    private CftInstance(final Instance instance,final String yPredicted,final String yActual,final String t){
        this.instance = instance;
        this.yPredicted = yPredicted;
        this.yActual = yActual;
        this.t=t;
        this.bn= ""; //todo - check what should be the initialization
    }

    public Instance getInstance() {
        return instance; //todo - first update instance fields to match t
    }

    public String getYactual() {
        return yActual;
    }

    public String getT() {
        return t;
    }

    public CftInstance getParent(){
        String tParent=t.substring(0,t.length()-1);//todo - verify index
        return new CftInstance(instance,yPredicted,yActual,tParent);
    }

    public CftInstance getLeftChild(){
        return new CftInstance(instance,yPredicted,yActual,t+"0");
    }

    public CftInstance getRightChild(){
        return new CftInstance(instance,yPredicted,yActual,t+"1");
    }

/*    public CftInstance getParentLevelN(int level) {
        String tParentLevelN=t.substring(0,t.length()-level);//todo - verify index
        return new CftInstance(instance,yPredicted,yActual,tParentLevelN);
    }*/

    public CftInstance getPredictedChild(String classification) {
        if (classification=="0")
            return getLeftChild();
        else if (classification=="1")
            return getRightChild();
        else throw new IllegalArgumentException("classification must be either '0' or '1'");
    }


    public void setBn(Double costClass0, Double costClass1) {
        bn = costClass0>costClass1?"1":"0";
    }

    public void setWn(double cost){
        wn=cost;
    }

    public void updateDataValues() {
        instance.setValue(-1,bn); //todo - values should be set to the coressponding index
        instance.setValue(-1,t); //todo - values should be set to the coressponding index
        instance.setWeight(wn);
    }

    public CftInstance getRoot() {
        return new CftInstance(instance,yPredicted,yActual,t+"0");
    }

}
