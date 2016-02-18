package datasets;

import exceptions.NotImplementedException;
import weka.core.Instance;

public final class CftInstance implements Cloneable {

    private final Instance instance;
    private final String yPredicted;
    private final String yActual;
    private final int tIndex;
    private final int bIndex;

    public CftInstance(final Instance instance,final String yPredicted,final String yActual) {
        this.instance = instance;
        this.yPredicted = yPredicted;
        this.yActual = yActual;
        this.tIndex=instance.numAttributes()-2;
        this.bIndex=instance.numAttributes()-1;

        instance.setValue(tIndex,yPredicted);
        instance.setValue(bIndex,"-1");
    }


    public final Instance getInstance() {
        return instance; //todo - first update instance fields to match t
    }

    public final String getYactual() {
        return yActual;
    }

    public final void setBn(final String b) {
        instance.setValue(bIndex,b);
    }

    public final void setWn(final double weight){
        instance.setWeight(weight);
    }

    public final void setTtoLevel(final int level) {

        String t = yPredicted.substring(0,yPredicted.length()-level);
        instance.setValue(tIndex,t);
    }

    public final String getT(){
        return instance.stringValue(tIndex);
    }
    public final void setTtoLeftChild() {
        String t = getT()+"0";
        instance.setValue(tIndex,t);
    }

    public final void setTtoRightChild() {
        String t = getT()+"1";
        instance.setValue(tIndex,t);
    }
}
