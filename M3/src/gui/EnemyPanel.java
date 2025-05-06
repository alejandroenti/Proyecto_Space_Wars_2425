package gui;

import java.awt.Color;

import javax.swing.JPanel;

public class EnemyPanel extends JPanel {
	
	private final int INIT_POS_X = 1500;
	private final int POS_Y = 0;
	private int posX;
	private int approaching_speed;
	
	EnemyPanel(int width, int height){
		approaching_speed = (INIT_POS_X - width) / STEPS;
		posX = INIT_POS_X;
		
		setSize(width, height);
		setBackground(Color.YELLOW);
		setLocation(posX,POS_Y);
		
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
	
	
}
