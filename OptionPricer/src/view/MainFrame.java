package view;

import controller.OPS;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * This main frame is the basic container for all other component panel.
 *
 * @author Wangyu Huang(Castiel)
 * @since 2015.03.02
 * @version 1.3.0
 *
 */
public class MainFrame extends JFrame {
    /**
     * Default Constructor, for the option frame, which is the welcome frame
     */
    public MainFrame() {
        initHeadPanel();
        
        Container c = getContentPane();
        c.setLayout(new FlowLayout(1, 10, 10));
        c.add(headPanel);       //the headPanel is on the top        
        OptionPanel op = new OptionPanel(this);
        c.add(op);              //the op is in the bottom     

        this.setVisible(true);
        this.setTitle("Option Pricer");
        this.setBackground(new java.awt.Color(150, 0, 0));
        this.setSize(1000, 720);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * Constructor, for the main function & result frame
     * 
     * @param operation determine which frame to jump
     */
    public MainFrame(String operation) {
        initHeadPanel();        //the headPanel is on the top 
        Container c = getContentPane();
        c.setLayout(new FlowLayout(1, 10, 10));
        c.add(headPanel);      
        OptionInfoPanel oip = new OptionInfoPanel(OPS.theOption.getStyle(), OPS.theOption.getRight());
        c.add(oip);             //the oip is at the top of CONTROLL and RESULT AREA

        final MainFrame tempmf = this;      //to get the mainframe

        //jump to the main function frame
        if (operation.equals("CONTROL")) {
            // the CONTROL AREA is at the bottom      
            JButton backButton = new JButton();
            backButton.setText("");
            //once back button is pressed, the current frame will disappear
            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new MainFrame();
                    tempmf.dispose();
                }
            });
            c.add(backButton);      //the back button is at the left side of CONTROLL and RESULT AREA

            //transfer mainframe's information through 'this'
            ControlPanel cp = new ControlPanel(this);
            c.add(cp);
        } 
          //jump to the result frame
        else if (operation.equals("RESULT")) {
            // the REAULT AREA is at the bottom
            ResultPanel rp = new ResultPanel(this);
            c.add(rp);
        }

        this.setVisible(true);
        this.setTitle("Option Pricer");
        this.setBackground(new java.awt.Color(150, 0, 0));
        this.setSize(1000, 700);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    //initialize the headPanel
    private void initHeadPanel() {
        headPanel = new javax.swing.JPanel();
        headLabel = new javax.swing.JLabel();

        headPanel.setBackground(new java.awt.Color(150, 0, 0));
        headPanel.setMaximumSize(new java.awt.Dimension(1000, 100));
        headPanel.setMinimumSize(new java.awt.Dimension(1000, 100));
        headPanel.setPreferredSize(new java.awt.Dimension(1000, 100));

        headLabel.setBackground(new java.awt.Color(150, 0, 0));
        headLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(IMGPATH)));
        headLabel.setMaximumSize(new java.awt.Dimension(150, 100));
        headLabel.setMinimumSize(new java.awt.Dimension(150, 100));
        headLabel.setPreferredSize(new java.awt.Dimension(150, 100));

        javax.swing.GroupLayout headPanelLayout = new javax.swing.GroupLayout(headPanel);
        headPanel.setLayout(headPanelLayout);
        headPanelLayout.setHorizontalGroup(
                headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(headPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(headLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headPanelLayout.setVerticalGroup(
                headPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(headLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables    
    //the directory of trademark picture
    private final String IMGPATH = "/pictures/cmu_wordmark.jpg";
    private javax.swing.JLabel headLabel;
    private javax.swing.JPanel headPanel;
    // End of variables declaration//GEN-END:variables
}
