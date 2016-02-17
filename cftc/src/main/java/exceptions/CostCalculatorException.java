package exceptions;

/**
 * Created by eyapeleg on 2/13/2016.
 */
public class CostCalculatorException extends RuntimeException{

    public CostCalculatorException(String message, Throwable e){super(message, e);}

    public CostCalculatorException(String message){super(message);}

    public CostCalculatorException(Throwable e) {super(e);}

}
