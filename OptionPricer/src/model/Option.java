package model;

/**
 * @author Tianyu (Sky) XU
 * @since 02.28.2015
 * @version 1.3.0
 * A class for model.Option. This class inherited from Product class, in which the basic attributes of an option are defined.
 */
public class Option extends Product {
    private OptionRight right;
    private OptionStyle style;
    
    //additional parameters for FiniteDifference    
    private int FDnti;      //number of time intervals
    private int FDnpi;      //number of price intervals
    private double FDsmax;    //the max limitation stock price
    
    //additional parameters for BinoialTree
    private int BTnti;      //number of time intervals
   
    //additional parameters for Simulation
    private int Snti;       //number of time intervals
    private int Snt;        //number of trials
    /**
     * This method returns the right of an option object.
     * @return the return value is of enum OptionRight type. The possible values are 'PUT' & 'CALL'.
     */
    public OptionRight getRight() {
        return right;
    }
    /**
     * This method sets the right of an option object.
     * @param right the parameter is of enum OptionRight type. The attribute 'right' in the option class will be set to this value.
     */
    public void setRight(OptionRight right) {
        this.right = right;
    }
    /**
     * This method returns the style of an option object.
     * @return the return value is of enum OptionStyle type. The possible values are 'AMERICAN', 'EUROPEAN' & 'ASIAN'.
     */
    public OptionStyle getStyle() {
        return style;
    }
    /**
     * This method sets the style of an option object.
     * @param style the parameter is of enum OptionStyle type. The attribute 'style' in the option class will be set to this value.
     */
    public void setStyle(OptionStyle style) {
        this.style = style;
    }

    /**
     * Default constructor.
     */
    public Option(){}

    /**
     * Constructor.
     * @param right The option right.
     * @param style The option style.
     */
    public Option(OptionRight right, OptionStyle style) {
        super();
        this.right = right;
        this.style = style;
    }
    /**
     * This method returns the value of number of time intervals for numerical integration method
     * @return 
     */
    public int getFDnti() {
        return FDnti;
    }
    /**
     * This method set the value of number of time intervals for numerical integration method
     * @param FDnti 
     */
    public void setFDnti(int FDnti) {
        this.FDnti = FDnti;
    }
    /**
     * This method returns the value of number of price intervals for numerical integration method
     * @return 
     */
    public int getFDnpi() {
        return FDnpi;
    }
    /**
     * This method sets the value of number of price intervals for numerical integration method
     * @param FDnpi 
     */
    public void setFDnpi(int FDnpi) {
        this.FDnpi = FDnpi;
    }
    /**
     * This method returns the value of limitation of stock price for numerical integration method
     * @return 
     */
    public double getFDsmax() {
        return FDsmax;
    }
    /**
     * This method sets the value of limitation of stock price for numerical integration method
     * @param FDsmax 
     */
    public void setFDsmax(double FDsmax) {
        this.FDsmax = FDsmax;
    }
    /**
     * This method returns the value of number of time intervals for binomial tree method
     * @return 
     */
    public int getBTnti() {
        return BTnti;
    }
    /**
     * This method sets the value of number of time intervals for binomial tree method
     * @param BTnti 
     */
    public void setBTnti(int BTnti) {
        this.BTnti = BTnti;
    }
    /**
     * This method returns the value of number of time intervals for simulation method
     * @return 
     */
    public int getSnti() {
        return Snti;
    }
    /**
     * This method sets the value of number of time intervals for simulation method
     * @param Snti 
     */
    public void setSnti(int Snti) {
        this.Snti = Snti;
    }
    /**
     * This method returns the value of number of trials for simulation method
     * @return 
     */
    public int getSnt() {
        return Snt;
    }
    /**
     * This method returns the value of number of trials for simulation method
     * @param Snt 
     */
    public void setSnt(int Snt) {
        this.Snt = Snt;
    }

    /**
     * Constructor.
     * @param strikeP strike price.
     * @param sNought current stock price.
     * @param riskFreeRate the risk-free interest rate.
     * @param volatility volatility of the stock.
     * @param term the term of option in MONTHS.
     * @param right the option right.
     * @param style the option style.
     */
    public Option(double strikeP, double sNought, double riskFreeRate, double volatility, double term, OptionRight right, OptionStyle style) {
        super(strikeP, sNought, riskFreeRate, volatility, term);
        this.right = right;
        this.style = style;
    }
}