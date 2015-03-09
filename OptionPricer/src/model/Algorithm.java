package model;

/**
 * @author Sky
 * An abstract class for algorithms to calculate the price of financial products.
 */
public abstract class Algorithm {
    /**
     * Result of calculation.
     */
    double[] results;
    /**
     * Constant. The number of dots used to construct the volatility smile.
     */
    final int NUMOFDOTS=11;
    /**
     * Constant. The interval of change in volatility in the graph.
     */
    final double VOLAINTERVAL =0.1;
    /**
     * An abstract method to calculate the price of an model.Option.
     * @param o the model.Option object to be calculated.
     * @return the result.
     */
    abstract public double[] computeOption(Option o);


}
