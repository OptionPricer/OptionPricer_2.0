package view;

import controller.OPS;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import model.volatilityGraph;
import static controller.OPS.resultSet;
import static controller.OPS.algNames;

import org.jfree.ui.RefineryUtilities;

/**
 * This class is used to show both the resultSet and diagrams
 * 
 * @author Castiel
 * @since 2015.03.07
 * @version 1.1.0
 */
public class ResultPanel extends JPanel implements ActionListener{
    public ResultPanel(JFrame jf) {
        mainframe = jf;
        temprp = this;
        
        promptLabel = new JLabel();
        resultTable = new JTable();
        restartButton = new JButton();
        showGraphButton = new JButton();
        
        promptLabel.setText("The result is shown below:");
        
        resultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Method", "S0", "K", "T", "r", "σ", "Price"
            }
        ));

        for (int i=0;i<OPS.algList.size();i++) {
            resultTable.setValueAt(OPS.algNames.get(i),i,0);
            resultTable.setValueAt(OPS.theOption.getsNought(),i,1);
            resultTable.setValueAt(OPS.theOption.getStrikeP(),i,2);
            resultTable.setValueAt(OPS.theOption.getTerm(),i,3);
            resultTable.setValueAt(OPS.theOption.getRiskFreeRate(),i,4);

            resultTable.setValueAt(OPS.theOption.getVolatility(), i, 5);
            System.out.println("-----------r:" + OPS.theOption.getVolatility());
            resultTable.setValueAt(OPS.resultSet.get(i)[5], i, 6);
        }

        restartButton.setText("Restart");
        showGraphButton.setText("Show Graph");
        
        //to the left, 0 and 0 gap
        this.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 0;
        con.gridy = 0;
        this.add(promptLabel, con);
        
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 0;
        con.gridy = 1;
        con.gridwidth = 3;
        this.add(resultTable, con);
        
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 1;
        con.gridy = 5;
        con.gridwidth = 1;        
        this.add(restartButton, con);
        
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 1;
        con.gridy = 8;
        con.gridwidth = 1;
        this.add(showGraphButton, con);
        
        restartButton.setActionCommand("RESTART");
        restartButton.addActionListener(this);
        showGraphButton.setActionCommand("SHOW");
        showGraphButton.addActionListener(this);
                
//        con.fill = GridBagConstraints.HORIZONTAL;
//        con.ipady = 0;       //reset to default
//        con.weighty = 1.0;   //request any extra vertical space
//        con.anchor = GridBagConstraints.PAGE_END; //bottom of space
//        con.insets = new Insets(10,0,0,0);  //top padding
//        con.gridx = 1;       //aligned with button 2
//        con.gridwidth = 1;   //2 columns wide
//        con.gridy = 2;       //third row        
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("SHOW")){
            int i,j;
            double[] v = new double[11];//store the 11 values for the volatility
            double[] op = new double[11];//store the 11 values responded to 11 different volatilities
            
            double vola = OPS.theOption.getVolatility();//get the volatility of the present option
            for(i=0;i<=5;i++) //calculate the 11 volatilities
                v[i] = vola - vola*0.5/5*(5-i);
            for(i=6;i<11;i++)
                v[i] = vola + vola*0.5/5*(i-5);
            
            int numOfAlgorithm = algNames.size();//get the number of algorithm the user selected.
            double[][] optionPrice = new double[numOfAlgorithm][11];
            for(i=0;i<numOfAlgorithm;i++){
                for(j=0;j<11;j++){
                    optionPrice[i][j]= resultSet.get(i)[j];
                }
            }
            //algNames.toArray();
            
            JFrame.setDefaultLookAndFeelDecorated(true); 
            volatilityGraph graph = new volatilityGraph("Option Pricer Software",numOfAlgorithm,v,optionPrice);              
            graph.pack();  
            RefineryUtilities.centerFrameOnScreen(graph);  
            graph.setVisible(true); 
        }
        else if(e.getActionCommand().equals("RESTART")){
            new MainFrame();
            mainframe.dispose();
        }
    }
    
    private JFrame mainframe;
    private ResultPanel temprp;   //used for the button actionperformed
    private javax.swing.JLabel promptLabel;
    private javax.swing.JTable resultTable;
    private javax.swing.JButton restartButton;
    private javax.swing.JButton showGraphButton;

    
    
}
