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
		
		setLayout(null);
		setSize((int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		setOpaque(false);
		setLocation(0,0);
		
		this.armyPanels = new ImagePanel[7];
		
		planetPanel = new ImagePanel(BASE_URL + "planet00.png");
		planetPanel.setBounds((int)(-getWidth() / 2), 0, (int)(FRAME_WIDTH / 2), FRAME_HEIGHT);
		planetPanel.addMouseListener(new MouseButtonsListener() {
			public void mouseEntered(MouseEvent e) {
				planetPanel.setToolTipText(InterfaceController.instance.printStats());				
			}
			
			public void mouseExited(MouseEvent e) {
			}
		});
		
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
		this.playerArmy = playerArmy;
		cleanPanels();
		loadArmy();
	}
	
	public void loadArmy() {
		
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
		
		for (int i = 0; i < playerArmy[unitType].size(); i++) {
			if (playerArmy[unitType].get(i) == unit) {
				playerArmy[unitType].remove(i);
				break;
			}
		}
		
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
