package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import utils.VariablesWindow;

public class PlayerPanel extends JPanel implements VariablesWindow {
	
	private ImagePanel planetPanel, armyPanel;
	private int[] playerAmy;
	private ArrayList<ImagePanel> armyPanels;
	
	public PlayerPanel(){
		super();
		
		setLayout(null);
		setSize((int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		setOpaque(false);
		setLocation(0,0);
		
		planetPanel = new ImagePanel(BASE_URL + "planet00.png");
		planetPanel.setBounds((int)(-getWidth() / 2), 0, (int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		
		add(planetPanel);
	}

	public int[] getPlayerAmy() {
		return playerAmy;
	}

	public void setPlayerAmy(int[] playerAmy) {
		this.playerAmy = playerAmy;
		loadArmy();
	}
	
	public void loadArmy() {
		
		armyPanels.clear();
		
		for (int i = 0; i < playerAmy.length; i++) {
			int spawn = playerAmy[i] / 10;
			for (int j = 0; j < spawn; j++) {
				int num = (int)(1 + Math.random() * 9);
				//armyPanel.add(new ImagePanel(BASE_URL + "ships_" + i + "_" + num + ".png"));
				armyPanel.add(new ImagePanel(BASE_URL + "planet_0" + i + ".png"));
			}
		}
		
		for (ImagePanel panel : armyPanels) {
			panel.setBounds((int)(300 + Math.random() * 300), (int)(Math.random() * 675), 64, 64);
			add(panel);
		}
		
	}
	
	
	
	
}
