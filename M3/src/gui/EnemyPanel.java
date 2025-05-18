package gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import events.MouseButtonsListener;
import ships.MilitaryUnit;
import utils.Printing;
import utils.Variables;
import utils.VariablesWindow;

public class EnemyPanel extends JPanel implements Variables, VariablesWindow {
	
	private final int INIT_POS_X = 1500;
	private final int POS_Y = 0;
	private final int MAX_LINE_SIZE = 64;
	private final int MAX_NUMBER_SIZE = 10;
	private int posX;
	private int approaching_speed;
	
	private ImagePanel planetPanel;
	private ArrayList<MilitaryUnit>[] enemyArmy;
	private ImagePanel[] armyPanels;
	private int colorArmy;
	
	public EnemyPanel () {
		// Setup approaching speed and Init Position Variable
		approaching_speed = (INIT_POS_X - (int)(FRAME_WIDTH / 2)) / APPROACH_STEPS;
		posX = INIT_POS_X;
		
		// Setup Panel
		setLayout(null);
		setSize((int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		setOpaque(false);
		setLocation(posX, POS_Y);
		
		// Initialize Panels of Enemy Army
		armyPanels = new ImagePanel[ENEMY_ARMY_LENGHT];
		
		// Choose a random planet
		int num = (int)(PLANET_SELECTION_MIN + Math.random() * PLANET_SELECTION_MAX);
		
		// Initialize planet panel with the planet chosen above
		planetPanel = new ImagePanel(BASE_URL + "planet0" + num + ".png");
		planetPanel.setBounds(getWidth() / 2, 0, (int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		
		// Add listener to update ToolTip text with current information
		planetPanel.addMouseListener(new MouseButtonsListener() {
			public void mouseEntered(MouseEvent e) {
				planetPanel.setToolTipText(printStats());				
			}
			
			public void mouseExited(MouseEvent e) {
			}
		});
		
		// Choose a random color army
		this.colorArmy = (int)(ARMY_SELECTION_MIN + Math.random() * ARMY_SELECTION_MAX);
				
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
		/*
		 * - Set new army
		 * - Clean older Unit Panels
		 * - Load the panels with new army
		 */
		this.enemyArmy = enemyArmy;
		cleanPanels();
		loadArmy();
	}
	
	public ImagePanel[] getArmyPanels() {
		return armyPanels;
	}

	public void enemyComing() {
		posX -= approaching_speed;
		setLocation(posX,POS_Y);
	}
	
	public void resetEnemy() {
		
		// Reset position to Initial
		posX = INIT_POS_X;
		setLocation(posX,POS_Y);
		
		// Choose a new planet
		int num = (int)(PLANET_SELECTION_MIN + Math.random() * PLANET_SELECTION_MAX);
		planetPanel.changeImage(BASE_URL + "planet0" + num + ".png");
		
		// Choose a new color army
		colorArmy = (int)(ARMY_SELECTION_MIN + Math.random() * ARMY_SELECTION_MAX);
	}
	
	public void loadArmy() {
		
		/*
		 * For every army unit:
		 * 	- Check if there is any unit:
		 * 		+ Initialize a panel with its unit image
		 * 		+ Set its size
		 * 		+ Rotate image
		 * 		+ Add listener to get units on ToolTip
		 * 		+ Add to panel to armyPanels on its unit type
		 * 		+ Add panel to Enemy Panel
		 * Repaint Enemy Panel
		 */
		
		for (int i = 0; i < enemyArmy.length; i++) {
			if (enemyArmy[i].size() > 0) {
				ImagePanel unit = new ImagePanel(BASE_URL + "ships_" + i + "_" + colorArmy + ".png");
				unit.setBounds(ENEMY_SHIPS_POSITIONS[i][0], ENEMY_SHIPS_POSITIONS[i][1], SHIPS_SIZES[i], SHIPS_SIZES[i]);
				unit.rotateImage(INITAL_ENEMY_SHIP_ROTATION);
				
				unit.addMouseListener(new MouseButtonsListener(i) {
					public void mouseEntered(MouseEvent e) {
						unit.setToolTipText(getUnitStats(this.getId()));				
					}
					
					public void mouseExited(MouseEvent e) {
					}
				});
				
				armyPanels[i] = unit;	
				add(unit);
			}
		}
		
		repaint();
	}

	public void removeUnit(int unitType, MilitaryUnit unit) {
		
		// Iterate for army unit type to eliminate unit
		for (int i = 0; i < enemyArmy[unitType].size(); i++) {
			if (enemyArmy[unitType].get(i) == unit) {
				enemyArmy[unitType].remove(i);
				break;
			}
		}
		
		// If there is no other unit, remove image from panel and army panels
		if (enemyArmy[unitType].size() <= 0) {
			remove(armyPanels[unitType]);
			enemyArmy[unitType] = null;
			repaint();
		}
	}
	
	private String getUnitStats(int unitType) {
		String result = "<html>";
		
		result += "<b>" + Printing.printTitle(MILITARY_UNIT_NAMES[unitType]) + "</b><br><br>";
		result += Printing.printStringSized("Units: ", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(enemyArmy[unitType].size(), MAX_NUMBER_SIZE) + "<br>";							
		
		result += "</html>";
		
		return result;
	}
	
	private String printStats() {
		String result = "<html>";
		
		result += "<b>" + Printing.printTitle("Enemy Stats:") + "</b><br><br>";
		result += "<b>" + Printing.printTitle("fleet".toUpperCase()) + "</b><br><br>";
		for (int i = 0; i < MilitaryUnitOrder.MISSILELAUNCHER.ordinal(); i++) {
			if (enemyArmy == null) {
				result += Printing.printStringSized(MILITARY_UNIT_NAMES[i] + " -> ", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(0, MAX_NUMBER_SIZE) + "<br>";							
			}
			else {
				result += Printing.printStringSized(MILITARY_UNIT_NAMES[i] + " -> ", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(enemyArmy[i].size(), MAX_NUMBER_SIZE) + "<br>";
				
			}
		}
		
		result += "</html>";
		
		return result;
	}
	
	private void cleanPanels() {
		for (int i = 0; i < ENEMY_ARMY_LENGHT; i++) {		
			if (armyPanels[i] != null) {
				remove(armyPanels[i]);
				armyPanels[i] = null;
			}
		}
	}
}