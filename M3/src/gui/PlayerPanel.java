package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import ships.MilitaryUnit;
import utils.Variables;
import utils.VariablesWindow;

public class PlayerPanel extends JPanel implements VariablesWindow {
	
	private ImagePanel planetPanel;
	private ArrayList<MilitaryUnit>[] playerArmy;
	private ImagePanel[] armyPanels;
	
	public PlayerPanel(){
		super();
		
		setLayout(null);
		setSize((int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		setOpaque(false);
		setLocation(0,0);
		
		this.armyPanels = new ImagePanel[7];
		
		planetPanel = new ImagePanel(BASE_URL + "planet00.png");
		planetPanel.setBounds((int)(-getWidth() / 2), 0, (int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		
		
		add(planetPanel);
	}

	public ArrayList<MilitaryUnit>[] getPlayerAmy() {
		return playerArmy;
	}

	public void setPlayerArmy(ArrayList<MilitaryUnit>[] playerArmy) {
		this.playerArmy = playerArmy;
		loadArmy();
	}
	
	public void loadArmy() {
		
		for (int i = 0; i < playerArmy.length; i++) {
			
			if (playerArmy[i].size() > 0) {
				ImagePanel unit = new ImagePanel(BASE_URL + "ships_" + i + "_0.png");
				unit.setBounds(PLAYER_SHIPS_POSITIONS[i][0], PLAYER_SHIPS_POSITIONS[i][1], SHIPS_SIZES[i], SHIPS_SIZES[i]);
				unit.rotateImage(INITAL_PLAYER_SHIP_ROTATION);
				armyPanels[i] = unit;	
				add(unit);
			}
		}
		
		repaint();
	}
	
	public void addUnit(int unitType, MilitaryUnit unit) {
		
		if (playerArmy[unitType].size() == 0) {
			ImagePanel unitPanel = new ImagePanel(BASE_URL + "ships_" + unitType + "_0.png");
			unitPanel.setBounds(PLAYER_SHIPS_POSITIONS[unitType][0], PLAYER_SHIPS_POSITIONS[unitType][1], SHIPS_SIZES[unitType], SHIPS_SIZES[unitType]);
			unitPanel.rotateImage(INITAL_PLAYER_SHIP_ROTATION);
			armyPanels[unitType] = unitPanel;
			add(unitPanel);
			repaint();
		}
		
		playerArmy[unitType].add(unit);
	}
	
	public void removeUnit(int unitType, MilitaryUnit unit) {
		
		for (int i = 0; i < playerArmy[unitType].size(); i++) {
			if (playerArmy[unitType].get(i) == unit) {
				playerArmy[unitType].remove(i);
				break;
			}
		}
		
		if (playerArmy[unitType].size() <= 0) {
			remove(armyPanels[unitType]);
			armyPanels[unitType] = null;
			repaint();
		}
	}
}
