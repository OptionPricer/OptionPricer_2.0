package model;
import java.util.*;
/**
 *
 * @author catherine_Liu
 */
public class FiniteDifference extends Algorithm{   
    int numOfTimeInterval;
    int numOfPriceInterval;
    double sMax;
        
    public FiniteDifference(){
       
    }
    public FiniteDifference(int N, int M, double sMax){
        this.numOfTimeInterval = N;
        this.numOfPriceInterval = M;
        this.sMax = sMax;
    }
    

    public double crunchPut(Option o){
        int i,j;
        int N = this.numOfTimeInterval;
        int M = this.numOfPriceInterval;
        double deltaT = o.getTerm() / N;
        double deltaS = this.sMax / M;
        
        double[][] f = new double[N+1][];
        for(i=0;i<=N;i++){
            f[i] = new double[M+1];
            for(j=0;j<=M;j++){
                f[i][j] = 0.0;
            }
        }
        //initialize stock price array
        double[] stockPrice = new double[M+1];
        for(j=0;j<=M;j++){
            stockPrice[j] = j * this.sMax / M;
        }
        //initial stock price at zero and sMax
                for(i=0;i<=N;i++){
                    f[i][0] = o.getStrikeP();
                    f[i][M] = 0.0;
                }

        //initialize option value at maturity
                for(j=0;j<=M;j++){
                    f[N][j]=Math.max(o.getStrikeP()-stockPrice[j],0.0);
                }      
        //Calculate interior values
        //These are arrays to be sent to the tridiagonal algorithm
        double[] a = new double[M-1];
        double[] b = new double[M-1];
        double[] c = new double[M-1];
        double[] r = new double[M-1];
        double[] u = new double[M-1];
        for(j=0;j<M-1;j++){           //note the offsets
            a[j] = aj(j+1,deltaT,o);
            b[j] = bj(j+1,deltaT,o);
            c[j] = cj(j+1,deltaT,o);
        }
        //The big loop over columns, backward from N-1 to zero
        for(i=1;i<=N;i++){
            for(j=0;j<M-1;j++){
                r[j] = f[N-i+1][j+1];               
            }
            r[0] = r[0]-a[0]*f[N-i][0];
            r[M-2] = r[M-2] - c[M-2]*f[N-i][M];
            //Solve the tridiagonal system of equations.
            //The solutions ar placed in u.
            tridiag(a,b,c,r,u,M-1);

            if(o.getStyle() == OptionStyle.AMERICAN){
                for(j=1;j<M;j++){
                    if(u[j-1] < o.getStrikeP() - j * deltaS)
                        f[N-i][j] = o.getStrikeP() - j * deltaS;
                    else
                        f[N-i][j] = u[j-1];
                } 
            }
            if(o.getStyle() == OptionStyle.EUROPEAN){
                for(j=1;j<M;j++){
                    f[N-i][j] = u[j-1];
                }
            }          
        }        
        double optionValue;
        j = 0;
        while(stockPrice[j] < o.getsNought())
            j++;
        if(stockPrice[j] == o.getsNought())
            optionValue = f[0][j];
        else
            //linear interpolation; we can do better
            optionValue = f[0][j-1] + (f[0][j] - f[0][j-1]) * (o.getsNought()-stockPrice[j-1])/(stockPrice[j]-stockPrice[j-1]);
        return optionValue;
    }
    

