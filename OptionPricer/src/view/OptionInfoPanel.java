package view;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.OptionRight;
import model.OptionStyle;

/**
 * This class is used to show the information of which option and what the type
 * of the option for the user.
 * 
 * @author Wangyu Huang(Castiel)
 * @since 2015.03.07
 * @version 1.1.0
 */
public class OptionInfoPanel extends JPanel{
    private javax.swing.JLabel optionLabel;
    
    public OptionInfoPanel(){
        this.setBackground(new java.awt.Color(200, 0, 0));
        this.setMaximumSize(new java.awt.Dimension(1000, 50));
        this.setMinimumSize(new java.awt.Dimension(1000, 50));
        this.setPreferredSize(new java.awt.Dimension(1000, 50));
        
        optionLabel = new JLabel();
        optionLabel.setText("Text Option");
        this.add(optionLabel);
    }
    
    public OptionInfoPanel(OptionStyle style, OptionRight right){        
        this.setBackground(new java.awt.Color(200, 0, 0));
        this.setMaximumSize(new java.awt.Dimension(1000, 50));
        this.setMinimumSize(new java.awt.Dimension(1000, 50));
        this.setPreferredSize(new java.awt.Dimension(1000, 50));
        
        optionLabel = new JLabel();
        String info = style + ": " + right;
        optionLabel.setText(info);
        optionLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        optionLabel.setVerticalAlignment(java.awt.Label.CENTER);
        optionLabel.setForeground(Color.BLACK);
        this.add(optionLabel);
    }
}
