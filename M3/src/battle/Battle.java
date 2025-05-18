package battle;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import controllers.DatabaseController;
import controllers.InterfaceController;
import ships.MilitaryUnit;
import utils.Printing;
import utils.Variables;
import xslt.XSLTransformer;

public class Battle implements Variables {
	
	private int battles; // Number of accumulated battles in the current planet
	private int num_battle; // Id of the battle
	private int defeats; // Number of accumulated defeats
	private String saved_xml;
	
	// Generating the variables that will store the information of the current battle
	private ArrayList<MilitaryUnit>[] planetArmy, enemyArmy;
	private ArrayList[][] armies;
	private String battleDevelopment;
	private int[][] initialCostFleet;
	private int initialNumberUnitsPlanet, initialNumberUnitsEnemy;
	private int[] wasteMetalDeuterium;
	private int[] enemyDrops, planetDrops;
	private int[][] resourcesLosses;
	private int[][] initialArmies;
	private int actualNumberUnitsPlanet, actualNumberUnitsEnemy;
	private int[] actualArmyPlanet, actualArmyEnemy;
	
	public Battle() {
		super();
		
		this.defeats = 0;
	}
	
	public int getBattles() {
		return battles;
	}

	
	public int getDefeats() {
		return defeats;
	}

	public void createBattle(ArrayList<MilitaryUnit>[] planetArmy, ArrayList<MilitaryUnit>[] enemyArmy) {
		
		this.planetArmy = planetArmy;
		this.enemyArmy = enemyArmy;
		
		// Storing in a matrix of ArrayList both the planet's and enemy's armies
		this.armies = new ArrayList[2][7];
		this.armies[0] = planetArmy;
		this.armies[1] = enemyArmy;
		
		this.battleDevelopment = "";
		
		// Calculating the cost of both fleets
		this.initialCostFleet = new int[2][2];
		calculateInitialCostFleet();
		
		// Calculating the initial units of each army
		this.initialNumberUnitsPlanet = calculateUnitNumber(0);
		this.initialNumberUnitsEnemy = calculateUnitNumber(1);
		this.actualNumberUnitsPlanet = this.initialNumberUnitsPlanet;
		this.actualNumberUnitsEnemy = this.initialNumberUnitsEnemy;
		
		// Initializing empty arrays and matrixes to store the battle's waste, drops and losses
		this.wasteMetalDeuterium = new int[2];
		this.planetDrops = new int[2];
		this.enemyDrops = new int[2];
		this.resourcesLosses = new int[2][3];
		
		// Calculating initial armies and initializing actual armies with the initial armies
		this.initialArmies = new int[2][7];
		calculateInitialArmy();
		this.actualArmyPlanet = initialArmies[0];
		this.actualArmyEnemy = initialArmies[1];
		
		startBattle();
	}
	
	public ArrayList<MilitaryUnit>[] getPlanetArmy() {
		return armies[0];
	}
	
	public ArrayList<MilitaryUnit>[] getEnemyArmy() {
		return armies[1];
	}
	
	private void calculateInitialCostFleet() {
		
		/*
		 * 1. Each army and type of ship is iterated over
		 * 2. The metal and deuterium costs of each unit are added
		 * 3. The cost of the initial armies are put in the matrix
		 */
		
		int metal = 0, deuterium = 0;
		
		for (int i = 0; i < armies.length; i++) {
			for (int j = 0; j < armies[i].length; j++) {
				// Checking if there are units of a certain type of ship in the army we are considering and, if there are, we calculate their cost 
				if (armies[i][j].size() > 0) { 
					metal += ((MilitaryUnit) armies[i][j].get(0)).getMetalCost() * armies[i][j].size();
					deuterium += ((MilitaryUnit) armies[i][j].get(0)).getDeuteriumCost() * armies[i][j].size();
				}
			}
			
			initialCostFleet[i][0] = metal;
			initialCostFleet[i][1] = deuterium;
			
			metal = 0;
			deuterium = 0;
		}
	}
	
	// We calculate the number of units there is in an army
	private int calculateUnitNumber(int armyNumber) {
		int total = 0;
		
		for (int i = 0; i < armies[armyNumber].length; i++) {
			try {
				total +=  armies[armyNumber][i].size();
			} catch (NullPointerException e) {
				// ENEMY ARMY ONLY INITIALIZE UNTIL 4TH POSITION
				continue;
			}
		}
		
		return total;
	}
	
