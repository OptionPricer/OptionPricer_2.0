package view;

import controller.FileScanner;
import controller.OPS;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.OptionRight;
import model.OptionStyle;

/**
 * This class show the main logic of the algorithms.
 *
 * @author Castiel
 * @since 2015.03.07
 * @version 1.2.0
 */
public class ControlPanel extends JPanel implements ActionListener {
    public ControlPanel(JFrame jf) {
        mainframe = jf;
        tempcp = this;

        algorithmsPanel = new JPanel();
        parametersPanel = new JPanel();
        choicePanel = new JPanel();
           
        initializeCusAlgo();
        //construct the algorithmsPanel by the user's choice
        customizedAlgorithmsPanel();

        //initialize the parametersPanel
        initParametersPanel();

        //intialize the choicePanel
        initChoicePanel();

        //to the left, 0 and 0 gap
        this.setLayout(new FlowLayout(0, 5, 5));
        add(algorithmsPanel, "center");
//        add(scrollPanel, "center");
        add(parametersPanel, "right");      //right is not useful?
        add(choicePanel, "center");

        initialComponents();
        this.setBackground(new java.awt.Color(150, 0, 0));
        this.setMaximumSize(new java.awt.Dimension(600, 450));
        this.setMinimumSize(new java.awt.Dimension(600, 450));
        this.setPreferredSize(new java.awt.Dimension(600, 450));
    }

