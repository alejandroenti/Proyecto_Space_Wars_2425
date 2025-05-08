package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import utils.VariablesWindow;

public class PlayerPanel extends JPanel implements VariablesWindow {
	
	private ImagePanel planetPanel, armyPanel;
	private int[] playerArmy;
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
		paint();
	}

	public int[] getPlayerAmy() {
		return playerArmy;
	}

	public void setPlayerArmy(int[] playerArmy) {
		this.playerArmy = playerArmy;
		loadArmy();
	}
	
	public void loadArmy() {
		
		armyPanels.clear();
		
		for (int i = 0; i < playerArmy.length; i++) {
			int spawn = playerArmy[i] / 10;
			spawn += playerArmy[i] % 10;
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
	
	
	private void paint() {
		ImagePanel s1 = new ImagePanel(BASE_URL + "ships_0_0.png");
		s1.setBounds(400, 337 - 64 - 50, 64, 64);
		s1.rotateImage(270);
		
		ImagePanel s2 = new ImagePanel(BASE_URL + "ships_1_0.png");
		s2.setBounds(400, 337 + 64 - 50, 64, 64);
		s2.rotateImage(270);
		
		ImagePanel s3 = new ImagePanel(BASE_URL + "ships_2_0.png");
		s3.setBounds(330, 337 - 64 * 3 - 50 * 2, 64, 64);
		s3.rotateImage(270);
		
		ImagePanel s4 = new ImagePanel(BASE_URL + "ships_3_0.png");
		s4.setBounds(330, 337 + 64 * 3 + 10,  64, 64);
		s4.rotateImage(270);
		
		ImagePanel s5 = new ImagePanel(BASE_URL + "missilelauncher.png");
		s5.setBounds(165, 100,  96, 96);
		s5.rotateImage(270);
		
		ImagePanel s6 = new ImagePanel(BASE_URL + "ioncannon.png");
		s6.setBounds(220, 337 - 80,  128, 128);
		s6.rotateImage(270);
		
		ImagePanel s7 = new ImagePanel(BASE_URL + "plasmacannon.png");
		s7.setBounds(180, 337 + 80,  128, 128);
		s7.rotateImage(270);
		
		
		add(s1);
		add(s2);
		add(s3);
		add(s4);
		add(s5);
		add(s6);
		add(s7);
	}
	
}