	// The number of initial units of each type of ship for each army is calculated and stored in a matrix
	private void calculateInitialArmy() {
		
		for (int i = 0; i < armies.length; i++) {
			for (int j = 0; j < armies[i].length; j++) {
				initialArmies[i][j] = armies[i][j].size();
			}
		}
	}
	
	// The number of current units of each type of ship for each army is calculated and stored in a matrix
	private int[] calculateActualArmy(int armyNumber) {
		
		int[] actualArmy = new int[7];
		
		for (int i = 0; i < armies[armyNumber].length; i++) {
			try {
				actualArmy[i] =  armies[armyNumber][i].size();
			} catch (NullPointerException e) {
				// ENEMY ARMY ONLY INITIALIZE UNTIL 4TH POSITION
				continue;
			}
		}
		
		return actualArmy;
	}
	
	// Method that contains the battle
	public void startBattle() {
		
		int order = (int)(Math.random() * 2); // Whether the planet or enemy start is decided randomly
		
		battleDevelopment = Printing.printStringCentred("THE BATTLE STARTS", '*', 60) + "\n";
		

		if (initialNumberUnitsPlanet != 0) { // Checking the planet has at least one unit (if not we skip the bulk of the battle and go to its ending
						
			resetUnitsArmor(armies); // The armor for the units that the planet has left are reseted
			
			// The percentage of alive army is calculated (if an army is below 20% of its initial army, the battle ends)
			int percentageArmyPlanetAlive = (int)((actualNumberUnitsPlanet / initialNumberUnitsPlanet) * 100);
			int percentageArmyEnemyAlive = (int)((actualNumberUnitsEnemy / initialNumberUnitsEnemy) * 100);
			
			// Repeat this block until one of the armies is at less than 20% its initial size (each time it starts again is because the attacker changes)
			do {
				battleDevelopment += Printing.printStringCentred("CHANGE ATTACKER", '*', 60) + "\n";
				
				MilitaryUnit attacker = null;
				MilitaryUnit defender = null;
				String attacker_name = null;
				String defender_name = null;
				int attackerType = 0;
				int defenderType = 0;
				
				int[] probabilities;
				
				// When the attacker is the planet and the enemy defends:
				if (order % 2 == 0) {
					int numAttacker = 0, numDefender = 1;
					int numRandom;
					probabilities = new int[7];
					probabilities[0] = CHANCE_ATTACK_PLANET_UNITS[0];
					int shipRandom = 0;
					
					// The probability a unit type has to attack is between the percentage in the previous array position an the percentage in the unit's type array position
					for (int i = 1; i < probabilities.length; i++) {
						probabilities[i] = probabilities[i - 1] + CHANCE_ATTACK_PLANET_UNITS[i];
					}
					
					// We choose a random number that will determine which group will attack (if the group is empty, we repeat this block until we have a group that is not empty)
					// If the group is not empty, a random ship will be chosen
					do {
						numRandom = (int)(1 + Math.random() * 100);
						if (numRandom < probabilities[MilitaryUnitOrder.LIGHTHUNTER.ordinal()] && armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size() > 0) {
							attackerType = MilitaryUnitOrder.LIGHTHUNTER.ordinal();
							shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size());
							attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].get(shipRandom));
							attacker_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.LIGHTHUNTER.ordinal()];
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.HEAVYHUNTER.ordinal()] && armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size() > 0) {
							attackerType = MilitaryUnitOrder.HEAVYHUNTER.ordinal();
							shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size());
							attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].get(shipRandom));
							attacker_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.HEAVYHUNTER.ordinal()];
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.BATTLESHIP.ordinal()] && armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].size() > 0) {
							attackerType = MilitaryUnitOrder.BATTLESHIP.ordinal();
							shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].size());
							attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].get(shipRandom));
							attacker_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.BATTLESHIP.ordinal()];
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.ARMOREDSHIP.ordinal()] && armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size() > 0) {
							attackerType = MilitaryUnitOrder.ARMOREDSHIP.ordinal();
							shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size());
							attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].get(shipRandom));
							attacker_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.ARMOREDSHIP.ordinal()];
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.MISSILELAUNCHER.ordinal()] && armies[numAttacker][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].size() > 0) {
							attackerType = MilitaryUnitOrder.MISSILELAUNCHER.ordinal();
							shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].size());
							attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].get(shipRandom));
							attacker_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.MISSILELAUNCHER.ordinal()];
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.IONCANNON.ordinal()] && armies[numAttacker][MilitaryUnitOrder.IONCANNON.ordinal()].size() > 0) {
							attackerType = MilitaryUnitOrder.IONCANNON.ordinal();
							shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.IONCANNON.ordinal()].size());
							attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.IONCANNON.ordinal()].get(shipRandom));
							attacker_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.IONCANNON.ordinal()];
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.PLASMACANNON.ordinal()] && armies[numAttacker][MilitaryUnitOrder.PLASMACANNON.ordinal()].size() > 0) {
							attackerType = MilitaryUnitOrder.PLASMACANNON.ordinal();
							shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.PLASMACANNON.ordinal()].size());
							attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.PLASMACANNON.ordinal()].get(shipRandom));
							attacker_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.PLASMACANNON.ordinal()];
						}
					} while (attacker == null);
					
					InterfaceController.instance.selectAttacker(numAttacker, attackerType);
					
					// A random enemy group is chosen randomly, if it is not empty, a random ship is chosen
					// This block is repeated while a random number is below or equal to the chance to attack again of the attacker's ship
					do {
						numRandom = (int)(1 + Math.random() * 100);
						probabilities = new int[4];
								
						// In the defender's case, the probabilities are calculated by the proportion of each group
						probabilities[0] = (int)(100 * armies[numDefender][0].size() / actualNumberUnitsEnemy);
						
						for (int i = 1; i < probabilities.length; i++) {
							probabilities[i] = probabilities[i - 1] + (int)(100 * armies[numDefender][i].size() / actualNumberUnitsEnemy);
						}
						
						do {
							if (numRandom <= probabilities[MilitaryUnitOrder.LIGHTHUNTER.ordinal()] && armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size() > 0) {
								defenderType = MilitaryUnitOrder.LIGHTHUNTER.ordinal();
								shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size());
								defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].get(shipRandom));
								defender_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.LIGHTHUNTER.ordinal()];
							}
							else if (numRandom <= probabilities[MilitaryUnitOrder.HEAVYHUNTER.ordinal()] && armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size() > 0) {
								defenderType = MilitaryUnitOrder.HEAVYHUNTER.ordinal();
								shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size());
								defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].get(shipRandom));
								defender_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.HEAVYHUNTER.ordinal()];
							}
							else if (numRandom <= probabilities[MilitaryUnitOrder.BATTLESHIP.ordinal()] && armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].size() > 0) {
								defenderType = MilitaryUnitOrder.BATTLESHIP.ordinal();
								shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].size());
								defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].get(shipRandom));
								defender_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.BATTLESHIP.ordinal()];
							}
							else if (numRandom <= probabilities[MilitaryUnitOrder.ARMOREDSHIP.ordinal()] && armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size() > 0) {
								defenderType = MilitaryUnitOrder.ARMOREDSHIP.ordinal();
								shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size());
								defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].get(shipRandom));
								defender_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.ARMOREDSHIP.ordinal()];
							}
							numRandom = (int)(1 + Math.random() * 100);
						} while (defender == null);
						
						InterfaceController.instance.selectDefender(numDefender, defenderType);
						
						// The attack occurs
						battleDevelopment += "Attacks Planet: " + attacker_name + " attacks " + defender_name + "\n";
						battleDevelopment += attacker_name + " generates the damage = " + attacker.attack() + "\n";
						defender.takeDamage(attacker.attack());
						battleDevelopment += defender_name + " stays with armor = " + defender.getActualArmor() + "\n";
						
						InterfaceController.instance.shootBullet(numAttacker, attackerType, defenderType);
						
						// If the defender loses its armor after the attack, it is randomly decided whether it generates waste and if it does, it is added to the wasteMetalDeuterium array
						if (defender.getActualArmor() <= 0) {
							numRandom = (int)(1 + Math.random() * 100);
							
							if (numRandom < defender.getChanceGeneratingWaste()) {
								wasteMetalDeuterium[0] += defender.getMetalCost() * PERCENTAGE_WASTE / 100;
								wasteMetalDeuterium[1] += defender.getDeuteriumCost() * PERCENTAGE_WASTE / 100;
							}
							
							// The value in metal and deuterium of the lost ship is added to enemyDrops
							enemyDrops[0] += defender.getMetalCost();
							enemyDrops[1] += defender.getDeuteriumCost();
							
							// The ship is eliminated from the armies matrix
							armies[numDefender][defenderType].remove(defender);
							InterfaceController.instance.removeEnemyArmyUnit(defenderType, defender);
							
							// When the ArrayList arrives to 0, it converts to null, we create another empty ArrayList
							if (armies[numDefender][defenderType] == null) {
								armies[numDefender][defenderType] = new ArrayList<MilitaryUnit>();
							}
							
							// We update the defender's unit number, actual army and alive enemy army percentage
							actualNumberUnitsEnemy = calculateUnitNumber(numDefender);
							actualArmyEnemy = calculateActualArmy(numDefender);
							
							battleDevelopment += "We eliminate " + defender_name + "\n";
							
							percentageArmyEnemyAlive = (int)((100 * actualNumberUnitsEnemy / initialNumberUnitsEnemy));	
						}
						
						if (percentageArmyEnemyAlive < 20) {
							break;
						}
						
						numRandom = (int)(1 + Math.random() * 100);
						
					} while (numRandom <= attacker.getChanceAttackAgain());
					
					
								
				} // When the enemy is the attacker and the planet defends
				else {
					int numAttacker = 1, numDefender = 0;
					int numRandom;
					int shipRandom = 0;
					
					probabilities = new int[4];
					probabilities[0] = CHANCE_ATTACK_ENEMY_UNITS[0];
					
					// The probability a unit type has to attack is between the percentage in the previous array position an the percentage in the unit's type array position
					for (int i = 1; i < probabilities.length; i++) {
						probabilities[i] = probabilities[i - 1] + CHANCE_ATTACK_ENEMY_UNITS[i];
					}
					
					// We choose a random number that will determine which enemy group will attack (if the group is empty, we repeat this block until we have a group that is not empty)
					// If the group is not empty, a random ship will be chosen
					do {	
						
						numRandom = (int)(1 + Math.random() * 100);
						
						if (numRandom <= probabilities[MilitaryUnitOrder.LIGHTHUNTER.ordinal()] && armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size() > 0) {
							attackerType = MilitaryUnitOrder.LIGHTHUNTER.ordinal();
							shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size());
							attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].get(shipRandom));
							attacker_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.LIGHTHUNTER.ordinal()];
						}
						else if (numRandom <= probabilities[MilitaryUnitOrder.HEAVYHUNTER.ordinal()] && armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size() > 0) {
							attackerType = MilitaryUnitOrder.HEAVYHUNTER.ordinal();
							shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size());
							attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].get(shipRandom));
							attacker_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.HEAVYHUNTER.ordinal()];
						}
						else if (numRandom <= probabilities[MilitaryUnitOrder.BATTLESHIP.ordinal()] && armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].size() > 0) {
							attackerType = MilitaryUnitOrder.BATTLESHIP.ordinal();
							shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].size());
							attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].get(shipRandom));
							attacker_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.BATTLESHIP.ordinal()];
						}
						else if (numRandom <= probabilities[MilitaryUnitOrder.ARMOREDSHIP.ordinal()] && armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size() > 0) {
							attackerType = MilitaryUnitOrder.ARMOREDSHIP.ordinal();
							shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size());
							attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].get(shipRandom));
							attacker_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.ARMOREDSHIP.ordinal()];
						}
					} while (attacker == null);
					
					InterfaceController.instance.selectAttacker(numAttacker, attackerType);
					
					// A random planet group is chosen randomly, if it is not empty, a random ship is chosen
					// This block is repeated while a random number is below or equal to the chance to attack again of the attacker's ship
					do {
						numRandom = (int)(1 + Math.random() * 100);

						probabilities = new int[7];
						probabilities[0] = (int)(100 * armies[numDefender][0].size() / actualNumberUnitsPlanet);
						
						// In the defender's case, the probabilities are calculated by the proportion of each group
						for (int i = 1; i < probabilities.length; i++) {
							probabilities[i] = probabilities[i - 1] + (int)(100 * armies[numDefender][i].size() / actualNumberUnitsPlanet);
						}
						
						do {
							if (numRandom < probabilities[MilitaryUnitOrder.LIGHTHUNTER.ordinal()] && armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size() > 0) {
								defenderType = MilitaryUnitOrder.LIGHTHUNTER.ordinal();
								shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size());
								defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].get(shipRandom));
								defender_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.LIGHTHUNTER.ordinal()];
							}
							else if (numRandom < probabilities[MilitaryUnitOrder.HEAVYHUNTER.ordinal()] && armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size() > 0) {
								defenderType = MilitaryUnitOrder.HEAVYHUNTER.ordinal();
								shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size());
								defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].get(shipRandom));
								defender_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.HEAVYHUNTER.ordinal()];
							}
							else if (numRandom < probabilities[MilitaryUnitOrder.BATTLESHIP.ordinal()] && armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].size() > 0) {
								defenderType = MilitaryUnitOrder.BATTLESHIP.ordinal();
								shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].size());
								defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].get(shipRandom));
								defender_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.BATTLESHIP.ordinal()];
							}
							else if (numRandom < probabilities[MilitaryUnitOrder.ARMOREDSHIP.ordinal()] && armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size() > 0) {
								defenderType = MilitaryUnitOrder.ARMOREDSHIP.ordinal();
								shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size());
								defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].get(shipRandom));
								defender_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.ARMOREDSHIP.ordinal()];
							}
							else if (numRandom < probabilities[MilitaryUnitOrder.MISSILELAUNCHER.ordinal()] && armies[numDefender][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].size() > 0) {
								defenderType = MilitaryUnitOrder.MISSILELAUNCHER.ordinal();
								shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].size());
								defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].get(shipRandom));
								defender_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.MISSILELAUNCHER.ordinal()];
							}
							else if (numRandom < probabilities[MilitaryUnitOrder.IONCANNON.ordinal()] && armies[numDefender][MilitaryUnitOrder.IONCANNON.ordinal()].size() > 0) {
								defenderType = MilitaryUnitOrder.IONCANNON.ordinal();
								shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.IONCANNON.ordinal()].size());
								defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.IONCANNON.ordinal()].get(shipRandom));
								defender_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.IONCANNON.ordinal()];
							}
							else if (numRandom < probabilities[MilitaryUnitOrder.PLASMACANNON.ordinal()] && armies[numDefender][MilitaryUnitOrder.PLASMACANNON.ordinal()].size() > 0) {
								defenderType = MilitaryUnitOrder.PLASMACANNON.ordinal();
								shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.PLASMACANNON.ordinal()].size());
								defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.PLASMACANNON.ordinal()].get(shipRandom));
								defender_name = MILITARY_UNIT_NAMES[MilitaryUnitOrder.PLASMACANNON.ordinal()];
							}
							
							numRandom = (int)(1 + Math.random() * 100);
						} while (defender == null);
						
						InterfaceController.instance.selectDefender(numDefender, defenderType);
						
						// The attack occurs
						battleDevelopment += "Attacks fleet enemy: " + attacker_name + " attacks " + defender_name + "\n";
						battleDevelopment += attacker_name + " generates the damage = " + attacker.attack() + "\n";
						defender.takeDamage(attacker.attack());
						battleDevelopment += defender_name + " stays with armor = " + defender.getActualArmor() + "\n";
							
						InterfaceController.instance.shootBullet(numAttacker, attackerType, defenderType);
						
						// If the defender loses its armor after the attack, it is randomly decided whether it generates waste and if it does, it is added to the wasteMetalDeuterium array
						if (defender.getActualArmor() <= 0) {
							numRandom = (int)(1 + Math.random() * 100);
							
							if (numRandom < defender.getChanceGeneratingWaste()) {
								wasteMetalDeuterium[0] += defender.getMetalCost() * PERCENTAGE_WASTE / 100;
								wasteMetalDeuterium[1] += defender.getDeuteriumCost() * PERCENTAGE_WASTE / 100;
							}
							
							// The value in metal and deuterium of the lost ship is added to planetDrops
							planetDrops[0] += defender.getMetalCost();
							planetDrops[1] += defender.getDeuteriumCost();
							
							// The ship is eliminated from the armies matrix
							armies[numDefender][defenderType].remove(defender); 
							InterfaceController.instance.removePlanetArmyUnit(defenderType, defender);
							
							// When the ArrayList arrives to 0, it converts to null, we create another empty ArrayList
							if (armies[numDefender][defenderType] == null) {
								armies[numDefender][defenderType] = new ArrayList<MilitaryUnit>();
							}
							
							// We update the defender's unit number, actual army and alive planet army percentage
							actualNumberUnitsPlanet = calculateUnitNumber(numDefender);
							actualArmyPlanet = calculateActualArmy(numDefender);
							
							battleDevelopment += "Enemy eliminates " + defender_name + "\n";
							
							percentageArmyPlanetAlive = (int)((100 * actualNumberUnitsPlanet / initialNumberUnitsPlanet));
						}
						
						if (percentageArmyPlanetAlive < 20) {
							break;
						}
						
						numRandom = (int)(1 + Math.random() * 100);
											
					} while (numRandom <= attacker.getChanceAttackAgain());
					
				}	
				order++; // The attacker is changed			
			} while (percentageArmyPlanetAlive >= 20 && percentageArmyEnemyAlive >= 20);
			
			InterfaceController.instance.hideSelectors();
			
			// We determine the resources' losses in metal, deuterium and weighed resources' losses
			resourcesLosses[0][0] = planetDrops[0];
			resourcesLosses[0][1] = planetDrops[1];
			resourcesLosses[0][2] = planetDrops[0] + 5 * planetDrops[1];
			
			resourcesLosses[1][0] = enemyDrops[0];
			resourcesLosses[1][1] = enemyDrops[1];
			resourcesLosses[1][2] = enemyDrops[0] + 5 * enemyDrops[1];
			
			System.out.println("Planet Losses: " + resourcesLosses[0][2] + " vs Enemy Losses: " + resourcesLosses[1][2]);
			
			// The number of battles that have happened in the planet is updated
			battles++;
			DatabaseController.instance.updateBattlesCounter(InterfaceController.instance.getPlanetId(), battles);
			
			// Whichever army has less weighed resources' losses, wins
			// If the planet wins, it recovers the waste generated
			if (resourcesLosses[0][2] < resourcesLosses[1][2]) {
				battleDevelopment += Printing.printStringCentred("PLAYER WINS!!", '=', 60) + "\n";
				
				InterfaceController.instance.collectRubble(wasteMetalDeuterium);
				InterfaceController.instance.showBattleWinner("PLAYER ARMY WINS!");

				num_battle = DatabaseController.instance.uploadBattleStats(InterfaceController.instance.getPlanetId(), wasteMetalDeuterium, true);
			}
			else {
				battleDevelopment += Printing.printStringCentred("ENEMY WINS!!", '=', 60) + "\n";
				InterfaceController.instance.showBattleWinner("ENEMY ARMY WINS!");
				num_battle = DatabaseController.instance.uploadBattleStats(InterfaceController.instance.getPlanetId(), wasteMetalDeuterium, false);
				
				defeats++; // If the planet looses, the defeats variable is updated
			}
			
			// The database data is updated
			DatabaseController.instance.updateRemainingUnits(armies, InterfaceController.instance.getPlanetId());
			
			// The planet's and enemy's armies are uploaded into the database
			DatabaseController.instance.uploadPlanetBattleDefense(InterfaceController.instance.getPlanetId(), num_battle, initialArmies, armies);
			DatabaseController.instance.uploadPlanetBattleArmy(InterfaceController.instance.getPlanetId(), num_battle, initialArmies, armies);
			DatabaseController.instance.uploadEnemyArmy(InterfaceController.instance.getPlanetId(), num_battle, initialArmies, armies);
			DatabaseController.instance.uploadBattleLog(InterfaceController.instance.getPlanetId(), num_battle, battleDevelopment);
			
			// An xml file is generated with this data and then converted into html
			saved_xml = DatabaseController.instance.convertIntoXML(InterfaceController.instance.getPlanetId(),num_battle);
			new XSLTransformer(saved_xml, num_battle);
			
			System.out.println(DatabaseController.instance.getBattleLog(InterfaceController.instance.getPlanetId(), num_battle));
			System.out.println(DatabaseController.instance.getBattleSummary(InterfaceController.instance.getPlanetId(), num_battle));
			
