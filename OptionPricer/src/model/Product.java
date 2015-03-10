package model;

/**
 * @author Tianyu (Sky) XU
 * @since 02.28.2015
 * @version 1.0.0
 * A class of general financial products
 */
public class Product {

    private double strikeP;//strike price
    private double sNought;//current stock price
    private double riskFreeRate;//risk free rate
    private double volatility;//volatility
    private double term;// Unit: years


    /**
     * Constructor.
     */
    public Product() {
    }

    /**
     * Constructor.
     * @param strikeP strike price.
     * @param sNought current stock price.
     * @param riskFreeRate risk free rate.
     * @param volatility volatility.
     * @param term the term in years.
     */
    public Product(double strikeP, double sNought, double riskFreeRate, double volatility, double term) {
        this.strikeP = strikeP;
        this.sNought = sNought;
        this.riskFreeRate = riskFreeRate;
        this.volatility = volatility;
        this.term = term;
    }

    public double getStrikeP() {
        return strikeP;
    }

    public void setStrikeP(double strikeP) {
        this.strikeP = strikeP;
    }

    public double getsNought() {
        return sNought;
    }

    public void setsNought(double sNought) {
        this.sNought = sNought;
    }

    public double getRiskFreeRate() {
        return riskFreeRate;
    }

    public void setRiskFreeRate(double riskFreeRate) {
        this.riskFreeRate = riskFreeRate;
    }

    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public double getTerm() {
        return term;
    }

    public void setTerm(double term) {
        this.term = term;
    }
}
