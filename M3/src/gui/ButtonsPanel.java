package gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import utils.VariablesWindow;

public class ButtonsPanel extends JPanel implements VariablesWindow{
	JButton buy, update, reports;
	
	ButtonsPanel(){
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setBackground(Color.BLUE);
		setSize(200,100);
		setLocation((int)(FRAME_WIDTH/2)-20,FRAME_HEIGHT-200);
		
		
		buy = new JButton("a");
		update = new JButton("b");
		reports = new JButton("c");
		
		add(buy);
		add(update);
		add(reports);
	}
}
