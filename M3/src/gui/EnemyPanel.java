package gui;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import ships.MilitaryUnit;
import utils.VariablesWindow;

public class EnemyPanel extends JPanel implements VariablesWindow {
	
	private final int INIT_POS_X = 1500;
	private final int POS_Y = 0;
	private int posX;
	private int approaching_speed;
	
	private ImagePanel planetPanel, armyPanel;
	private ArrayList<MilitaryUnit>[] enemyArmy;
	private ImagePanel[] armyPanels;
	private int colorArmy;
	
	public EnemyPanel () {
		approaching_speed = (INIT_POS_X - (int)(FRAME_WIDTH / 2)) / APPROACH_STEPS;
		posX = INIT_POS_X;
		
		setLayout(null);
		setSize((int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		setOpaque(false);
		setLocation(posX, POS_Y);
		
		armyPanels = new ImagePanel[4];
		
		int num = (int)(1+ Math.random() * 9);
		planetPanel = new ImagePanel(BASE_URL + "planet0" + num + ".png");
		planetPanel.setBounds(getWidth() / 2, 0, (int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		
		this.colorArmy = (int)(1 + Math.random() * 5);
				
		add(planetPanel);
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
	
	public ArrayList<MilitaryUnit>[] getEnemyArmy() {
		return enemyArmy;
	}
	
	public void setEnemyArmy(ArrayList<MilitaryUnit>[] enemyArmy) {
		this.enemyArmy = enemyArmy;
		loadArmy();
	}

	public void enemyComing() {
		posX -= approaching_speed;
		setLocation(posX,POS_Y);
	}
	
	public void loadArmy() {
		
		for (int i = 0; i < enemyArmy.length; i++) {
			
			if (enemyArmy[i].size() > 0) {
				ImagePanel unit = new ImagePanel(BASE_URL + "ships_" + i + "_" + colorArmy + ".png");
				unit.setBounds(ENEMY_SHIPS_POSITIONS[i][0], ENEMY_SHIPS_POSITIONS[i][1], SHIPS_SIZES[i], SHIPS_SIZES[i]);
				unit.rotateImage(INITAL_ENEMY_SHIP_ROTATION);
				armyPanels[i] = unit;	
				add(unit);
			}
		}
		
		repaint();
	}
	
	public void addUnit(int unitType, MilitaryUnit unit) {
		
		if (enemyArmy[unitType].size() == 0) {
			ImagePanel unitPanel = new ImagePanel(BASE_URL + "ships_" + unitType + "_0.png");
			unitPanel.setBounds(ENEMY_SHIPS_POSITIONS[unitType][0], ENEMY_SHIPS_POSITIONS[unitType][1], SHIPS_SIZES[unitType], SHIPS_SIZES[unitType]);
			unitPanel.rotateImage(INITAL_ENEMY_SHIP_ROTATION);
			armyPanels[unitType] = unitPanel;
			add(unitPanel);
			repaint();
		}
		
		enemyArmy[unitType].add(unit);
	}
	
	public void removeUnit(int unitType, MilitaryUnit unit) {
		
		for (int i = 0; i < enemyArmy[unitType].size(); i++) {
			if (enemyArmy[unitType].get(i) == unit) {
				enemyArmy[unitType].remove(i);
				break;
			}
		}
		
		if (enemyArmy[unitType].size() <= 0) {
			remove(armyPanels[unitType]);
			enemyArmy[unitType] = null;
			repaint();
		}
	}
}