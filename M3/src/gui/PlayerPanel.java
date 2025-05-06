package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import utils.VariablesWindow;

public class PlayerPanel extends JPanel implements VariablesWindow {
	
	private ImagePanel planetPanel, armyPanel;
	
	public PlayerPanel(){
		super();
		
		setLayout(null);
		setSize((int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		setBackground(Color.RED);
		setLocation(0,0);
		
		planetPanel = new ImagePanel(BASE_URL + "planet00.png");
		planetPanel.setBounds((int)(-getWidth() / 2), 0, (int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
				
		add(planetPanel);
	}
	
	
	
	
	
	
}
