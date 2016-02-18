package datasets;

import exceptions.NotImplementedException;
import weka.core.Instance;

public final class CftInstance implements Cloneable {

    private final Instance instance;
    private final String yPredicted;
    private final String yActual;
    //private final String t;
   // private String bn;
    private double wn;

    private static int tMax; //todo - implement

    public CftInstance(final Instance instance,final String yPredicted,final String yActual) {
        this.instance = instance;
        this.yPredicted = yPredicted;
        this.yActual = yActual;
     //   this.t=yPredicted;
     //   this.bn= ""; //todo - check what should be the initialization
    }

    private CftInstance(final Instance instance,final String yPredicted,final String yActual,final String t){
        this.instance = instance;
        this.yPredicted = yPredicted;
        this.yActual = yActual;
      // this.t=t;
      //  this.bn= ""; //todo - check what should be the initialization
    }

    public final Instance getInstance() {
        return instance; //todo - first update instance fields to match t
    }

    public final String getYactual() {
        return yActual;
    }

  /*  public final String getT() {
        return t;
    }

    public final CftInstance getParent(){
        String tParent=t.substring(0,t.length()-1);//todo - verify index
        return new CftInstance(instance,yPredicted,yActual,tParent);
    }

    public final CftInstance getLeftChild(){
        return new CftInstance(instance,yPredicted,yActual,t+"0");
    }

    public final CftInstance getRightChild(){
        return new CftInstance(instance,yPredicted,yActual,t+"1");
    }*/

   /* public final CftInstance getPredictedChild(String classification) {
        if (classification=="0")
            return getLeftChild();
        else if (classification=="1")
            return getRightChild();
        else throw new IllegalArgumentException("classification must be either '0' or '1'");
    }*/


    public final void setBn(final Double costClass0,final Double costClass1) {
        throw new NotImplementedException();
        //instance.setValue() //todo - implement
        //bn = costClass0>costClass1?"1":"0";
    }

    public final void setWn(final double weight){
        instance.setWeight(weight);
    }

    public void setTtoLevel(int level) {
        throw new NotImplementedException();
        //instance.setValue(); //todo - calculate t according to level
        //this.t = t;
    }

    /*public final CftInstance getRoot() {
        return new CftInstance(instance,yPredicted,yActual,t+"0");
    */}

    public void seTtoLeftChild() {
        throw new NotImplementedException();
    }

    public void seTtoRightChild() {
        throw new NotImplementedException();
    }
}
