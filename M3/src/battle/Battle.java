package battle;

import java.util.ArrayList;

import controllers.DatabaseController;
import controllers.InterfaceController;
import ships.MilitaryUnit;
import utils.Printing;
import utils.Variables;

public class Battle implements Variables {
	
	private int battles; // Podemos recuperar este número de la base de datos (permitiremos mostrar el report resumen de la batalla que quieras pero el battleDevelopment solo de las ultimas 5 batallas)
	private int num_battle;
	
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
		
	}
	
	public int getBattles() {
		return battles;
	}

	public void createBattle(ArrayList<MilitaryUnit>[] planetArmy, ArrayList<MilitaryUnit>[] enemyArmy) {
		this.num_battle = 0;
		
		this.planetArmy = planetArmy;
		this.enemyArmy = enemyArmy;
		
		// Juntamos en una variable ambos ejércitos
		this.armies = new ArrayList[2][7];
		this.armies[0] = planetArmy;
		this.armies[1] = enemyArmy;
		
		this.battleDevelopment = "";
		
		// Calculamos los costes de cada ejército
		this.initialCostFleet = new int[2][2];
		calculateInitialCostFleet();
		
		// Calculamos las unidades iniciales de cada ejército
		this.initialNumberUnitsPlanet = calculateUnitNumber(0);
		this.initialNumberUnitsEnemy = calculateUnitNumber(1);
		this.actualNumberUnitsPlanet = this.initialNumberUnitsPlanet;
		this.actualNumberUnitsEnemy = this.initialNumberUnitsEnemy;
		
		this.wasteMetalDeuterium = new int[2];
		this.planetDrops = new int[2];
		this.enemyDrops = new int[2];
		this.resourcesLosses = new int[2][3];
		
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
		 * 1. Recorremos cada uno de los ejercitos
		 * 2. Sumamos el valor en metal y deuterio de cada unidad
		 * 3. Añadimos a la matriz de costes iniciales
		 */
		
		int metal = 0, deuterium = 0;
		
		for (int i = 0; i < armies.length; i++) {
			for (int j = 0; j < armies[i].length; j++) {
				// Comprobamos que haya alguna unidad para calcular su coste
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
	
	private void calculateInitialArmy() {
		
		for (int i = 0; i < armies.length; i++) {
			for (int j = 0; j < armies[i].length; j++) {
				initialArmies[i][j] = armies[i][j].size();
			}
		}
	}
	
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
	
	public void startBattle() {
		
		battles++;
		
		//DatabaseController.instance.updateBattlesCounter(InterfaceController.instance.getPlanetId(), battles);
		
		int order = (int)(Math.random() * 2);
		
		int percentageArmyPlanetAlive = (int)((actualNumberUnitsPlanet / initialNumberUnitsPlanet) * 100);
		int percentageArmyEnemyAlive = (int)((actualNumberUnitsEnemy / initialNumberUnitsEnemy) * 100);
		
		battleDevelopment = Printing.printStringCentred("THE BATTLE STARTS", '*', 60) + "\n";
		
		do {
			battleDevelopment += Printing.printStringCentred("CHANGE ATTACKER", '*', 60) + "\n";
			
			MilitaryUnit attacker = null;
			MilitaryUnit defender = null;
			String attacker_name = null;
			String defender_name = null;
			int attackerType = 0;
			int defenderType = 0;
			
			int[] probabilities;
			
			if (order % 2 == 0) {
				int numAttacker = 0, numDefender = 1;
				int numRandom;
				probabilities = new int[7];
				probabilities[0] = CHANCE_ATTACK_PLANET_UNITS[0];
				int shipRandom = 0;
				
				for (int i = 1; i < probabilities.length; i++) {
					probabilities[i] = probabilities[i - 1] + CHANCE_ATTACK_PLANET_UNITS[i];
				}
				
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
				
				do {
					numRandom = (int)(1 + Math.random() * 100);
					probabilities = new int[4];
										
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
					
					battleDevelopment += "Attacks Planet: " + attacker_name + " attacks " + defender_name + "\n";
					battleDevelopment += attacker_name + " generates the damage = " + attacker.attack() + "\n";
					defender.takeDamage(attacker.attack());
					battleDevelopment += defender_name + " stays with armor = " + defender.getActualArmor() + "\n";
					
					InterfaceController.instance.shootBullet(numAttacker, attackerType, defenderType);
					
					if (defender.getActualArmor() <= 0) {
						numRandom = (int)(1 + Math.random() * 100);
						
						if (numRandom < defender.getChanceGeneratingWaste()) {
							wasteMetalDeuterium[0] += defender.getMetalCost() * PERCENTAGE_WASTE / 100;
							wasteMetalDeuterium[1] += defender.getDeuteriumCost() * PERCENTAGE_WASTE / 100;
						}
						
						enemyDrops[0] += defender.getMetalCost();
						enemyDrops[1] += defender.getDeuteriumCost();
						
						armies[numDefender][defenderType].remove(defender);
						InterfaceController.instance.removeEnemyArmyUnit(defenderType, defender);
						
						// For any reason, when an enemy ArrayList arrives to 0, it converts in null, we create another empty ArrayList
						if (armies[numDefender][defenderType] == null) {
							armies[numDefender][defenderType] = new ArrayList<MilitaryUnit>();
						}

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
				
				
							
			}
			else {
				int numAttacker = 1, numDefender = 0;
				int numRandom;
				int shipRandom = 0;
				
				probabilities = new int[4];
				probabilities[0] = CHANCE_ATTACK_ENEMY_UNITS[0];
				
				for (int i = 1; i < probabilities.length; i++) {
					probabilities[i] = probabilities[i - 1] + CHANCE_ATTACK_ENEMY_UNITS[i];
				}
				
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
				
				do {
					numRandom = (int)(1 + Math.random() * 100);

					probabilities = new int[7];
					probabilities[0] = (int)(100 * armies[numDefender][0].size() / actualNumberUnitsPlanet);
					
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
					
					battleDevelopment += "Attacks fleet enemy: " + attacker_name + " attacks " + defender_name + "\n";
					battleDevelopment += attacker_name + " generates the damage = " + attacker.attack() + "\n";
					defender.takeDamage(attacker.attack());
					battleDevelopment += defender_name + " stays with armor = " + defender.getActualArmor() + "\n";
						
					InterfaceController.instance.shootBullet(numAttacker, attackerType, defenderType);
					
					if (defender.getActualArmor() <= 0) {
						numRandom = (int)(1 + Math.random() * 100);
						
						if (numRandom < defender.getChanceGeneratingWaste()) {
							wasteMetalDeuterium[0] += defender.getMetalCost() * PERCENTAGE_WASTE / 100;
							wasteMetalDeuterium[1] += defender.getDeuteriumCost() * PERCENTAGE_WASTE / 100;
						}
						
						planetDrops[0] += defender.getMetalCost();
						planetDrops[1] += defender.getDeuteriumCost();
						
						
						armies[numDefender][defenderType].remove(defender); // A veces peta 
						InterfaceController.instance.removePlanetArmyUnit(defenderType, defender);

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
			order++;			
		} while (percentageArmyPlanetAlive >= 20 && percentageArmyEnemyAlive >= 20);
		
		InterfaceController.instance.hideSelectors();
		
		resourcesLosses[0][0] = planetDrops[0];
		resourcesLosses[0][1] = planetDrops[1];
		resourcesLosses[0][2] = planetDrops[0] + 5 * planetDrops[1];
		
		resourcesLosses[1][0] = enemyDrops[0];
		resourcesLosses[1][1] = enemyDrops[1];
		resourcesLosses[1][2] = enemyDrops[0] + 5 * enemyDrops[1];
		
		System.out.println("Planet Losses: " + resourcesLosses[0][2] + " vs Enemy Losses: " + resourcesLosses[1][2]);
		
		if (resourcesLosses[0][2] <= resourcesLosses[1][2]) {
			battleDevelopment += Printing.printStringCentred("PLAYER WINS!!", '=', 60) + "\n";
			
			InterfaceController.instance.collectRubble(wasteMetalDeuterium);
			InterfaceController.instance.showBattleWinner("PLAYER ARMY WINS!");

			num_battle = DatabaseController.instance.uploadBattleStats(InterfaceController.instance.getPlanetId(), wasteMetalDeuterium, true);
		}
		else {
			battleDevelopment += Printing.printStringCentred("ENEMY WINS!!", '=', 60) + "\n";
			InterfaceController.instance.showBattleWinner("ENEMY ARMY WINS!");
			num_battle = DatabaseController.instance.uploadBattleStats(InterfaceController.instance.getPlanetId(), wasteMetalDeuterium, false);

		}
		
		//DatabaseController.instance.updateRemainingUnits(armies, InterfaceController.instance.getPlanetId());
		

		DatabaseController.instance.uploadPlanetBattleDefense(InterfaceController.instance.getPlanetId(), num_battle, initialArmies, armies);
		DatabaseController.instance.uploadPlanetBattleArmy(InterfaceController.instance.getPlanetId(), num_battle, initialArmies, armies);
		DatabaseController.instance.uploadEnemyArmy(InterfaceController.instance.getPlanetId(), num_battle, initialArmies, armies);
		DatabaseController.instance.uploadBattleLog(InterfaceController.instance.getPlanetId(), num_battle, battleDevelopment);
		
		DatabaseController.instance.convertIntoXML(InterfaceController.instance.getPlanetId(),num_battle);
		
		System.out.println(getBattleDevelopment());
		System.out.println(getBattleReport(battles));
	}
	
	// FUNCIÓN RESETEAR ARMADURA UNIDADES PLANETA
	
	// FUNCIÓN REPORTE DE LA BATALLA
	// Reporte resumen
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
		
		if (resourcesLosses[0][2] <= resourcesLosses[1][2]) {
			summary += "\n" + "Battle winned by PLANET. We collect rubble." + "\n";
		}
		else {
			summary += "\n" + "Battle winned by ENEMY. We do not collect rubble." + "\n";
			summary += "\n" + Printing.printStringCentred("ENEMY WINS!!", '=', 60);
		}
		
		return summary;
		
	}
	
	// Reporte BattleDevelopment
	public String getBattleDevelopment() {
		return battleDevelopment;
	}
	
}