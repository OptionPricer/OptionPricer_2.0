
package model;
/**
 * This class inherits from Algorithm. It is used for calculating option price with binomial tree.
 * @author jameslaney
 * @since 03.03.2015
 * @version 1.3.0
 */
public class BinomialTree extends Algorithm {
        
        double deltaT;
    
        
    // Alternate values suggested by Prof. Hrusa
	private double up;		
	private double down;		
	private double upProb	= 0.5;
        private double downProb = 0.5;
        private int numIntervals = 0;
        private Price[][] binomialTree;
        double binomValue;
        /**
         * Inner class used by binomial tree valuation method
         */
        class Price{
             
             double stockPrice = 0.0;
             double optionPrice=0.0;
             Price(double sP, double oP){
                 this.stockPrice = sP;
                 this.optionPrice =oP;
             }
        }
        /**
         * constructs binomial tree for computing options prices
         * @param option put or call option 
         * @param intervals use specified number of times price should be evaluated
         */
        public BinomialTree(Option option, int intervals){
            super();
            this.numIntervals = intervals;
            deltaT = option.getTerm() / numIntervals;
            results = new double[11];           
        }

        public BinomialTree(){};
        /**
         * This method is inherited from the parent class Algorithm, which implement 
         * the calculation of option price 11 times and generate 11 option price responded to 11 
         * different volatilities for the purpose of drawing volatility smile graph.
         * @param option this is the option object to whose option price is to be calculated.
         * @return The return is an array of option price, which will used to generate 11 points and 
         */
        @Override
        public double[] computeOption(Option option){
            double temp=option.getVolatility();
            //double[] results = new double[11];
            int i;double volatility = option.getVolatility();;
            if(option.getRight() == OptionRight.PUT){
                
                for(i=0;i<=5;i++){
                    
                    option.setVolatility(volatility - volatility*0.5/5*(5-i));
                    results[i] = crunchPut(option); 
                }
                for(i=6;i<11;i++){
                    option.setVolatility(volatility + volatility/0.5/5 * (i-5));
                    results[i] = crunchPut(option);
                }
                
            }else 
            {
                for(i=0;i<=5;i++){
                    
                    option.setVolatility(volatility - volatility*0.5/5*(5-i));
                    results[i] = crunchCall(option); 
                }
                for(i=6;i<11;i++){
                    option.setVolatility(volatility + volatility/0.5/5 * (i-5));
                    results[i] = crunchCall(option);
                }
                
                
            }
            option.setVolatility(temp);
            return results;
        }
        /**
         * This crunchPut method calculates put option price for American, European option.
         * @param option This is the option object whose price is to be calculated.
         * @return The return value is the price of the given option.
         */
        private double crunchPut(Option option){
            int i,j;
            up = 1.0 + option.getRiskFreeRate() * deltaT + (option.getVolatility()*Math.sqrt(deltaT));
            down = 1.0 + option.getRiskFreeRate() * deltaT - (option.getVolatility()*Math.sqrt(deltaT));
                                            
            binomialTree = new Price[numIntervals+1][];
            
            for (i = 0; i <= numIntervals; i++) {
                binomialTree[i] = new Price[i + 1];
            }
            for ( i = 0; i <= numIntervals; i++) {
		for ( j = 0; j <= i; j++) {
                        binomialTree[i][j] =  new Price(option.getsNought() * Math.pow(up, j) * Math.pow(down, i-j), 0);
		}
            }
            // Fill the optionPrices at the terminal nodes for CALL = max(S(t) - k, 0) option
//            for ( j = 0; j <= numIntervals; j++) {
//		binomialTree[numIntervals][j].optionPrice = Math.max(binomialTree[numIntervals][j].stockPrice - option.getStrikeP(), 0.0);
//            }
            // Fill the optionPrices at the terminal nodes for PUT = max(K - S(t),0) option
            for ( j = 0; j <= numIntervals; j++) {
		binomialTree[numIntervals][j].optionPrice = Math.max(option.getStrikeP() - binomialTree[numIntervals][j].stockPrice, 0.0);
            }
            
            // Now work backwards, filling optionPrices in the rest of the tree
            double discount = Math.exp(-option.getRiskFreeRate()*deltaT);
            if(option.getStyle() == OptionStyle.AMERICAN){
            for ( i = numIntervals-1; i >= 0; i--) {
		for ( j = 0; j <= i; j++) {
			binomialTree[i][j].optionPrice = 
                                Math.max(option.getStrikeP() - binomialTree[i][j].stockPrice ,
					discount*(upProb*binomialTree[i+1][j+1].optionPrice + downProb*binomialTree[i+1][j].optionPrice));
		}
            }
            }
            if(option.getStyle() == OptionStyle.EUROPEAN){
                for(i=this.numIntervals-1;i>=0;i--){
                    for(j=0;j<=i;j++){
                        binomialTree[i][j].optionPrice = discount*(this.upProb*binomialTree[i+1][j+1].optionPrice+this.downProb*binomialTree[i+1][j].optionPrice);
                    }
                }
            }
            binomValue = binomialTree[0][0].optionPrice;
            return binomValue;
        }
        /**
         * This crunchCall method calculates call option price for American, European option.
         * @param option This is the option object whose price is to be calculated.
         * @return The return value is the price of the given option.
         */
        private double crunchCall(Option option){
          
            int i,j;
          
            up = 1.0 + option.getRiskFreeRate() * deltaT + (option.getVolatility()*Math.sqrt(deltaT));
            down = 1.0 + option.getRiskFreeRate() * deltaT - (option.getVolatility()*Math.sqrt(deltaT));
            
            
            binomialTree = new Price[numIntervals+1][];
            
            for (i = 0; i <= numIntervals; i++) {
                binomialTree[i] = new Price[i + 1];
            }
            //Fill the stockPrice componet of the binomial Tree
            for ( i = 0; i <= numIntervals; i++) {
		for ( j = 0; j <= i; j++) {
                        binomialTree[i][j] =  new Price(option.getsNought() * Math.pow(up, j) * Math.pow(down, i-j), 0);
		}
            }
            System.out.println("Initilized Tree");
            // Fill the optionPrices at the terminal nodes for CALL = max(S(t) - k, 0) option
            for ( j = 0; j <= numIntervals; j++) {
		binomialTree[numIntervals][j].optionPrice = Math.max(binomialTree[numIntervals][j].stockPrice - option.getStrikeP(), 0.0);
            }
            
            
            // Now work backwards, filling optionPrices in the rest of the tree
            double discount = Math.exp(-option.getRiskFreeRate()*deltaT);
            if(option.getStyle() == OptionStyle.AMERICAN){
            for ( i = numIntervals-1; i >= 0; i--) {
		for ( j = 0; j <= i; j++) {
			binomialTree[i][j].optionPrice = 
                                Math.max(binomialTree[i][j].stockPrice - option.getStrikeP(),
					discount*(upProb*binomialTree[i+1][j+1].optionPrice + downProb*binomialTree[i+1][j].optionPrice));

		}
            }
            }
            if(option.getStyle() == OptionStyle.EUROPEAN){
                for ( i = numIntervals-1; i >= 0; i--) {
		for ( j = 0; j <= i; j++) {
			binomialTree[i][j].optionPrice = 
                                Math.max(binomialTree[i][j].stockPrice - option.getStrikeP(),
					discount*(upProb*binomialTree[i+1][j+1].optionPrice + downProb*binomialTree[i+1][j].optionPrice));

		}
                }  
            }
            binomValue = binomialTree[0][0].optionPrice;
            return binomValue;
        }       
        
}