    public void actionPerformed(ActionEvent e) {
        OPS.showDiagram = false;        //initialization
        //logic for "ADD" button
        if (e.getActionCommand().equals("ADD")) {  
            //set the file name of certain algorithm
            String newFormula;
            newFormula = customizedAlgorithmTextField.getText();
            try{
                OPS.validateFormula(newFormula);
                // Do something (like a popup) to tell user the formula has been added.
            }
            catch (Exception exception)
            {
                String errMsg="INPUT ERROR! "+ exception.getMessage();// such as "** is not number", "Parentheses mismatched"
                System.out.println(errMsg);
                // Do something (like a popup) to display the error message.
            }

//            initializeCusAlgo();
//            OPS.evalExpression(cusAlgoList);
        }                
        
        //logic for "CALCULATE" button
        else if (e.getActionCommand().equals("CALCULATE")) {
            //pop out a dialogue to remind empty errors
            if (sTextField.getText().isEmpty() || kTextField.getText().isEmpty()
                    || tTextField.getText().isEmpty() || rTextField.getText().isEmpty()
                    || oTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(parametersPanel, "some parameter(s) is empty",
                        "lack of parameter", JOptionPane.INFORMATION_MESSAGE);
            }
            //pop out a dialogue to remind non-numeric errors
            else if (!isNumeric(sTextField.getText()) || !isNumeric(kTextField.getText())
                    || !isNumeric(tTextField.getText()) || !isNumeric(rTextField.getText())
                    || !isNumeric(oTextField.getText())) {
                JOptionPane.showMessageDialog(parametersPanel, "some parameter(s) is not a number",
                        "invalid parameter", JOptionPane.INFORMATION_MESSAGE);
            } 
            //pop out a dialogue to remind no algorithm errors
            else if (!bsCheckBox.isSelected() && !btCheckBox.isSelected()
                    && !niCheckBox.isSelected() && !sCheckBox.isSelected()) {
                JOptionPane.showMessageDialog(parametersPanel, "At least choose one algorithm",
                        "lack of algorithm", JOptionPane.INFORMATION_MESSAGE);
            } 
            //once those inputs are in correct format
            else {
                double stf = Double.parseDouble(sTextField.getText());
                double ktf = Double.parseDouble(kTextField.getText());
                double ttf = Double.parseDouble(tTextField.getText());
                double rtf = Double.valueOf(rTextField.getText());
                double otf = Double.valueOf(oTextField.getText()).doubleValue();
                String tempInput;       //temporarily store additional input

                //set attributes in OPS
                OPS.theOption.setsNought(stf);
                OPS.theOption.setStrikeP(ktf);
                OPS.theOption.setTerm(ttf);
                OPS.theOption.setRiskFreeRate(rtf);
                OPS.theOption.setVolatility(otf);

                if (graphCheckBox.isSelected()) {
                    OPS.showDiagram = true;
                }

                OPS.algNames.clear();       //initialization
                if (bsCheckBox.isSelected()) {
                    OPS.algNames.add("BlackScholesModel");
                }
                if (btCheckBox.isSelected()) {
                    OPS.algNames.add("BinomialTree");
                    tempInput = JOptionPane.showInputDialog("Binomial Tree: \n"
                            + "Number of time intervals: ");

                    while (!valueVerify(tempInput, "Integer")) {
                        tempInput = JOptionPane.showInputDialog("Binomial Tree: \n"
                                + "Number of time intervals: ");
                    }
                    int BTnti = Integer.parseInt(tempInput);
                    OPS.theOption.setBTnti(BTnti);
                }
                if (niCheckBox.isSelected()) {
                    OPS.algNames.add("FiniteDifference");
                    tempInput = JOptionPane.showInputDialog("Finite Difference: \n"
                            + "Number of time intervals: ");

                    while (!valueVerify(tempInput, "Integer")) {
                        tempInput = JOptionPane.showInputDialog("Binomial Tree: \n"
                                + "Number of time intervals: ");
                    }
                    int FDnti = Integer.parseInt(tempInput);
                    OPS.theOption.setFDnti(FDnti);
                    tempInput = JOptionPane.showInputDialog("Finite Difference: \n"
                            + "Number of price intervals: ");

                    while (!valueVerify(tempInput, "Integer")) {
                        tempInput = JOptionPane.showInputDialog("Binomial Tree: \n"
                                + "Number of price intervals: ");
                    }
                    int FDnpi = Integer.parseInt(tempInput);
                    OPS.theOption.setFDnpi(FDnpi);
                    tempInput = JOptionPane.showInputDialog("Finite Difference: \n"
                            + "Max limitation stock price: ");

                    while (!valueVerify(tempInput, "Double")) {
                        tempInput = JOptionPane.showInputDialog("Binomial Tree: \n"
                                + "Max limitation stock price: ");
                    }
                    double FDsmax = Double.parseDouble(tempInput);
                    OPS.theOption.setFDsmax(FDsmax);
                }
                if (sCheckBox.isSelected()) {
                    OPS.algNames.add("SimulationModel");
                    tempInput = JOptionPane.showInputDialog("Simulation: \n"
                            + "Number of time intervals: ");

                    while (!valueVerify(tempInput, "Integer")) {
                        tempInput = JOptionPane.showInputDialog("Binomial Tree: \n"
                                + "Number of time intervals: ");
                    }
                    int Snti = Integer.parseInt(tempInput);
                    OPS.theOption.setSnti(Snti);
                    tempInput = JOptionPane.showInputDialog("Simulation: \n"
                            + "Number of trials (recommend 10): ");

                    while (!valueVerify(tempInput, "Integer")) {
                        tempInput = JOptionPane.showInputDialog("Binomial Tree: \n"
                                + "Number of trials: ");
                    }
                    int Snt = Integer.parseInt(tempInput);
                    OPS.theOption.setSnt(Snt);
                }
                
                //the customized algorithms
                OPS.customizedAlgNames = new ArrayList<String>();
                for(int i = 0; i < cusAlgorithms.size(); i++){
                    if(cusAlgorithms.get(i).isSelected()){
                        OPS.customizedAlgNames.add(cusAlgoList.get(i));
                    }
                }

                System.out.print(OPS.theOption.getStyle());
                System.out.print(": ");
                System.out.println(OPS.theOption.getRight());
                System.out.print("s:" + OPS.theOption.getsNought() + ", ");
                System.out.print("k:" + OPS.theOption.getStrikeP() + ", ");
                System.out.print("t:" + OPS.theOption.getTerm() + ", ");
                System.out.print("r:" + OPS.theOption.getRiskFreeRate() + ", ");
                System.out.println("o:" + OPS.theOption.getVolatility());
                System.out.println(OPS.showDiagram + "   " + OPS.algNames);
                System.out.println("Binomial Tree:" + OPS.theOption.getBTnti());
                System.out.println("Finite Difference:" + OPS.theOption.getFDnpi()
                        + ", " + OPS.theOption.getFDnti() + ", " + OPS.theOption.getFDsmax());
                System.out.println("Simulation:" + OPS.theOption.getSnt() + ", "
                        + OPS.theOption.getSnti());

                OPS.compute();
                System.out.println(OPS.results.size());
                new MainFrame("RESULT");
                mainframe.dispose();        //dispose the original frame
            }
        }
    }

    //to test whether a string is a number
    public boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

