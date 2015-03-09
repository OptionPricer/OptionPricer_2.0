package view;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import model.Option;

/**
 * This class is used to show both the results and diagrams
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
                {"Simulation", "50.00", "50.00", "5", "0.10", "0.40", "0.91"},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Method", "S0", "K", "T", "r", "σ", "Price"
            }
        ));
        
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