    public double crunchCall(Option o){
        int i,j;
        int N = this.numOfTimeInterval;
        int M = this.numOfPriceInterval;
        double deltaT = o.getTerm() / N;
        double deltaS = this.sMax / M;
        
        double[][] f = new double[N+1][];
        for(i=0;i<=N;i++){
            f[i] = new double[M+1];
            for(j=0;j<=M;j++){
                f[i][j] = 0.0;
            }
        }
        //initialize stock price array
        double[] stockPrice = new double[M+1];
        for(j=0;j<=M;j++){
            stockPrice[j] = j * this.sMax / M;
        }
        //initial stock price at zero and sMax
       
                for(i=0;i<=N;i++){
                    f[i][0] = 0.0;
                    f[i][M] = o.getStrikeP();
                }            
        
        //initialize option value at maturity
        if(o.getRight() == OptionRight.CALL){
            for(j=0;j<=M;j++){
                f[N][j]=Math.max(stockPrice[j]-o.getStrikeP(),0.0);
            }
        }
        //Calculate interior values
        //These are arrays to be sent to the tridiagonal algorithm
        double[] a = new double[M-1];
        double[] b = new double[M-1];
        double[] c = new double[M-1];
        double[] r = new double[M-1];
        double[] u = new double[M-1];
        for(j=0;j<M-1;j++){           //note the offsets
            a[j] = aj(j+1,deltaT,o);
            b[j] = bj(j+1,deltaT,o);
            c[j] = cj(j+1,deltaT,o);
        }
        //The big loop over columns, backward from N-1 to zero
        for(i=1;i<=N;i++){
            for(j=0;j<M-1;j++){
                r[j] = f[N-i+1][j+1];               
            }
            r[0] = r[0]-a[0]*f[N-i][0];
            r[M-2] = r[M-2] - c[M-2]*f[N-i][M];
            //Solve the tridiagonal system of equations.
            //The solutions ar placed in u.
            tridiag(a,b,c,r,u,M-1);
            if(o.getRight() == OptionRight.CALL){
            if(o.getStyle() == OptionStyle.AMERICAN){
               for(j=1;j<M;j++){
                if(u[j-1] < j * deltaS - o.getStrikeP())
                    f[N-i][j] = j * deltaS - o.getStrikeP();
                else
                    f[N-i][j] = u[j-1];
                } 
            }
            
            if(o.getStyle() == OptionStyle.EUROPEAN){
                for(j=1;j<M;j++){
                    f[N-i][j] = u[j-1];
                }
            }
            }
        }        
        double optionValue;
        j = 0;
        while(stockPrice[j] < o.getsNought()) //
            j++;
        if(stockPrice[j] == o.getsNought())
            optionValue = f[0][j];
        else
            //linear interpolation; we can do better
            optionValue = f[0][j-1] + (f[0][j] - f[0][j-1]) * (o.getsNought()-stockPrice[j-1])/(stockPrice[j]-stockPrice[j-1]);
        return optionValue;
    
    }
    
    @Override
    public double[] computeOption(Option o){
        double temp=o.getVolatility();
        double[] optionValues = new double[11];
        int i = 0;
        for(i=0;i<=5;i++){
            o.setVolatility(0.4-0.2/5*(5-i));
            if(o.getRight()== OptionRight.PUT)
                optionValues[i] = this.crunchPut(o);
            if(o.getRight() == OptionRight.CALL)
                optionValues[i] = this.crunchCall(o);
        }
        for(i=6;i<11;i++){
            o.setVolatility(0.4+0.2/5*(i-5));
            if(o.getRight()== OptionRight.PUT)
                optionValues[i] = this.crunchPut(o);
            if(o.getRight() == OptionRight.CALL)
                optionValues[i] = this.crunchCall(o);
        }
        o.setVolatility(temp);
        return optionValues;
    }
    
    private void tridiag(double[] a, double[] b, double[] c, double[] r, double[] u, int length) {
	int j;
	double bet;
	double[] gam = new double[length];
	u[0] = r[0] /( bet=b[0]);
	for (j = 1; j < length; j++) {
		gam[j] = c[j-1] / bet;
		bet = b[j] - a[j] * gam[j];
		u[j] = (r[j] - a[j] * u[j-1]) / bet;
	}
	for (j = (length-2); j >= 0; j--)
		u[j] -= gam[j+1] * u[j+1];
    }
    
