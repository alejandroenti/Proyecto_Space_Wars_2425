package controllers;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import battle.Battle;
import exceptions.ResourceException;
import gui.EnemyPanel;
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

public class InterfaceController implements Variables, VariablesWindow {
	
	private Planet planet;
	private PlayerPanel playerPanel;
	private EnemyPanel enemyPanel;
	private MainWindow mainWindow;
	private Battle battle;
	private String buyStringContext;
	
	public static InterfaceController instance;
	
	public InterfaceController() {
		super();
		
		InterfaceController.instance = this;
		
		this.planet = new Planet();
		this.mainWindow = new MainWindow();
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
	
	public int getPlanetId() {
		return planet.getPlanet_id();
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
	
	public String printStats() {
		return planet.printStats();
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

		int[] position = new int[2];
		
		if (army == 0) {
			position[0] = PLAYER_SHIPS_POSITIONS[unitType][0] + (int)(SHIPS_SIZES[unitType] / 2) - 24;
			position[1] = PLAYER_SHIPS_POSITIONS[unitType][1] - 48;
		}
		else {
			position[0] = (int)(FRAME_WIDTH / 2) + ENEMY_SHIPS_POSITIONS[unitType][0] + (int)(SHIPS_SIZES[unitType] / 2) - 24;
			position[1] = ENEMY_SHIPS_POSITIONS[unitType][1] - 48;
		}
		
		mainWindow.getAttackerSelectorPanel().setBounds(position[0], position[1], 48, 48);
		mainWindow.repaint();
		
		sleepThread(500);
	}
	
	public void selectDefender(int army, int unitType) {

		int[] position = new int[2];
		
		if (army == 0) {
			position[0] = PLAYER_SHIPS_POSITIONS[unitType][0] + (int)(SHIPS_SIZES[unitType] / 2) - 24;
			position[1] = PLAYER_SHIPS_POSITIONS[unitType][1] - 48;
		}
		else {
			position[0] = (int)(FRAME_WIDTH / 2) + ENEMY_SHIPS_POSITIONS[unitType][0] + (int)(SHIPS_SIZES[unitType] / 2) - 24;
			position[1] = ENEMY_SHIPS_POSITIONS[unitType][1] - 48;
		}
		
		mainWindow.getDefendeerSelectorPanel().setBounds(position[0], position[1], 48, 48);
		mainWindow.repaint();
		
		sleepThread(500);
	}
	
	public void hideSelectors() {
		
		mainWindow.getAttackerSelectorPanel().setBounds(-1000, 0, 48, 48);
		mainWindow.getDefendeerSelectorPanel().setBounds(-1000, 0, 48, 48);
		mainWindow.repaint();
	}
	
	public void shootBullet(int attackerArmy, int attackerUnitType, int defenderUnitType) {
		
		int[] initialPosition = new int[2];
		int[] finalPosition = new int[2];
		
		// Reset current Bullet rotation
		mainWindow.getBullPanel().rotateImage(-mainWindow.getBullPanel().getCurrentRotation());
		
		// Calculate Initial and Final Position and Changing Bullet and Explosion Type
		if (attackerArmy == 0) {
			initialPosition[0] = PLAYER_SHIPS_POSITIONS[attackerUnitType][0] + SHIPS_SIZES[attackerUnitType] + 12;
			initialPosition[1] = PLAYER_SHIPS_POSITIONS[attackerUnitType][1] + (int)(SHIPS_SIZES[attackerUnitType] / 2);
			finalPosition[0] = (int)(FRAME_WIDTH / 2) + ENEMY_SHIPS_POSITIONS[defenderUnitType][0] + (int)(SHIPS_SIZES[defenderUnitType] / 2);
			finalPosition[1] = ENEMY_SHIPS_POSITIONS[defenderUnitType][1] + (int)(SHIPS_SIZES[defenderUnitType] / 2);
			mainWindow.getBullPanel().changeImage(BASE_URL + "bullet_" + attackerUnitType + "_player.png");
			mainWindow.getBullPanel().rotateImage(INITAL_ENEMY_SHIP_ROTATION);
			mainWindow.getExplosionPanel().changeImage(BASE_URL + "explosion_enemy.png");
		}
		else {
			initialPosition[0] = (int)(FRAME_WIDTH / 2) + ENEMY_SHIPS_POSITIONS[attackerUnitType][0] - 12;
			initialPosition[1] = ENEMY_SHIPS_POSITIONS[attackerUnitType][1] + (int)(SHIPS_SIZES[attackerUnitType] / 2);
			finalPosition[0] = PLAYER_SHIPS_POSITIONS[defenderUnitType][0] + (int)(SHIPS_SIZES[defenderUnitType] / 2);
			finalPosition[1] = PLAYER_SHIPS_POSITIONS[defenderUnitType][1] + (int)(SHIPS_SIZES[defenderUnitType] / 2);
			mainWindow.getBullPanel().changeImage(BASE_URL + "bullet_" + attackerUnitType + "_enemy.png");
			mainWindow.getBullPanel().rotateImage(INITAL_PLAYER_SHIP_ROTATION);
			mainWindow.getExplosionPanel().changeImage(BASE_URL + "explosion_player.png");
		}
		
		
		// Calculate Unitary Direction
		int[] direction = { finalPosition[0] - initialPosition[0], finalPosition[1] - initialPosition[1] };
		double magnitudeDirection = Math.sqrt(Math.pow(direction[0], 2) + Math.pow(direction[1], 2));
		double[] unitaryDirection = { (direction[0] / magnitudeDirection), (direction[1] / magnitudeDirection) };
		
		// Calculate Dot Product
		double dot = initialPosition[0] * finalPosition[0] + initialPosition[1] * finalPosition[1];
		double magnitudeInitialPosition = Math.sqrt(Math.pow(initialPosition[0], 2) + Math.pow(initialPosition[1], 2));
		double magnitudeFinalPosition = Math.sqrt(Math.pow(finalPosition[0], 2) + Math.pow(finalPosition[1], 2));
		double cosTheta = dot / (magnitudeInitialPosition * magnitudeFinalPosition);
        cosTheta = Math.max(-1.0, Math.min(1.0, cosTheta));
        double angleRad = Math.acos(cosTheta);
        double angleDeg = Math.toDegrees(angleRad);
        
        // Change angle if Defender Ship is above us
        if (finalPosition[1] > initialPosition[1]) {
        	angleDeg *= -1;
        }
        
        /*
         * Apply rotation
         *  - Player Ships rotate on opposite way than Enemy Ships
         */
        if (attackerArmy == 0) {
        	mainWindow.getPlayerPanel().getArmyPanels()[attackerUnitType].rotateImage(angleDeg * -1);
        	mainWindow.getBullPanel().rotateImage(angleDeg * -1);
        }
		else {
			mainWindow.getEnemyPanel().getArmyPanels()[attackerUnitType].rotateImage(angleDeg);
			mainWindow.getBullPanel().rotateImage(angleDeg);
		}
        
        double posX = initialPosition[0];
        double posY = initialPosition[1];
        boolean hit = false;
        
        do {
        	// Paint Bullet in the correct position
        	mainWindow.getBullPanel().setBounds((int)posX, (int)posY, 24, 24);        	
        	mainWindow.repaint();
        	
        	// Update position
        	posX += unitaryDirection[0] * 0.05;
        	posY += unitaryDirection[1] * 0.05;
        	
        	// Check if Bullet has impacted in Defender
        	if (attackerArmy == 0) {
            	hit = posX > finalPosition[0];
            }
    		else {
    			hit = posX < finalPosition[0];
    		}
        } while (!hit);
        
        // Position Bullet out of the view
    	mainWindow.getBullPanel().setBounds(-1000, 0, 24, 24);
    	mainWindow.repaint();
        
    	// Rotate again Attacker to its Initial Position
        if (attackerArmy == 0) {
        	mainWindow.getPlayerPanel().getArmyPanels()[attackerUnitType].rotateImage(angleDeg);
        }
		else {
			mainWindow.getEnemyPanel().getArmyPanels()[attackerUnitType].rotateImage(angleDeg * -1);
		}
        
        sleepThread(500);
        
        // Calculate position of Explosion
        int posExplosionX = finalPosition[0] - 32;
        int posExplosionY = finalPosition[1] - 32;
        
        // Animate Explosion through different sprites
        mainWindow.getExplosionPanel().setBounds(posExplosionX, posExplosionY, 64, 64);
        for (BufferedImage image : mainWindow.getExplosionSprites()) {
        	mainWindow.getExplosionPanel().changeImage(image);
        	mainWindow.repaint();
        	sleepThread(10);
        }
        mainWindow.getExplosionPanel().setBounds(-1000, 0, 24, 24);
        mainWindow.repaint();
	}
	
	public void collectRubble(int[] wasteMetalDeuterium) {
		planet.setMetal(planet.getMetal() + wasteMetalDeuterium[0]);
		planet.setDeuterium(planet.getDeuterium() + wasteMetalDeuterium[1]);
	}
	
	public void showBattleWinner(String message) {
		mainWindow.showBattleWinner(message);
	}
	
	private void sleepThread(int milisecs) {
		try {
			Thread.sleep(milisecs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
