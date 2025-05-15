package gui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import controllers.InterfaceController;
import events.MouseButtonsListener;
import ships.MilitaryUnit;
import utils.Printing;
import utils.Variables;
import utils.VariablesWindow;

public class PlayerPanel extends JPanel implements Variables , VariablesWindow {
	
	private final int MAX_LINE_SIZE = 64;
	private final int MAX_NUMBER_SIZE = 10;
	
	private ImagePanel planetPanel;
	private ArrayList<MilitaryUnit>[] playerArmy;
	private ImagePanel[] armyPanels;
	private JPanel armyPanel;
	
	public PlayerPanel(){
		super();
		
		// Setup panel
		setLayout(null);
		setSize((int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		setOpaque(false);
		setLocation(0,0);
		
		// Initialize army panels array
		this.armyPanels = new ImagePanel[PLANET_ARMY_LENGHT];
		
		// Initialize planet panel with its image
		planetPanel = new ImagePanel(BASE_URL + "planet00.png");
		planetPanel.setBounds(PLANET_PANEL_POSITION[0], PLANET_PANEL_POSITION[1], (int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		
		// Add listener to get its info for ToolTip
		planetPanel.addMouseListener(new MouseButtonsListener() {
			public void mouseEntered(MouseEvent e) {
				planetPanel.setToolTipText(InterfaceController.instance.printStats());				
			}
			
			public void mouseExited(MouseEvent e) {
			}
		});
		
		// Initialize army Panel - Necessary for defense units ToolTip
		armyPanel = new JPanel();
		armyPanel.setLayout(null);
		armyPanel.setBounds(0, 0, (int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		armyPanel.setOpaque(false);
			
		add(armyPanel);
		add(planetPanel);
	}

	public ArrayList<MilitaryUnit>[] getPlayerAmy() {
		return playerArmy;
	}

	public ImagePanel[] getArmyPanels() {
		return armyPanels;
	}
	
	public void setPlayerArmy(ArrayList<MilitaryUnit>[] playerArmy) {
		/*
		 * - Set new army
		 * - Clean older Unit Panels
		 * - Load the panels with new army
		 */
		this.playerArmy = playerArmy;
		cleanPanels();
		loadArmy();
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
		 * 		+ Add panel to Army Panel
		 * Repaint Player Panel
		 */		
		for (int i = 0; i < playerArmy.length; i++) {
			if (playerArmy[i].size() > 0) {
				ImagePanel unit = new ImagePanel(BASE_URL + "ships_" + i + "_0.png");
				unit.setBounds(PLAYER_SHIPS_POSITIONS[i][0], PLAYER_SHIPS_POSITIONS[i][1], SHIPS_SIZES[i], SHIPS_SIZES[i]);
				unit.rotateImage(INITAL_PLAYER_SHIP_ROTATION);
				
				unit.addMouseListener(new MouseButtonsListener(i) {
					public void mouseEntered(MouseEvent e) {
						unit.setToolTipText(getUnitStats(this.getId()));				
					}
					
					public void mouseExited(MouseEvent e) {
					}
				});
				
				armyPanels[i] = unit;	
				armyPanel.add(unit);
			}
		}
		
		repaint();
	}
	
	public void addUnit(int unitType, MilitaryUnit unit) {
		
		/*
		 * - Check if there is no other unit in this unit type:
		 * 		+ Generate new Image Panel with the unit type
		 * 		+ Size it and rotate it
		 * 		+ Add it to armyPanels
		 * 		+ Add listener to get units on ToolTip
		 * 		+ Add unit to army Panel
		 * - Add unit to army
		 */
		if (playerArmy[unitType].size() == 0) {
			ImagePanel unitPanel = new ImagePanel(BASE_URL + "ships_" + unitType + "_0.png");
			unitPanel.setBounds(PLAYER_SHIPS_POSITIONS[unitType][0], PLAYER_SHIPS_POSITIONS[unitType][1], SHIPS_SIZES[unitType], SHIPS_SIZES[unitType]);
			unitPanel.rotateImage(INITAL_PLAYER_SHIP_ROTATION);
			armyPanels[unitType] = unitPanel;
			
			unitPanel.addMouseListener(new MouseButtonsListener(unitType) {
				public void mouseEntered(MouseEvent e) {
					unitPanel.setToolTipText(getUnitStats(this.getId()));				
				}
				
				public void mouseExited(MouseEvent e) {
				}
			});
			
			armyPanel.add(unitPanel);
			repaint();
		}
		
		playerArmy[unitType].add(unit);
	}
	
	public void removeUnit(int unitType, MilitaryUnit unit) {
		
		// Iterate for army unit type to eliminate unit
		for (int i = 0; i < playerArmy[unitType].size(); i++) {
			if (playerArmy[unitType].get(i) == unit) {
				playerArmy[unitType].remove(i);
				break;
			}
		}
		
		// If there is no other unit, remove image from panel and army panels
		if (playerArmy[unitType].size() <= 0) {
			armyPanel.remove(armyPanels[unitType]);
			armyPanels[unitType] = null;
			repaint();
		}
	}
	
	private String getUnitStats(int unitType) {
		String result = "<html>";
		
		result += "<b>" + Printing.printTitle(MILITARY_UNIT_NAMES[unitType]) + "</b><br><br>";
		result += Printing.printStringSized("Units: ", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(playerArmy[unitType].size(), MAX_NUMBER_SIZE) + "<br>";							
		
		result += "</html>";
		
		return result;
	}
	
	private void cleanPanels() {
		for (int i = 0; i < 7; i++) {		
			if (armyPanels[i] != null) {
				armyPanel.remove(armyPanels[i]);
				armyPanels[i] = null;
			}
		}
		repaint();
	}
}
