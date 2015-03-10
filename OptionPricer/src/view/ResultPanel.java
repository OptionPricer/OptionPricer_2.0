package view;

import controller.OPS;
import static controller.OPS.algNames;
import static controller.OPS.resultSet;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import model.volatilityGraph;
import org.jfree.ui.RefineryUtilities;

/**
 * This class is used to show both the resultSet and diagrams
 * 
 * @author Wangyu Huang(Castiel) & Lijing Liu(Catherine)
 * @since 2015.03.07
 * @version 1.1.0
 */
public class ResultPanel extends JPanel implements ActionListener{
    public ResultPanel(JFrame jf) {
        mainframe = jf;
        
        String[] columnNames = {"Method", "S0", "K", "T", "r", "σ", "Price"};
        Object[][] data = {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},                
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},                
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
            };
        promptLabel = new JLabel();
        resultTable = new JTable(data, columnNames);
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        resultTable.getColumnModel().getColumn(6).setPreferredWidth(100);        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        resultTable.setFillsViewportHeight(true);
        restartButton = new JButton();
        showGraphButton = new JButton();
        
        promptLabel.setText("The result is shown below:");
        
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");        
        
        //set those attributes to the table
        for (int i=0;i<OPS.algList.size()+OPS.customizedAlgNames.size();i++) {
            if(i<OPS.algNames.size()){
                resultTable.setValueAt(OPS.algNames.get(i),i,0);
            }
            else{
                resultTable.setValueAt(OPS.customizedAlgNames.get(i-OPS.algNames.size()), i, 0);
            }
            resultTable.setValueAt(OPS.theOption.getsNought(),i,1);
            resultTable.setValueAt(OPS.theOption.getStrikeP(),i,2);
            resultTable.setValueAt(OPS.theOption.getTerm(),i,3);
            resultTable.setValueAt(OPS.theOption.getRiskFreeRate(),i,4);
            resultTable.setValueAt(OPS.theOption.getVolatility(), i, 5);
            if(i<OPS.algNames.size()){
                resultTable.setValueAt(df.format(OPS.resultSet.get(i)[5]), i, 6);
            }
            else{
                resultTable.setValueAt(df.format(OPS.customizedResults.get(i-OPS.algNames.size())), i, 6);
            }
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
        this.add(scrollPane, con);
        
        con.insets = new Insets(5, 3, 0, 3);
        con.gridx = 0;
        con.gridy = 2;
        con.gridwidth = 0;   
        con.fill = 0;
        con.anchor = java.awt.GridBagConstraints.WEST;        
        this.add(restartButton, con);
        
        con.gridx = 2;
        con.gridy = 2;
        con.gridwidth = 0;
        con.fill = 0;
        this.add(showGraphButton, con);
        
        restartButton.setActionCommand("RESTART");
        restartButton.addActionListener(this);
        showGraphButton.setActionCommand("SHOW");
        showGraphButton.addActionListener(this);                  
    }
    
    
    @Override
    /**
     * Diagrams for every selected algorithms will be shown once "SHOW" button is pressed.
     * Program will restart from the beginning once "RESTART" button is pressed.
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("SHOW")){
            int i,j;
            double[] v = new double[11];//store the 11 values responded to 11 different volatilities
            
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
            
            JFrame.setDefaultLookAndFeelDecorated(true); 
            volatilityGraph graph = new volatilityGraph("Option Pricer Software",numOfAlgorithm,v,optionPrice);              
            graph.pack();  
            RefineryUtilities.centerFrameOnScreen(graph);  
            graph.setVisible(true); 
            graph.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
        else if(e.getActionCommand().equals("RESTART")){
            new MainFrame();
            mainframe.dispose();
        }
    }
    
    private JFrame mainframe;
    private javax.swing.JLabel promptLabel;
    private javax.swing.JTable resultTable;
    private javax.swing.JButton restartButton;
    private javax.swing.JButton showGraphButton;
}
