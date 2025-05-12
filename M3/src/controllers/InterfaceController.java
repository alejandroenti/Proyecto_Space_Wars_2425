package controllers;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import battle.Battle;
import exceptions.ResourceException;
import gui.BuyWindow;
import gui.EnemyPanel;
import gui.MainWindow;
import gui.PlayerPanel;
import planets.Planet;
import ships.MilitaryUnit;
import utils.Variables;
import utils.VariablesWindow;

public class InterfaceController implements Variables, VariablesWindow {
	
	private Planet planet;
	private PlayerPanel playerPanel;
	private EnemyPanel enemyPanel;
	private MainWindow mainWindow;
	private BuyWindow buyWindow;
	private Battle battle;
	private String buyStringContext;
	
	public static InterfaceController instance;
	
	public InterfaceController(Planet planet, MainWindow mainWindow) {
		super();
		
		this.planet = planet;
		this.mainWindow = mainWindow;
		this.playerPanel = mainWindow.getPlayerPanel();
		this.enemyPanel = mainWindow.getEnemyPanel();
		this.buyStringContext = "";
		
		InterfaceController.instance = this;
		
		planet.generateInitShips();
		playerPanel.setPlayerArmy(planet.getArmy());
	}
	
	public ArrayList<MilitaryUnit>[] getPlanetArmy() {
		return planet.getArmy();
	}
	
	public int getPlanetDefenseTechnology() {
		return planet.getTechnologyDefense();
	}
	
	public int getPlanetAttackTechnology() {
		return planet.getTechnologyAttack();
	}
	
	public int getPlayerUnitNumber(int unitType) {	
		return getPlanetArmy()[unitType].size();
	}
	
	public void upgradePlanetDefenseTechnology() throws ResourceException{
		int currentDefenseLevel = getPlanetDefenseTechnology();
		planet.upgradeTechnologyDefense();
		
		if (currentDefenseLevel == getPlanetDefenseTechnology()) {
			throw new ResourceException();
		}
	}
	
	public void upgradePlanetAttackTechnology() throws ResourceException{
		int currentAttackLevel = getPlanetAttackTechnology();
		planet.upgradeTechnologyAttack();
		
		if (currentAttackLevel == getPlanetAttackTechnology()) {
			throw new ResourceException();
		}
	}

	public void addArmiesPanels(PlayerPanel playerPanel, EnemyPanel enemyPanel) {
		this.playerPanel = playerPanel;
		this.enemyPanel = enemyPanel;
	}
	
	public void buyMilitaryUnit(ArrayList<Integer> unitType, ArrayList<Integer> quantity) {
		
		buyStringContext = "";
		
		for (int i = 0; i < unitType.size(); i++) {
			switch (unitType.get(i)) {
			case 0:
				planet.newLightHunter(quantity.get(i));
				break;
			case 1:
				planet.newHeavytHunter(quantity.get(i));
				break;
			case 2:
				planet.newBattleShip(quantity.get(i));
				break;
			case 3:
				planet.newArmoredShip(quantity.get(i));
				break;
			case 4:
				planet.newMissileLauncher(quantity.get(i));
				break;
			case 5:
				planet.newIonCannon(quantity.get(i));
				break;
			case 6:
				planet.newPlasmaCannon(quantity.get(i));
				break;
			default:
				break;
			}
		}
		
		JOptionPane.showMessageDialog(null, buyStringContext, "Buy Resume", JOptionPane.INFORMATION_MESSAGE);
		InterfaceController.instance.playerPanel.setPlayerArmy(getPlanetArmy());
	}
	
	public void addBuyInfo(String message) {
		buyStringContext += message + "\n";
	}
	
	public void printStats() {
		planet.printStats();
	}
}
