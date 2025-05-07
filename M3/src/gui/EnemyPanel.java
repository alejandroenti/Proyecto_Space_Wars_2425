package gui;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import utils.VariablesWindow;

public class EnemyPanel extends JPanel implements VariablesWindow {
	
	private final int INIT_POS_X = 1500;
	private final int POS_Y = 0;
	private int posX;
	private int approaching_speed;
	
	private ImagePanel planetPanel, armyPanel;
	private int[] playerArmy;
	private ArrayList<ImagePanel> armyPanels;
	
	EnemyPanel(){
		approaching_speed = (INIT_POS_X - (int)(FRAME_WIDTH / 2)) / APPROACH_STEPS;
		posX = INIT_POS_X;
		
		setLayout(null);
		setSize((int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		setOpaque(false);
		setLocation(posX, POS_Y);
		
		int num = (int)(1+ Math.random() * 9);
		planetPanel = new ImagePanel(BASE_URL + "planet0" + num + ".png");
		planetPanel.setBounds(getWidth() / 2, 0, (int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
				
		add(planetPanel);
		paint();
		
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getApproaching_speed() {
		return approaching_speed;
	}

	public void setApproaching_speed(int approaching_speed) {
		this.approaching_speed = approaching_speed;
	}

	public int getINIT_POS_X() {
		return INIT_POS_X;
	}

	public int getPOS_Y() {
		return POS_Y;
	}

	public void enemyComing() {
		posX -= approaching_speed;
		setLocation(posX,POS_Y);
	}
	
	private void paint() {
		ImagePanel s1 = new ImagePanel(BASE_URL + "ships_0_1.png");
		s1.setBounds(140, 337 - 64 - 50, 64, 64);
		s1.rotateImage(90);
		
		ImagePanel s2 = new ImagePanel(BASE_URL + "ships_1_1.png");
		s2.setBounds(140, 337 + 64 - 50, 64, 64);
		s2.rotateImage(90);
		
		ImagePanel s3 = new ImagePanel(BASE_URL + "ships_2_1.png");
		s3.setBounds(210, 337 - 64 * 3 - 50 * 2, 64, 64);
		s3.rotateImage(90);
		
		ImagePanel s4 = new ImagePanel(BASE_URL + "ships_3_1.png");
		s4.setBounds(210, 337 + 64 * 3 + 10,  64, 64);
		s4.rotateImage(90);
		
		add(s1);
		add(s2);
		add(s3);
		add(s4);
	}
	
}
