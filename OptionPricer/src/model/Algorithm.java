package model;

/**
 * @author Tianyu(Sky) Xu
 * @since 02.26.2015
 * @version 1.0.0
 * An abstract class for algorithms to calculate the price of financial products.
 * This class is the parent class. In this project the children classes are BlackScholes, BinomialTree, 
 * FiniteDifference and Simulation, of which all are used for calculate option price.
 */
public abstract class Algorithm {
    /**
     * Result of calculation.Result of calculation. 11 values are stored in this array for the purpose of drawing a volatility smile graph.
     */
    double[] results;
    /**
     * Constant. The number of dots used to construct the volatility smile.
     * The range for the change of volatility is the original volatility +-50%.
     */
    final int NUMOFDOTS=11;
    /**
     * Constant. The interval of change in volatility in the graph.
     */
    final double VOLAINTERVAL =0.1;
    /**
     * An abstract method to calculate the price of an Option.
     * @param o the model.Option object to be calculated.
     * @return Because we need 11 option prices responded to 11 different volatility values to draw the graph, here we run the calculator 11 times for a specific option with a selected method to generate 11 option prices for the purpose of drawing graph.
     *         The expected option price to print on the screen is the 6th value in this result array.
     */
    abstract public double[] computeOption(Option o);


}
