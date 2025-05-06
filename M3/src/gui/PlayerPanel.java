package gui;

import java.awt.Color;

import javax.swing.JPanel;

import utils.VariablesWindow;

public class PlayerPanel extends JPanel implements VariablesWindow {
	
	public PlayerPanel(){
		super();
		
		setSize((int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		setBackground(Color.RED);
		setLocation(0,0);
	}
}
