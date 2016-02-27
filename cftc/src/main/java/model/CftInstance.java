package model;

import weka.core.Instance;

public final class CftInstance implements Cloneable {

    private final Instance instance;
    private String yPredicted;
    private final String yActual;
    private final int tIndex;
    private final int bIndex;
    private final int id;

    public CftInstance(final Instance instance,final String yPredicted,final String yActual,int id) {
        this.instance = instance;
        this.yPredicted = yPredicted;
        this.yActual = yActual;
        this.tIndex=instance.numAttributes()-2;
        this.bIndex=instance.numAttributes()-1;
        this.id=id;

        instance.setValue(tIndex,"");
        instance.setValue(bIndex,Classification.NONE);
    }

    public final void setYPredicted(String yPredicted){
        this.yPredicted=yPredicted;
    }

    public final Instance getInstance() {
        return instance;
    }

    public final String getYactual() {
        return yActual;
    }

    public final String getYpredicted() {
        return yPredicted;
    }

    public final int getId() {return id;}

    public final void setBn(final String b) {
        instance.setValue(bIndex,b);
    }

    public final void setWn(final double weight){
        instance.setWeight(weight);
    }

    public final void setTtoLevel(final int level) {
        String t = yPredicted.substring(0,level-1);
        instance.setValue(tIndex,t);
    }

    public final String getT(){
        return instance.stringValue(tIndex);
    }
    public final void setTtoLeftChild() {
        String t = getT()+Classification.LEFT_CHILD;
        instance.setValue(tIndex,t);
    }

    public final void setTtoRightChild() {
        String t = getT()+Classification.RIGHT_CHILD;
        instance.setValue(tIndex,t);
    }

    public CftInstance copy(){
        return new CftInstance((Instance)instance.copy(),yPredicted,yActual,id);
    }

}
