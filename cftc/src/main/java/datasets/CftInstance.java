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
        instance.setValue(bIndex,Classification.NONE);
    }


    public final Instance getInstance() {
        return instance;
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

        String t = yPredicted.substring(0,yPredicted.length()-level - 1);
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
}
