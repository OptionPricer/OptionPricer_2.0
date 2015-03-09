package controller;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;
import model.*;

import java.util.ArrayList;

/**
 * @author Sky
 * The Controller class.
 */
public class OPS {


    /**
     * An arraylist to store the names of algorithms chosen by the user.
     */
    public static ArrayList<String> algNames;
    /**
     * An arraylist to store the names of user's own algorithms.
     */
    public static ArrayList<String> customizedAlgNames;
    /**
     * An arraylist to store the algorithms chosen by the user.
     */
    public static ArrayList<Algorithm> algList;
    /**
     * An arraylist to store the calculation resultSet.
     */
    public static ArrayList<double[]> resultSet;
    /**
     * The option object which is to be calculated
     */
    public static Option theOption;
    /**
     * A boolean value indicating whether the user would like to show the diagram.
     */
    public static boolean showDiagram;

    /**
     * To create an option.
     * This method is called when the model.OptionRight and model.OptionStyle is decided,i.e., the Call/Put button is clicked.
     *
     * @param r The model.Option right. (CALL/PUT)
     * @param s The model.Option Style. (ASIAN/EUROPEAN/AMERICAN)
     */
    public static void createOption(OptionRight r, OptionStyle s){
        theOption=new Option(r,s);
        // then it will call findApplicableAlgs().
    }
    public static void initAlgs(){
        algList = new ArrayList<Algorithm>();
        algNames = new ArrayList<String>();
        resultSet = new ArrayList();
        // Not sure what to do so far.
    }


    /**
     * A function to calculate the option price using the String-type formula that user enters.
     * Adapted from the Javaluator v3.0
     * @param expressions An arraylist of the String type formulas.
     * @return An arraylist of the price.
     */
    public static ArrayList<Double> evalExpression(ArrayList<String> expressions){
        DoubleEvaluator de=new DoubleEvaluator();
        StaticVariableSet<Double> vars=new StaticVariableSet<>();
        ArrayList<Double> results=new ArrayList<>();
        double s = theOption.getsNought();
        double k = theOption.getStrikeP();
        double t = theOption.getTerm();
        double o = theOption.getVolatility();
        double r = theOption.getRiskFreeRate();
        vars.set("s",s);
        vars.set("k",k);
        vars.set("t",t);
        vars.set("o",o);
        vars.set("r",r);
        // Evaluate an expression
        try {
            for (String exp : expressions) {
                results.add(de.evaluate(exp, vars));
                //test
                System.out.println(exp + " = " + de.evaluate(exp, vars));
            }
        }
        catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        return results;
    }
    public static void validateFormula(String str) throws Exception{
        DoubleEvaluator de=new DoubleEvaluator();
        StaticVariableSet<Double> vars=new StaticVariableSet<>();
        double s = 0;
        double k = 0;
        double t = 0;
        double o = 0;
        double r = 0;
        vars.set("s",s);
        vars.set("k",k);
        vars.set("t",t);
        vars.set("o",o);
        vars.set("r",r);
        try {
            System.out.println(str + " = " + de.evaluate(str, vars));
        }
        catch (Exception e){
            System.out.println("INPUT ERROR: "+e.getMessage());
            throw e;
        }

    }
    /**
     * A method to calculate option price using all the algorithms that user has chosen.
     */
    public static void compute(){
        for(String s:algNames){
            switch (s){
                case "BinomialTree": {
                    algList.add(new BinomialTree(OPS.theOption, OPS.theOption.getBTnti()));
                    break;
                }
                case "BlackScholesModel": {
                    algList.add(new BlackScholesModel());
                    break;
                }
                case "FiniteDifference": {
                    algList.add(new FiniteDifference(OPS.theOption.getFDnti(), OPS.theOption.getFDnpi(), OPS.theOption.getFDsmax()));
                    break;
                }
                case "SimulationModel": {
                    algList.add(new SimulationModel(OPS.theOption.getSnti(),OPS.theOption.getSnt()));
                    break;
                }
            }
        }
        for(Algorithm al:algList){
            System.out.println(al.getClass().getName());
            resultSet.add(al.computeOption(OPS.theOption));
        }
        for(double[] r: resultSet){
            for(double p:r){
                System.out.println(p);
            }
        }
    }
}
