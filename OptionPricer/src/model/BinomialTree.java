/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
/**
 *
 * @author jameslaney
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
        }

        public BinomialTree(){};
        @Override
        public double[] computeOption(Option option){
            System.out.println("Up prob: " + up);
            System.out.println("Down prob: " + down);
            System.out.println("TimeStep: " + deltaT);
            int i;double volatility = option.getVolatility();;
            if(option.getRight() == OptionRight.PUT){
                
                for(i=0;i<=5;i++){
                    
                    option.setVolatility(volatility - volatility*0.5/5*(5-i));
                    System.out.println("Calculating PUT Option");
                    results[i] = crunchPut(option); 
                }
                for(i=6;i<11;i++){
                    option.setVolatility(volatility + volatility/0.5/5 * (i-5));
                    System.out.println("Calculating PUT Option");
                    results[i] = crunchPut(option);
                }
                
            }else {
                
                System.out.println("Calculating Call Option");
                crunchCall(option);
            }
            return results;
        }
        
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
            System.out.println("Initilized Tree");
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
            
            for ( i = numIntervals-1; i >= 0; i--) {
		for ( j = 0; j <= i; j++) {
			binomialTree[i][j].optionPrice = 
                                Math.max(option.getStrikeP() - binomialTree[i][j].stockPrice ,
					discount*(upProb*binomialTree[i+1][j+1].optionPrice + downProb*binomialTree[i+1][j].optionPrice));
                        
                        
                        /** original code for put options
                         * binomialTree[i][j].optionPrice = 
                                Math.max(option.getStrikeP() - binomialTree[i][j].stockPrice,
					discount*(upProb*binomialTree[i+1][j+1].optionPrice + downProb*binomialTree[i+1][j].optionPrice));
                         */
		}
            }
            
            binomValue = binomialTree[0][0].optionPrice;
            System.out.println("Option price for " + option.getClass().getName() );
            System.out.println("value: " + binomValue);
            return binomValue;
        }
        
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
            
            for ( i = numIntervals-1; i >= 0; i--) {
		for ( j = 0; j <= i; j++) {
			binomialTree[i][j].optionPrice = 
                                Math.max(binomialTree[i][j].stockPrice - option.getStrikeP(),
					discount*(upProb*binomialTree[i+1][j+1].optionPrice + downProb*binomialTree[i+1][j].optionPrice));
                        
                        
                        /** original code for put options
                         * binomialTree[i][j].optionPrice = 
                                Math.max(option.getStrikeP() - binomialTree[i][j].stockPrice,
					discount*(upProb*binomialTree[i+1][j+1].optionPrice + downProb*binomialTree[i+1][j].optionPrice));
                         */
		}
            }
            
            binomValue = binomialTree[0][0].optionPrice;
            System.out.println("Option price for " + option.toString() );
            System.out.println("value: " + binomValue);
            return binomValue;
        }
        
}