    //to test whether a string is an Integer
    public boolean isInteger(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //to verify whether the String is number
    public boolean valueVerify(String testString, String numType) {
        if (!testString.isEmpty() && isNumeric(testString)) {
            if (numType.equals("Integer") && isInteger(testString)) {
                return true;
            } else if (numType.equals("Double")) {
                return true;
            } else {
                JOptionPane.showMessageDialog(parametersPanel, "Invalid input",
                        "invalid parameter", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(parametersPanel, "Invalid input",
                    "invalid parameter", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }
    
    //to initialize the customer algorithms
    private void initializeCusAlgo(){  
        //set the file name of certain algorithm
        String fileName = "src/algorithmSaver/" + OPS.theOption.getStyle().toString() 
                + OPS.theOption.getRight().toString() + ".txt";  
        cusAlgoList = new ArrayList<String>();
            
        //read the file 
        try {
            FileScanner.readByLines(fileName, cusAlgoList);
        } catch (IOException ex) {
            Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
        }            
    }
    
    //intialize the AlgorithmsPanel by user's choice
    private void customizedAlgorithmsPanel() {
        algoInfoLabel = new JLabel();
        algoInfoLabel.setText("Check the algorithms(s) you want to use:");
        bsCheckBox = new JCheckBox();
        bsCheckBox.setText("B-S Formula");
        btCheckBox = new JCheckBox();
        btCheckBox.setText("Binomial Tree");
        niCheckBox = new JCheckBox();
        niCheckBox.setText("Numerical Integration");
        sCheckBox = new JCheckBox();
        sCheckBox.setText("Simulation");
        cusInfoLabel = new JLabel();
        cusInfoLabel.setText("Customized algorithms:");

//        algorithmsPanel.setBackground(new java.awt.Color(150,0,0));
        algorithmsPanel.add(algoInfoLabel);
        //add the basic 4 algorithms into algorithmsPanel
        if (OPS.theOption.getStyle() == OptionStyle.AMERICAN) {
            if (OPS.theOption.getRight() == OptionRight.PUT) {
                algorithmsPanel.add(btCheckBox);
                algorithmsPanel.add(sCheckBox);
                algorithmsPanel.add(niCheckBox);
            } else {
                algorithmsPanel.add(bsCheckBox);
                algorithmsPanel.add(btCheckBox);
                algorithmsPanel.add(sCheckBox);
                algorithmsPanel.add(niCheckBox);
            }
        } else if (OPS.theOption.getStyle() == OptionStyle.EUROPEAN) {
            algorithmsPanel.add(bsCheckBox);
            algorithmsPanel.add(btCheckBox);
            algorithmsPanel.add(sCheckBox);
            algorithmsPanel.add(niCheckBox);
        } else if (OPS.theOption.getStyle() == OptionStyle.ASIAN) {
            algorithmsPanel.add(sCheckBox);
        }
        
        algorithmsPanel.add(cusInfoLabel);
        cusAlgorithms = new ArrayList();
        for(int i = 0; i < cusAlgoList.size(); i++){
            cusAlgorithms.add(new JCheckBox()); 
            cusAlgorithms.get(i).setText(cusAlgoList.get(i));
            algorithmsPanel.add(cusAlgorithms.get(i));
        }

        algorithmsPanel.setLayout(new GridLayout(0, 1, 2, 10));
//        algorithmsPanel.scrollRectToVisible(new java.awt.Rectangle(new java.awt.Dimension(300, 200)));        
        algorithmsPanel.setMaximumSize(new java.awt.Dimension(300, 300));
        algorithmsPanel.setMinimumSize(new java.awt.Dimension(300, 300));
        algorithmsPanel.setPreferredSize(new java.awt.Dimension(300, 300));
//        scrollPanel = new JScrollPane(algorithmsPanel);
//        scrollPanel.setBounds(100, 100, 100, 300);
    }

    private void initParametersPanel() {
        paraInfoLabel = new JLabel();
        paraInfoLabel.setText("Parameters:");
        paraInfoLabel.setForeground(Color.WHITE);
        s0Label = new JLabel();
        kLabel = new JLabel();
        tLabel = new JLabel();
        rLabel = new JLabel();
        oLabel = new JLabel();
        s0Label.setText("S0:");
        s0Label.setForeground(Color.WHITE);
        kLabel.setText("K:");
        kLabel.setForeground(Color.WHITE);
        tLabel.setText("T:");
        tLabel.setForeground(Color.WHITE);
        rLabel.setText("r:");
        rLabel.setForeground(Color.WHITE);
        oLabel.setText("o(σ):");
        oLabel.setForeground(Color.WHITE);
        sTextField = new JTextField(10);
        kTextField = new JTextField(10);
        tTextField = new JTextField(10);
        rTextField = new JTextField(10);
        oTextField = new JTextField(10);

        parametersPanel.setBackground(new java.awt.Color(150, 0, 0));
        parametersPanel.setLayout(new GridLayout(6, 2, 2, 10));
        parametersPanel.add(paraInfoLabel);
        parametersPanel.add(new JLabel());  //just used to keep columns aligned     
        parametersPanel.add(s0Label);
        parametersPanel.add(sTextField);
        parametersPanel.add(kLabel);
        parametersPanel.add(kTextField);
        parametersPanel.add(tLabel);
        parametersPanel.add(tTextField);
        parametersPanel.add(rLabel);
        parametersPanel.add(rTextField);
        parametersPanel.add(oLabel);
        parametersPanel.add(oTextField);
    }

    private void initChoicePanel() {
        noteLabel = new JLabel();
        noteLabel.setText("Note:");
        noteLabel.setForeground(Color.WHITE);
        instruction1Label = new JLabel();
        instruction1Label.setText("S: current stock price;   K: strike price;   "
                + "T term of the option in years;   ");
        instruction1Label.setForeground(Color.WHITE);
        instruction2Label = new JLabel();
        instruction2Label.setText("r: risk-free interest rate;   o(σ): "
                + "volatility of the stock price,");
        instruction2Label.setForeground(Color.WHITE);
        customizedAlgorithmTextField = new JTextField();
        customizedAlgorithmTextField.setText("[Enter alternate fomula. e.g: S+K+T-r*o]");
        addAlgorithmButton = new JButton();
        addAlgorithmButton.setText("ADD");
        graphCheckBox = new JCheckBox();
        graphCheckBox.setText("Show graph");
        calculateButton = new JButton();
        calculateButton.setText("CALCULATE");

        choicePanel.setBackground(new java.awt.Color(150, 0, 0));
        choicePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        choicePanel.add(noteLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        choicePanel.add(instruction1Label, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        choicePanel.add(instruction2Label, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 35);     //keep the right distance
        c.gridx = 0;
        c.gridy = 3;
        choicePanel.add(customizedAlgorithmTextField, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 40;
        c.gridx = 1;
        c.gridy = 3;
        c.fill = 0;         //not resize
        choicePanel.add(addAlgorithmButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(20, 0, 0, 0);     //keep the top distance
        c.ipadx = 10;
        c.gridx = 0;
        c.gridy = 4;
        c.fill = 0;
        c.anchor = java.awt.GridBagConstraints.WEST;
        choicePanel.add(graphCheckBox, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        c.fill = 0;
        choicePanel.add(calculateButton, c);
    }

    //initialize the "ADD" & "CALCULATE" button
    private void initialComponents() {
        addAlgorithmButton.setActionCommand("ADD");
        calculateButton.setActionCommand("CALCULATE");
        addAlgorithmButton.addActionListener(tempcp);
        calculateButton.addActionListener(tempcp);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JFrame mainframe;
    private ControlPanel tempcp;   //used for the button actionperformed    
    private String bsf = "B-S Formula";
    private String bt = "Binomial Tree";
    private String ni = "Numerical Integration";
    private String si = "Simulation";
    private ArrayList<String> cusAlgoList;     //the algorithms customer adds
    private ArrayList<JCheckBox> cusAlgorithms;     //the algorithms customer adds

//    private javax.swing.JScrollPane algorithmsPanel;
    private javax.swing.JPanel algorithmsPanel;
    private javax.swing.JPanel parametersPanel;  //parameters
    private javax.swing.JPanel choicePanel;     //add, graph and calculate

//    private javax.swing.JButton backButton;
    private javax.swing.JTextField customizedAlgorithmTextField;
    private javax.swing.JButton addAlgorithmButton;
    private javax.swing.JCheckBox graphCheckBox;
    private javax.swing.JButton calculateButton;

    private javax.swing.JLabel algoInfoLabel;
    private javax.swing.JCheckBox bsCheckBox;
    private javax.swing.JCheckBox btCheckBox;
    private javax.swing.JCheckBox niCheckBox;
    private javax.swing.JCheckBox sCheckBox;

    private javax.swing.JLabel s0Label;
    private javax.swing.JTextField sTextField;
    private javax.swing.JLabel kLabel;
    private javax.swing.JTextField kTextField;
    private javax.swing.JLabel tLabel;
    private javax.swing.JTextField tTextField;
    private javax.swing.JLabel rLabel;
    private javax.swing.JTextField rTextField;
    private javax.swing.JLabel oLabel;
    private javax.swing.JTextField oTextField;

    private javax.swing.JLabel cusInfoLabel;

    private javax.swing.JLabel paraInfoLabel;

    private javax.swing.JLabel noteLabel;
    private javax.swing.JLabel instruction1Label;
    private javax.swing.JLabel instruction2Label;
    // End of variables declaration//GEN-END:variables
}
