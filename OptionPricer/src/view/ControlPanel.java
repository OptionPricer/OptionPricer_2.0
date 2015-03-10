package view;

import controller.FileHandler;
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
 * @author Wangyu Huang(Castiel)
 * @since 2015.03.07
 * @version 1.5.5
 */
public class ControlPanel extends JPanel implements ActionListener {

    /**
     * Constructor
     *
     * @param jf the mainframe
     */
    public ControlPanel(JFrame jf) {
        //set the file name pertain to certain algorithm
        fileName = "src/algorithmSaver/" + OPS.theOption.getStyle().toString()
                + OPS.theOption.getRight().toString() + ".txt";
        mainframe = jf;
        tempcp = this;      //this ControlPanel

        algorithmsPanel = new JPanel();
        parametersPanel = new JPanel();
        choicePanel = new JPanel();

        initializeCusAlgo();        //initialize the customer algorithms                
        customizedAlgorithmsPanel();    //construct the algorithmsPanel by the user's choice      
        initParametersPanel();      //initialize the parametersPanel       
        initChoicePanel();          //intialize the choicePanel 
        initButtons();              //initialize the "ADD" & "CALCULATE" button

        //to the left, 0 and 0 gap        
        this.add(algorithmsPanel);
        this.add(parametersPanel);
        this.add(choicePanel);
        this.setLayout(new FlowLayout(0, 5, 5));
        this.setBackground(new java.awt.Color(150, 0, 0));
        this.setMaximumSize(new java.awt.Dimension(600, 460));
        this.setMinimumSize(new java.awt.Dimension(600, 460));
        this.setPreferredSize(new java.awt.Dimension(600, 460));
    }