    private double aj(int j, double deltaT,Option o){
        double riskFreeRate = o.getRiskFreeRate();
        double volatility = o.getVolatility();
        return (0.5 * riskFreeRate * j * deltaT - 0.5 * volatility * volatility * j * j * deltaT);
    }
    private double bj(int j, double deltaT,Option o){
        double riskFreeRate = o.getRiskFreeRate();
        double volatility = o.getVolatility();
        return (1.0 + volatility*volatility*j*j*deltaT + riskFreeRate*deltaT);
    }
    private double cj(int j, double deltaT,Option o){
        double riskFreeRate = o.getRiskFreeRate();
        double volatility = o.getVolatility();
        return (-0.5*riskFreeRate*j*deltaT - 0.5*volatility*volatility*j*j*deltaT);
    }
    
/*    public double calculate(Option o){
        int i,j;
        int N = this.numOfTimeInterval;
        int M = this.numOfPriceInterval;
        double deltaT = this.Option.getTerm() / N;
        double deltaS = this.sMax / M;
        
        double[][] f = new double[N+1][];
        for(i=0;i<=N;i++){
            f[i] = new double[M+1];
            for(j=0;j<=M;j++){
                f[i][j] = 0.0;
            }
        }
        //initialize stock price array
        double[] stockPrice = new double[M+1];
        for(j=0;j<=M;j++){
            stockPrice[j] = j * this.sMax / M;
        }
        //initial stock price at zero and sMax
        if((this.Option.getRight() == OptionRight.PUT)){        
                for(i=0;i<=N;i++){
                    f[i][0] = this.Option.getStrikeP();
                    f[i][M] = 0.0;
                }
            
        }
        if((this.Option.getRight() == OptionRight.CALL)){         
                for(i=0;i<=N;i++){
                    f[i][0] = 0.0;
                    f[i][M] = this.Option.getStrikeP();
                }
        }
            
        
        //initialize option value at maturity
        if(this.Option.getRight() == OptionRight.PUT){
                for(j=0;j<=M;j++){
                    f[N][j]=Math.max(this.Option.getStrikeP()-stockPrice[j],0.0);
                }
            
        }
        if(this.Option.getRight() == OptionRight.CALL){
            for(j=0;j<=M;j++){
                f[N][j]=Math.max(stockPrice[j]-this.Option.getStrikeP(),0.0);
            }
        }
        //Calculate interior values
        //These are arrays to be sent to the tridiagonal algorithm
        double[] a = new double[M-1];
        double[] b = new double[M-1];
        double[] c = new double[M-1];
        double[] r = new double[M-1];
        double[] u = new double[M-1];
        for(j=0;j<M-1;j++){           //note the offsets
            a[j] = aj(j+1,deltaT);
            b[j] = bj(j+1,deltaT);
            c[j] = cj(j+1,deltaT);
        }
        //The big loop over columns, backward from N-1 to zero
        for(i=1;i<=N;i++){
            for(j=0;j<M-1;j++){
                r[j] = f[N-i+1][j+1];               
            }
            r[0] = r[0]-a[0]*f[N-i][0];
            r[M-2] = r[M-2] - c[M-2]*f[N-i][M];
            //Solve the tridiagonal system of equations.
            //The solutions ar placed in u.
            tridiag(a,b,c,r,u,M-1);
            if(this.Option.getRight() == OptionRight.PUT){
            if(this.Option.getStyle() == OptionStyle.AMERICAN){
                for(j=1;j<M;j++){
                    if(u[j-1] < this.Option.getStrikeP() - j * deltaS)
                        f[N-i][j] = this.Option.getStrikeP() - j * deltaS;
                    else
                        f[N-i][j] = u[j-1];
                } 
            }
            if(this.Option.getStyle() == OptionStyle.EUROPEAN){
                for(j=1;j<M;j++){
                    f[N-i][j] = u[j-1];
                }
            }
            }
            if(this.Option.getRight() == OptionRight.CALL){
            if(this.Option.getStyle() == OptionStyle.AMERICAN){
               for(j=1;j<M;j++){
                if(u[j-1] < j * deltaS - this.Option.getStrikeP())
                    f[N-i][j] = j * deltaS - this.Option.getStrikeP();
                else
                    f[N-i][j] = u[j-1];
                } 
            }
            
            if(this.Option.getStyle() == OptionStyle.EUROPEAN){
                for(j=1;j<M;j++){
                    f[N-i][j] = u[j-1];
                }
            }
            }
        }
        
        double optionValue;
        j = 0;
        while(stockPrice[j] < this.Option.getsNought())
            j++;
        if(stockPrice[j] == this.Option.getsNought())
            optionValue = f[0][j];
        else
            //linear interpolation; we can do better
            optionValue = f[0][j-1] + (f[0][j] - f[0][j-1]) * (this.Option.getsNought()-stockPrice[j-1])/(stockPrice[j]-stockPrice[j-1]);
            //double [] optionValue = 
        return optionValue;
    }*/
    //Solve a tridiagonal system of linear equations
    
}