package model;

/**
 * This class inherits from Algorithm. It is used for calculating option price Black-Scholes formula.
 * @author Tianyu (Sky) XU
 * @since 02.28.2015
 * @version 1.2.0
 * Note: Black-Scholes is applicable for European call & put option and American call option.
 */
public class BlackScholesModel extends Algorithm{
    /**
     * Constructor.
     */
    public BlackScholesModel() {
    }

    /**
     * A method to calculate the price of an model.Option by Black-Scholes formula.
     * @param o the model.Option object to be calculated.
     * @return the result.
     */
    @Override
    public double[] computeOption(Option o){
        double[] prices=new double[NUMOFDOTS];
        double temp=o.getVolatility();
        double vBase=o.getVolatility();
        int count= (NUMOFDOTS-1)/2; // count = 5

        if(o.getRight()==OptionRight.PUT){
            prices[count]=crunchPut(o); // Middle point should be the "original" option.
            for (int i = count ; i >0 ; i--) {
                o.setVolatility(vBase*(1-i* VOLAINTERVAL));
                prices[count-i]=crunchPut(o);
                o.setVolatility(vBase*(1+i* VOLAINTERVAL));
                prices[count+i]=crunchPut(o);
            }
        }

        if(o.getRight()==OptionRight.CALL){
            prices[count]=crunchCall(o); // Middle point should be the "original" option.
            for (int i = count ; i >0 ; i--) {
                o.setVolatility(vBase*(1-i* VOLAINTERVAL));
                prices[count-i]=crunchCall(o);
                o.setVolatility(vBase*(1+i* VOLAINTERVAL));
                prices[count+i]=crunchCall(o);
            }
        }
        o.setVolatility(temp);
        return prices;
    }


    /**
     * A method to calculate the price of a put model.Option by Black-Scholes formula.
     * @param o the model.Option object to be calculated.
     * @return the result.
     */

    private double crunchPut(Option o){
        double s0=o.getsNought();
        double k=o.getStrikeP();
        double t=o.getTerm();
        double r=o.getRiskFreeRate();
        double sigma=o.getVolatility();
        double d1=(Math.log(s0/k)+(r+sigma*sigma/2)*t)/(sigma*Math.sqrt(t));
        double d2=d1-sigma*Math.sqrt(t);
        return k*Math.exp(-r*t)*cdf(-d2)-s0*cdf(-d1);
    }

    /**
     * A method to calculate the price of a call model.Option by Black-Scholes formula.
     * @param o the model.Option object to be calculated.
     * @return the result.
     */
    private double crunchCall(Option o){
        double s0=o.getsNought();
        double k=o.getStrikeP();
        double t=o.getTerm();
        double r=o.getRiskFreeRate();
        double sigma=o.getVolatility();
        double d1=(Math.log(s0/k)+(r+sigma*sigma/2)*t)/(sigma*Math.sqrt(t));
        double d2=d1-sigma*Math.sqrt(t);
        return s0*cdf(d1)-k*Math.exp(-r*t)*cdf(d2);
    }

    /** Compute the cumulative (up to x) of the standard normal distribution.
     * Taken directly from Abramowitz and Stegun, 1972. Changed a bit to make it work for java.
     * @param x
     * @return the cumulative probability distribution for the given x.
     */
    private double cdf(double x){
        final double GAMMA = 0.2316419;
        final double A1 = 0.319381530;
        final double A2 = -0.356563782;
        final double A3 = 1.781477937;
        final double A4 = -1.821255978;
        final double A5 = 1.330274429;
        boolean xIsNeg = false;
        if (x < 0.0) {
            xIsNeg = true;
            x = -x;
        }

        double k = 1.0/(1.0 + GAMMA * x);
        double oneOverSqrtTwoPI = 1.0 / Math.sqrt(2*Math.PI);
        double nPrime = (1.0/Math.sqrt(2*Math.PI))*Math.exp((-0.5)*x*x);
        double kSquared = k*k;
        double kFourth = kSquared * kSquared;
        double result = 1.0 -
                nPrime*(A1*k + A2*kSquared + A3*k*kSquared + A4*kFourth + A5*kFourth*k);
        if (xIsNeg)
            return 1-result;
        else
            return result;
    }

}
