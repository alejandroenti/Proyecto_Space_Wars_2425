package gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonsPanel extends JPanel{
	JButton buy, update, reports;
	
	ButtonsPanel(){
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setLocation(0,0);
		setBackground(Color.BLUE);
		
		buy = new JButton("a");
		update = new JButton("b");
		reports = new JButton("c");
		
		add(buy);
		add(update);
		add(reports);
	}
}