//			System.out.println(getBattleDevelopment());
//			System.out.println(getBattleReport(battles));

		} // If the planet's units were 0 at the beginning of the battle, we skip directly to this block
		else {
			// The number of battles that have happened in the planet is updated
			battles++;
			DatabaseController.instance.updateBattlesCounter(InterfaceController.instance.getPlanetId(), battles);
			
			battleDevelopment += "We didn't have ships! We automatically lost.\n";
			battleDevelopment += Printing.printStringCentred("ENEMY WINS!!", '=', 60) + "\n";
			InterfaceController.instance.showBattleWinner("ENEMY ARMY WINS!");
			num_battle = DatabaseController.instance.uploadBattleStats(InterfaceController.instance.getPlanetId(), wasteMetalDeuterium, false);
				
			defeats++; // The defeats variable is updated
			
			// The database data is updated
			DatabaseController.instance.updateRemainingUnits(armies, InterfaceController.instance.getPlanetId());
			
			// The planet's and enemy's armies are uploaded into the database
			DatabaseController.instance.uploadPlanetBattleDefense(InterfaceController.instance.getPlanetId(), num_battle, initialArmies, armies);
			DatabaseController.instance.uploadPlanetBattleArmy(InterfaceController.instance.getPlanetId(), num_battle, initialArmies, armies);
			DatabaseController.instance.uploadEnemyArmy(InterfaceController.instance.getPlanetId(), num_battle, initialArmies, armies);
			DatabaseController.instance.uploadBattleLog(InterfaceController.instance.getPlanetId(), num_battle, battleDevelopment);
			
			// An xml file is generated with this data and then converted into html
			saved_xml = DatabaseController.instance.convertIntoXML(InterfaceController.instance.getPlanetId(),num_battle);
			new XSLTransformer(saved_xml, num_battle);
			
			System.out.println(DatabaseController.instance.getBattleLog(InterfaceController.instance.getPlanetId(), num_battle));
			System.out.println(DatabaseController.instance.getBattleSummary(InterfaceController.instance.getPlanetId(), num_battle));
			
//			System.out.println(getBattleDevelopment());
//			System.out.println(getBattleReport(battles));
		}

		
		// If the planet has been defeated 3 times, the game is over
		if (defeats == 3) {
			
			System.err.println("=============DERROTA=============");
			
			JOptionPane.showMessageDialog(null, InterfaceController.instance.getNamePlanet()+" has been conquered!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
			
			System.exit(0);
		}
	}
	
	// This method resets the armor of the remaining planet's ships
	public void resetUnitsArmor(ArrayList[][] armies) {
		for (int i = 0; i < armies[0].length; i++) {
			for (int j = 0; j < armies[0][i].size(); j++) {
				if (armies[0][i].get(j) != null) {
					((MilitaryUnit) armies[0][i].get(j)).resetArmor();
				}
			}
		}
	}
	
	
	// These methods print the battle summary and battleDevelopment (using the attributes of this class, not the database data)
	// Summary report
	public String getBattleReport(int battles) {
		String summary = "BATTLE NUMBER: " + battles + "\n" + "\n" + "BATTLE STATISTICS" + "\n" + "\n";
		
		summary += String.format("%-27s%10s%10s      %-27s%10s%10s", "PLANET ARMY", "Units", "Drops", "ENEMY ARMY","Units","Drops") + "\n" + "\n";
		summary += String.format("%-27s%10d%10d      %-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.LIGHTHUNTER.ordinal()],initialArmies[0][MilitaryUnitOrder.LIGHTHUNTER.ordinal()],initialArmies[0][MilitaryUnitOrder.LIGHTHUNTER.ordinal()]-actualArmyPlanet[MilitaryUnitOrder.LIGHTHUNTER.ordinal()],MILITARY_UNIT_NAMES[MilitaryUnitOrder.LIGHTHUNTER.ordinal()],initialArmies[1][MilitaryUnitOrder.LIGHTHUNTER.ordinal()],initialArmies[1][MilitaryUnitOrder.LIGHTHUNTER.ordinal()]-actualArmyEnemy[MilitaryUnitOrder.LIGHTHUNTER.ordinal()]) + "\n";
		summary += String.format("%-27s%10d%10d      %-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.HEAVYHUNTER.ordinal()],initialArmies[0][MilitaryUnitOrder.HEAVYHUNTER.ordinal()],initialArmies[0][MilitaryUnitOrder.HEAVYHUNTER.ordinal()]-actualArmyPlanet[MilitaryUnitOrder.HEAVYHUNTER.ordinal()],MILITARY_UNIT_NAMES[MilitaryUnitOrder.HEAVYHUNTER.ordinal()],initialArmies[1][MilitaryUnitOrder.HEAVYHUNTER.ordinal()],initialArmies[1][MilitaryUnitOrder.HEAVYHUNTER.ordinal()]-actualArmyEnemy[MilitaryUnitOrder.HEAVYHUNTER.ordinal()]) + "\n";
		summary += String.format("%-27s%10d%10d      %-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.BATTLESHIP.ordinal()],initialArmies[0][MilitaryUnitOrder.BATTLESHIP.ordinal()],initialArmies[0][MilitaryUnitOrder.BATTLESHIP.ordinal()]-actualArmyPlanet[MilitaryUnitOrder.BATTLESHIP.ordinal()],MILITARY_UNIT_NAMES[MilitaryUnitOrder.BATTLESHIP.ordinal()],initialArmies[1][MilitaryUnitOrder.BATTLESHIP.ordinal()],initialArmies[1][MilitaryUnitOrder.BATTLESHIP.ordinal()]-actualArmyEnemy[MilitaryUnitOrder.BATTLESHIP.ordinal()]) + "\n";
		summary += String.format("%-27s%10d%10d      %-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.ARMOREDSHIP.ordinal()],initialArmies[0][MilitaryUnitOrder.ARMOREDSHIP.ordinal()],initialArmies[0][MilitaryUnitOrder.ARMOREDSHIP.ordinal()]-actualArmyPlanet[MilitaryUnitOrder.ARMOREDSHIP.ordinal()],MILITARY_UNIT_NAMES[MilitaryUnitOrder.ARMOREDSHIP.ordinal()],initialArmies[1][MilitaryUnitOrder.ARMOREDSHIP.ordinal()],initialArmies[1][MilitaryUnitOrder.ARMOREDSHIP.ordinal()]-actualArmyEnemy[MilitaryUnitOrder.ARMOREDSHIP.ordinal()]) + "\n";
		summary += String.format("%-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.MISSILELAUNCHER.ordinal()],initialArmies[0][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()],initialArmies[0][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()]-actualArmyPlanet[MilitaryUnitOrder.MISSILELAUNCHER.ordinal()]) + "\n";
		summary += String.format("%-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.IONCANNON.ordinal()],initialArmies[0][MilitaryUnitOrder.IONCANNON.ordinal()],initialArmies[0][MilitaryUnitOrder.IONCANNON.ordinal()]-actualArmyPlanet[MilitaryUnitOrder.IONCANNON.ordinal()]) + "\n";
		summary += String.format("%-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.PLASMACANNON.ordinal()],initialArmies[0][MilitaryUnitOrder.PLASMACANNON.ordinal()],initialArmies[0][MilitaryUnitOrder.PLASMACANNON.ordinal()]-actualArmyPlanet[MilitaryUnitOrder.PLASMACANNON.ordinal()]) + "\n" + "\n";
		
		
		summary += Printing.printLineChar('*', 100);
		summary += String.format("%-47s      %-47s", "Cost Planet Army", "Cost Enemy Army") + "\n" + "\n";
		summary += String.format("%-15s%15d%23s%-15s%15d", "Metal:", initialCostFleet[0][0], "", "Metal:",initialCostFleet[1][0])+"\n";
		summary += String.format("%-15s%15d%23s%-15s%15d", "Deuterium:", initialCostFleet[0][1], "", "Deuterium:",initialCostFleet[1][1])+"\n"+"\n";
		
		summary += Printing.printLineChar('*', 100);
		summary += String.format("%-47s      %-47s", "Losses Planet Army", "Losses Enemy Army") + "\n" + "\n";
		summary += String.format("%-15s%15d%23s%-15s%15d", "Metal:", resourcesLosses[0][0], "", "Metal:",resourcesLosses[1][0])+"\n";
		summary += String.format("%-15s%15d%23s%-15s%15d", "Deuterium:", resourcesLosses[0][1], "", "Deuterium:",resourcesLosses[1][1])+"\n";
		summary += String.format("%-15s%15d%23s%-15s%15d", "Weighted:", resourcesLosses[0][2], "", "Weighted:",resourcesLosses[1][2])+"\n"+"\n";
		
		summary += Printing.printLineChar('*', 100);
		summary += "Waste Generated:\n";
		summary += String.format("%-15s%15d", "Metal:", wasteMetalDeuterium[0])+"\n";
		summary += String.format("%-15s%15d", "Deuterium:", wasteMetalDeuterium[1])+"\n";
		
		if (resourcesLosses[0][2] < resourcesLosses[1][2]) {
			summary += "\n" + "Battle winned by PLANET. We collect rubble." + "\n";
		}
		else {
			summary += "\n" + "Battle winned by ENEMY. We do not collect rubble." + "\n";
			summary += "\n" + Printing.printStringCentred("ENEMY WINS!!", '=', 60);
		}
		
		return summary;
		
	}
	
	// battleDevelopment report
	public String getBattleDevelopment() {
		return battleDevelopment;
	}
	
}