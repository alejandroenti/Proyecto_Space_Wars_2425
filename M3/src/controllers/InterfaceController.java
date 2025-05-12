package controllers;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import battle.Battle;
import exceptions.ResourceException;
import gui.BuyWindow;
import gui.EnemyPanel;
import gui.ImagePanel;
import gui.MainWindow;
import gui.PlayerPanel;
import planets.Planet;
import ships.ArmoredShip;
import ships.BattleShip;
import ships.HeavyHunter;
import ships.LightHunter;
import ships.MilitaryUnit;
import utils.Variables;
import utils.VariablesWindow;
import utils.Variables.MilitaryUnitOrder;

public class InterfaceController implements Variables, VariablesWindow {
	
	private Planet planet;
	private PlayerPanel playerPanel;
	private EnemyPanel enemyPanel;
	private MainWindow mainWindow;
	private Battle battle;
	private String buyStringContext;
	
	public static InterfaceController instance;
	
	public InterfaceController(Planet planet, MainWindow mainWindow) {
		super();
		
		InterfaceController.instance = this;
		
		this.planet = planet;
		this.mainWindow = mainWindow;
		this.playerPanel = mainWindow.getPlayerPanel();
		this.enemyPanel = mainWindow.getEnemyPanel();
		this.battle = new Battle();
		this.buyStringContext = "";
		
		this.mainWindow.approachEnemy();
		
		planet.generateInitShips();
		playerPanel.setPlayerArmy(planet.getArmy());
	}
	
	public ArrayList<MilitaryUnit>[] getPlanetArmy() {
		return planet.getArmy();
	}
	
	public ArrayList<MilitaryUnit>[] getEnemyArmy() {
		return mainWindow.getEnemyPanel().getEnemyArmy();
	}
	
	public void removePlanetArmyUnit(int unitType, MilitaryUnit unit) {
		getPlanetArmy()[unitType].remove(unit);
		playerPanel.removeUnit(unitType, unit);
	}
	
	public void removeEnemyArmyUnit(int unitType, MilitaryUnit unit) {
		enemyPanel.removeUnit(unitType, unit);
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
	
	public void startBattle() {
		mainWindow.getButtonsPanel().hidePanel();
		battle.createBattle(getPlanetArmy(), getEnemyArmy());
		mainWindow.getButtonsPanel().showPanel();
	}
	
	public void createEnemyArmy() {
		ArrayList<MilitaryUnit>[] army = new ArrayList[4];
		
		int[] probabilities = new int[4];
		int numRandom;
		boolean canBuyUnit = true;
		
		int metal = (METAL_BASE_PLANET_ARMY - (int)(METAL_BASE_PLANET_ARMY / 4));// + (METAL_BASE_PLANET_ARMY * battle.getBattles() * (int)(PERCENTAGE_INCREMENT_ENEMY_RESOURCE / 100));
		int deuterium = (DEUTERIUM_BASE_PLANET_ARMY - (int)(DEUTERIUM_BASE_PLANET_ARMY / 4)); //+ (DEUTERIUM_BASE_PLANET_ARMY * battle.getBattles() * (int)(PERCENTAGE_INCREMENT_ENEMY_RESOURCE / 100));
				
		probabilities[0] = CHANGE_GENERATE_ENEMY_UNIT[0];
		army[0] = new ArrayList<MilitaryUnit>();
		for (int i = 1; i < probabilities.length; i++) {
			army[i] = new ArrayList<MilitaryUnit>();
			probabilities[i] = CHANGE_GENERATE_ENEMY_UNIT[i] + probabilities[i - 1];
		}
		
		do {
			
			numRandom = (int) (1 + Math.random() * 100);
			
			if (numRandom <= probabilities[MilitaryUnitOrder.LIGHTHUNTER.ordinal()]) {
				LightHunter ship = new LightHunter();
				if (metal - ship.getMetalCost() < 0 || deuterium - ship.getDeuteriumCost() < 0) {
					canBuyUnit = false;
					continue;
				}
				metal -= ship.getMetalCost();
				deuterium -= ship.getDeuteriumCost();
				army[MilitaryUnitOrder.LIGHTHUNTER.ordinal()].add(ship);
			}
			else if (numRandom <= probabilities[MilitaryUnitOrder.HEAVYHUNTER.ordinal()]) {
				HeavyHunter ship = new HeavyHunter();
				if (metal - ship.getMetalCost() < 0 || deuterium - ship.getDeuteriumCost() < 0) {
					continue;
				}
				metal -= ship.getMetalCost();
				deuterium -= ship.getDeuteriumCost();
				army[MilitaryUnitOrder.HEAVYHUNTER.ordinal()].add(ship);
			}
			else if (numRandom <= probabilities[MilitaryUnitOrder.BATTLESHIP.ordinal()]) {
				BattleShip ship = new BattleShip();
				if (metal - ship.getMetalCost() < 0 || deuterium - ship.getDeuteriumCost() < 0) {
					continue;
				}
				metal -= ship.getMetalCost();
				deuterium -= ship.getDeuteriumCost();
				army[MilitaryUnitOrder.BATTLESHIP.ordinal()].add(ship);
			}
			else if (numRandom <= probabilities[MilitaryUnitOrder.ARMOREDSHIP.ordinal()]) {
				ArmoredShip ship = new ArmoredShip();
				if (metal - ship.getMetalCost() < 0 || deuterium - ship.getDeuteriumCost() < 0) {
					continue;
				}
				metal -= ship.getMetalCost();
				deuterium -= ship.getDeuteriumCost();
				army[MilitaryUnitOrder.ARMOREDSHIP.ordinal()].add(ship);
			}
		} while (canBuyUnit);
		
		enemyPanel.setEnemyArmy(army);
	}
	
	public void selectAttacker(int army, int unitType) {
		int posX = 0, posY = 0;
		
		if (army == 0) {
			posX = PLAYER_SHIPS_POSITIONS[unitType][0] + (int)(SHIPS_SIZES[unitType] / 2) - 24;
			posY = PLAYER_SHIPS_POSITIONS[unitType][1] - 48;
		}
		else {
			posX = (int)(FRAME_WIDTH / 2) + ENEMY_SHIPS_POSITIONS[unitType][0] + (int)(SHIPS_SIZES[unitType] / 2) - 24;
			posY = ENEMY_SHIPS_POSITIONS[unitType][1] - 48;
		}
		
		mainWindow.getAttackerSelectorPanel().setBounds(posX, posY, 48, 48);
		mainWindow.repaint();
		
		sleepThread(1000);
	}
	
	public void selectDefender(int army, int unitType) {
		int posX = 0, posY = 0;
		
		if (army == 0) {
			posX = PLAYER_SHIPS_POSITIONS[unitType][0] + (int)(SHIPS_SIZES[unitType] / 2) - 24;
			posY = PLAYER_SHIPS_POSITIONS[unitType][1] - 48;
		}
		else {
			posX = (int)(FRAME_WIDTH / 2) + ENEMY_SHIPS_POSITIONS[unitType][0] + (int)(SHIPS_SIZES[unitType] / 2) - 24;
			posY = ENEMY_SHIPS_POSITIONS[unitType][1] - 48;
		}
		
		mainWindow.getDefendeerSelectorPanel().setBounds(posX, posY, 48, 48);
		mainWindow.repaint();
		
		sleepThread(1000);
	}
	
	private void sleepThread(int milisecs) {
		try {
			Thread.sleep(milisecs);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
