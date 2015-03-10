package view;

import controller.OPS;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.OptionRight;
import model.OptionStyle;

/**
 * This panel is to show which style and right of the option user wants to calculate
 * 
 * @author Wangyu Huang(Castiel)
 * @since 2015.03.03
 * @version 1.2.0
 */
public class OptionPanel extends JPanel implements ActionListener{
    /**
     * Constructor
     * 
     * @param jf    the mainframe
     */
    public OptionPanel(JFrame jf){
        mainframe = jf;         //store the information of mainframe
        selectPanel = new javax.swing.JPanel();
        backgroundPanel = new javax.swing.JPanel();
        infoLabel = new javax.swing.JLabel();
        EuropeanRadioButton = new javax.swing.JRadioButton();
        AmericanRadioButton = new javax.swing.JRadioButton();
        AsianRadioButton = new javax.swing.JRadioButton();
        callButton = new javax.swing.JButton();
        putButton = new javax.swing.JButton();
       
        selectPanel.setBackground(new java.awt.Color(150, 0, 0));
        backgroundPanel.setBackground(Color.white);

        infoLabel.setText("Please choose an option you want:");
        infoLabel.setForeground(Color.WHITE);
        EuropeanRadioButton.setText("European");
        AmericanRadioButton.setText("American");
        AsianRadioButton.setText("Asian");
        ButtonGroup group = new ButtonGroup();
        group.add(EuropeanRadioButton);
        group.add(AmericanRadioButton);
        group.add(AsianRadioButton);        
        callButton.setText("CALL");
        putButton.setText("PUT");

        javax.swing.GroupLayout selectPanelLayout = new javax.swing.GroupLayout(selectPanel);
        selectPanel.setLayout(selectPanelLayout);
        //selectPanel.setSize(500,500);
        selectPanelLayout.setHorizontalGroup(
            selectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, selectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, true)
                    .addGroup(selectPanelLayout.createSequentialGroup()
                        .addGroup(selectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AsianRadioButton)
                            .addComponent(AmericanRadioButton)
                            .addComponent(EuropeanRadioButton)
                            .addComponent(infoLabel))
                        .addGap(150, 150, 150))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, selectPanelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(callButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(putButton)
                        .addGap(80, 80, 80))))
        );

        selectPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {callButton, putButton});

        selectPanelLayout.setVerticalGroup(
            selectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, selectPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(infoLabel)
                .addGap(20, 20, 20)
                .addComponent(EuropeanRadioButton)
                .addGap(20, 20, 20)
                .addComponent(AmericanRadioButton)
                .addGap(20, 20, 20)
                .addComponent(AsianRadioButton)
                .addGap(45, 45, 45)
                .addGroup(selectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(callButton)
                    .addComponent(putButton))
                .addGap(60, 60, 60))
        );
               
        javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        // Modifier: for the gap between selectPanel and backgroundPanel 
        backgroundPanelLayout.setHorizontalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundPanelLayout.createSequentialGroup()
                .addContainerGap(100, 300)
                .addComponent(selectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
        );
        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
               
        add(selectPanel, "center");
        add(backgroundPanel, "center");
        
//        EuropeanRadioButton.setMnemonic(KeyEvent.VK_P);
        EuropeanRadioButton.setActionCommand(erb);
        AmericanRadioButton.setActionCommand(amrb);
        AsianRadioButton.setActionCommand(asrb);
        callButton.setActionCommand(cb);
        putButton.setActionCommand(pb);
      
        EuropeanRadioButton.addActionListener(this);
        AmericanRadioButton.addActionListener(this);
        AsianRadioButton.addActionListener(this);
        callButton.addActionListener(this);
        putButton.addActionListener(this);   
        
        //set default
        EuropeanRadioButton.setSelected(true);
    }
      
    @Override
    /**
     * action will be performed after user press "PUT" or "CALL"
     */
    public void actionPerformed(ActionEvent e){    
        OptionStyle style = null;
        OptionRight right = null;   
        
        //get the button user select
        if(EuropeanRadioButton.isSelected()){
            style = OptionStyle.EUROPEAN;
        }
        else if(AmericanRadioButton.isSelected()){
            style = OptionStyle.AMERICAN;
        }
        else if(AsianRadioButton.isSelected()){
            style = OptionStyle.ASIAN;
        }
        
        //creat a new frame and drop the old one once "PUT" or "CALL" is pressed
        if(e.getActionCommand().equals("PUT")){
            right = OptionRight.PUT;   
            OPS.createOption(right, style);
            OPS.initAlgs();
            new MainFrame("CONTROL");   
            mainframe.dispose();
        }
        else if(e.getActionCommand().equals("CALL")){
            right = OptionRight.CALL; 
            OPS.createOption(right, style);
            OPS.initAlgs();
            new MainFrame("CONTROL");   
            mainframe.dispose();
        }        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JFrame mainframe;    
    private String erb = "European Option: ";
    private String amrb = "American Option: ";
    private String asrb = "Asian Option: ";
    private String cb = "CALL";
    private String pb = "PUT";
    private javax.swing.JRadioButton AmericanRadioButton;
    private javax.swing.JRadioButton AsianRadioButton;
    private javax.swing.JRadioButton EuropeanRadioButton;
    private javax.swing.JButton callButton;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JButton putButton;
    public javax.swing.JPanel selectPanel;
    private javax.swing.JPanel backgroundPanel;
    // End of variables declaration//GEN-END:variables
}