    @Override
    /**
     * Customer's inputting formula will be checked, update into certain .txt
     * file if it's valid and then refresh the panel once "ADD" button is
     * pressed. Verify all the parameters and algorithms once "CALCULATE" button
     * is pressed, then return result panel
     */
    public void actionPerformed(ActionEvent e) {
        //logic for "ADD" button
        if (e.getActionCommand().equals("ADD")) {
            //set the file name of certain algorithm
            String newFormula;
            newFormula = customizedAlgorithmTextField.getText();

            //check whether the new formula is valid or not, 
            //if valid, input to the corresponding file and refresh the frame; error message pop out otherwise            
            if (OPS.isValidateFormula(newFormula)) {
                try {
                    FileHandler.addNewFormula(fileName, newFormula);
                    //refresh the frame                    
                    new MainFrame("CONTROL");
                    JOptionPane.showMessageDialog(parametersPanel, "Successfully add");
                    mainframe.dispose();        //dispose the original frame

                } catch (IOException ex) {
                    Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(parametersPanel, "Fail to add. Invalid formula");
            }
        } //logic for "CALCULATE" button
        else if (e.getActionCommand().equals("CALCULATE")) {
            boolean isCusAlgorithm = false;
            
            //check whether the use select the customized algorithms
            for (int i = 0; i < cusAlgoList.size(); i++) {
                if (cusAlgorithms.get(i).isSelected()) {
                    isCusAlgorithm = true;
                }
            }

            //pop out a dialogue to remind empty errors
            if (checkFieldEmpty()) {
                JOptionPane.showMessageDialog(parametersPanel, "Empty parameter(s)",
                        "lack of parameter", JOptionPane.INFORMATION_MESSAGE);
            } //pop out a dialogue to remind non-numeric errors
            else if (checkFieldNumeric()) {
                JOptionPane.showMessageDialog(parametersPanel, "Invalid parameter(s)",
                        "invalid parameter", JOptionPane.INFORMATION_MESSAGE);
            } //pop out a dialogue to remind no algorithm errors
            else if (checkAlgorithmsSelected(isCusAlgorithm)) {
                JOptionPane.showMessageDialog(parametersPanel, "Choose at least one algorithm",
                        "lack of algorithm", JOptionPane.INFORMATION_MESSAGE);
            } //once those inputs are in correct format
            else {
                setUpOPSWithBasicAlgo();

                //the customized algorithms
                OPS.customizedAlgNames = new ArrayList<String>();
                for (int i = 0; i < cusAlgorithms.size(); i++) {
                    System.out.println("Customized Algorithm:" + i);
                    if (cusAlgorithms.get(i).isSelected()) {
                        System.out.println(cusAlgoList.get(i));
                        OPS.customizedAlgNames.add(cusAlgoList.get(i));
                    }
                }

                OPS.compute();      //compute the results for selected algorithms
                new MainFrame("RESULT");
                mainframe.dispose();        //dispose the original frame
            }
        }
    }

    /**
     * to check whether a string is a number
     *
     * @param str String to check
     * @return true if the String is a number, false otherwise
     */
    public boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

    /**
     * to check whether a String is an Integer
     *
     * @param str String to check
     * @return true if the String is an Integer, false otherwise
     */
    public boolean isInteger(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * to check whether a String is in valid value
     *
     * @param str String to check
     * @param numType the number type that the String used to check
     * @return true if the String is valid, false otherwise
     */
    public boolean isValueValid(String str, String numType) {
        if (!str.isEmpty() && isNumeric(str)) {
            if (numType.equals("Integer") && isInteger(str)) {
                if (!str.equals("0")) {
                    return true;
                } else {
                    return false;
                }
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

    //initialize the customer algorithms by reading the exact .txt file
    private void initializeCusAlgo() {
        cusAlgoList = new ArrayList<String>();
        //read the file 
        try {
            FileHandler.readByLines(fileName, cusAlgoList);
        } catch (IOException ex) {
            Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //initialize the algorithmsPanel by user's choice
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
        for (int i = 0; i < cusAlgoList.size(); i++) {
            cusAlgorithms.add(new JCheckBox());
            cusAlgorithms.get(i).setText(cusAlgoList.get(i));
            algorithmsPanel.add(cusAlgorithms.get(i));
        }

        algorithmsPanel.setLayout(new GridLayout(0, 1, 2, 10));
        algorithmsPanel.setMaximumSize(new java.awt.Dimension(300, 300));
        algorithmsPanel.setMinimumSize(new java.awt.Dimension(300, 300));
        algorithmsPanel.setPreferredSize(new java.awt.Dimension(300, 300));
    }

    //initialize the parametersPanel
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

    //initialize the choicePanel
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
                + "volatility of the stock price");
        instruction2Label.setForeground(Color.WHITE);
        instruction3Label = new JLabel();
        instruction3Label.setText("Only s, k, t, r and o can be the parameters.");
        instruction3Label.setForeground(Color.WHITE);
        customizedAlgorithmTextField = new JTextField();
        customizedAlgorithmTextField.setText("[Enter alternate fomula. e.g: S+K+T-r*o]");
        addAlgorithmButton = new JButton();
        addAlgorithmButton.setText("ADD");
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
        c.gridx = 0;
        c.gridy = 3;
        choicePanel.add(instruction3Label, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 35);     //keep the right distance
        c.gridx = 0;
        c.gridy = 4;
        choicePanel.add(customizedAlgorithmTextField, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 0, 0);     //keep the top distance
        c.gridwidth = 40;
        c.gridx = 1;
        c.gridy = 4;
        c.fill = 0;         //not resize
        choicePanel.add(addAlgorithmButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(15, 0, 0, 0);     //keep the top distance
        c.gridx = 0;
        c.gridy = 5;
        c.fill = 0;             //do not resize
        c.anchor = java.awt.GridBagConstraints.CENTER;
        choicePanel.add(calculateButton, c);
    }

    //initialize the "ADD" & "CALCULATE" button
    private void initButtons() {
        addAlgorithmButton.setActionCommand("ADD");
        calculateButton.setActionCommand("CALCULATE");
        addAlgorithmButton.addActionListener(tempcp);
        calculateButton.addActionListener(tempcp);
    }
    
    //check whether there is empty textField
    private boolean checkFieldEmpty() {
        if (sTextField.getText().isEmpty() || kTextField.getText().isEmpty()
                || tTextField.getText().isEmpty() || rTextField.getText().isEmpty()
                || oTextField.getText().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    //check whether the content in the textField is number
    private boolean checkFieldNumeric() {
        if (!isNumeric(sTextField.getText()) || !isNumeric(kTextField.getText())
                || !isNumeric(tTextField.getText()) || !isNumeric(rTextField.getText())
                || !isNumeric(oTextField.getText())) {
            return true;
        } else {
            return false;
        }
    }

    //check whether user select a algorithm
    private boolean checkAlgorithmsSelected(boolean isCusAlgorithm) {
        if (!bsCheckBox.isSelected() && !btCheckBox.isSelected()
                && !niCheckBox.isSelected() && !sCheckBox.isSelected() && !isCusAlgorithm) {
            return true;
        } else {
            return false;
        }
    }
    
    //set those basic attributes OPS has, including those user choosing basic algorithms 
    private void setUpOPSWithBasicAlgo() {
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

        OPS.algNames.clear();       //initialization
        //check which basic algorithm is selected, further parameters should be inputted if needed
        if (bsCheckBox.isSelected()) {
            OPS.algNames.add("BlackScholesModel");
        }
        //one more parameter is needed for Binomial Tree
        if (btCheckBox.isSelected()) {
            OPS.algNames.add("BinomialTree");
            tempInput = JOptionPane.showInputDialog("Binomial Tree: \n"
                    + "Number of time intervals: ");
            //continue inputting until the valid input is gotten
            while (!isValueValid(tempInput, "Integer")) {
                tempInput = JOptionPane.showInputDialog("Invalid input, please input again:\n"
                        + "Binomial Tree: Number of time intervals: ");
            }
            int BTnti = Integer.parseInt(tempInput);
            OPS.theOption.setBTnti(BTnti);
        }
        //three more parameters are needed for Finite Difference (Numerical Integration)
        if (niCheckBox.isSelected()) {
            OPS.algNames.add("FiniteDifference");
            tempInput = JOptionPane.showInputDialog("Numerical Integration: \n"
                    + "Number of time intervals: ");
            //continue inputting until the valid input is gotten
            while (!isValueValid(tempInput, "Integer")) {
                tempInput = JOptionPane.showInputDialog("Invalid input, please input again:\n"
                        + "Numerical Integration: Number of time intervals: ");
            }
            int FDnti = Integer.parseInt(tempInput);
            OPS.theOption.setFDnti(FDnti);
            tempInput = JOptionPane.showInputDialog("Numerical Integration: \n"
                    + "Number of price intervals: ");
            //continue inputting until the valid input is gotten
            while (!isValueValid(tempInput, "Integer")) {
                tempInput = JOptionPane.showInputDialog("Invalid input, please input again:\n"
                        + "Numerical Integration: Number of price intervals: ");
            }
            int FDnpi = Integer.parseInt(tempInput);
            OPS.theOption.setFDnpi(FDnpi);
            tempInput = JOptionPane.showInputDialog("Numerical Integration: \n"
                    + "Max limitation stock price: ");
            //continue inputting until the valid input is gotten
            while (!isValueValid(tempInput, "Double")) {
                tempInput = JOptionPane.showInputDialog("Invalid input, please input again:\n"
                        + "Numerical Integration: Max limitation stock price: ");
            }
            double FDsmax = Double.parseDouble(tempInput);
            OPS.theOption.setFDsmax(FDsmax);
        }
        //two more parameters are needed for Finite Difference (Numerical Integration)
        if (sCheckBox.isSelected()) {
            OPS.algNames.add("SimulationModel");
            tempInput = JOptionPane.showInputDialog("Simulation: \n"
                    + "Number of time intervals: ");
            //continue inputting until the valid input is gotten
            while (!isValueValid(tempInput, "Integer")) {
                tempInput = JOptionPane.showInputDialog("Invalid input, please input again:\n"
                        + "Simulation: Number of time intervals: ");
            }
            int Snti = Integer.parseInt(tempInput);
            OPS.theOption.setSnti(Snti);
            tempInput = JOptionPane.showInputDialog("Simulation: \n"
                    + "Number of trials (recommend 10,000) "
                    + "Trials exceeding 10,000 will increase computational time. ");
            //continue inputting until the valid input is gotten
            while (!isValueValid(tempInput, "Integer")) {
                tempInput = JOptionPane.showInputDialog("Invalid input, please input again:\n"
                        + "Simulation: Number of trials: ");
            }
            int Snt = Integer.parseInt(tempInput);
            OPS.theOption.setSnt(Snt);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private String fileName;
    private JFrame mainframe;
    private ControlPanel tempcp;   //used for the button actionperformed    
    private ArrayList<String> cusAlgoList;     //the algorithms customer adds
    private ArrayList<JCheckBox> cusAlgorithms;     //the algorithms customer adds

    private javax.swing.JPanel algorithmsPanel;
    private javax.swing.JPanel parametersPanel;  //parameters
    private javax.swing.JPanel choicePanel;     //add, graph and calculate

    private javax.swing.JTextField customizedAlgorithmTextField;
    private javax.swing.JButton addAlgorithmButton;
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
    private javax.swing.JLabel instruction3Label;
    // End of variables declaration//GEN-END:variables
}
