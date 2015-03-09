package model;

/**
 * @author Sky
 * A class for model.Option.
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

    public OptionRight getRight() {
        return right;
    }

    public void setRight(OptionRight right) {
        this.right = right;
    }

    public OptionStyle getStyle() {
        return style;
    }

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
    
    public int getFDnti() {
        return FDnti;
    }

    public void setFDnti(int FDnti) {
        this.FDnti = FDnti;
    }

    public int getFDnpi() {
        return FDnpi;
    }

    public void setFDnpi(int FDnpi) {
        this.FDnpi = FDnpi;
    }

    public double getFDsmax() {
        return FDsmax;
    }

    public void setFDsmax(double FDsmax) {
        this.FDsmax = FDsmax;
    }

    public int getBTnti() {
        return BTnti;
    }

    public void setBTnti(int BTnti) {
        this.BTnti = BTnti;
    }

    public int getSnti() {
        return Snti;
    }

    public void setSnti(int Snti) {
        this.Snti = Snti;
    }

    public int getSnt() {
        return Snt;
    }

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