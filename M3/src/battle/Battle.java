package battle;

import java.util.ArrayList;
import java.util.Arrays;

import gui.EnemyPanel;
import gui.PlayerPanel;
import ships.MilitaryUnit;
import utils.Printing;
import utils.Variables;

public class Battle implements Variables {
	
	private int battleNumber;
	
	private PlayerPanel playerPanel;
	private EnemyPanel enemyPanel;

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
	
	public Battle(ArrayList<MilitaryUnit>[] planetArmy, ArrayList<MilitaryUnit>[] enemyArmy, PlayerPanel playerPanel, EnemyPanel enemyPanel) {
		super();
		
		this.planetArmy = planetArmy;
		this.enemyArmy = enemyArmy;
		
		this.playerPanel = playerPanel;
		this.enemyPanel = enemyPanel;
		
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
	}
	
	public ArrayList<MilitaryUnit>[] getPlanetArmy() {
		return armies[0];
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
			total +=  armies[armyNumber][i].size();
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
			actualArmy[i] =  armies[armyNumber][i].size();
		}
		
		return actualArmy;
	}
	
	public void startBattle() {
		
		int order = (int)(Math.random() * 2);
		
		int percentageArmyPlanetAlive = (int)((actualNumberUnitsPlanet / initialNumberUnitsPlanet) * 100);
		int percentageArmyEnemyAlive = (int)((actualNumberUnitsEnemy / initialNumberUnitsEnemy) * 100);
		
		do {
			battleDevelopment += "*****************CHANGE ATTACKER*****************\n";
			
			MilitaryUnit attacker = null;
			MilitaryUnit defender = null;
			int[] probabilities;
			
			if (order % 2 == 0) {
				int numAttacker = 0, numDefender = 1;
				int numRandom = (int)(1 + Math.random() * 100);
				probabilities = new int[7];
				probabilities[0] = CHANCE_ATTACK_PLANET_UNITS[0];
				int type = 0;
				int shipRandom = 0;
				
				for (int i = 1; i < probabilities.length; i++) {
					probabilities[i] = probabilities[i - 1] + CHANCE_ATTACK_PLANET_UNITS[i];
				}
				
				do {
					if (numRandom < probabilities[MilitaryUnitOrder.LIGHTHUNTER.ordinal()] && armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size() > 0) {
						shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size());
						attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].get(shipRandom));
					}
					else if (numRandom < probabilities[MilitaryUnitOrder.HEAVYHUNTER.ordinal()] && armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size() > 0) {
						shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size());
						attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].get(shipRandom));
					}
					else if (numRandom < probabilities[MilitaryUnitOrder.BATTLESHIP.ordinal()] && armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].size() > 0) {
						shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].size());
						attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].get(shipRandom));
					}
					else if (numRandom < probabilities[MilitaryUnitOrder.ARMOREDSHIP.ordinal()] && armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size() > 0) {
						shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size());
						attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].get(shipRandom));
					}
					else if (numRandom < probabilities[MilitaryUnitOrder.MISSILELAUNCHER.ordinal()] && armies[numAttacker][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].size() > 0) {
						shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].size());
						attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].get(shipRandom));
					}
					else if (numRandom < probabilities[MilitaryUnitOrder.IONCANNON.ordinal()] && armies[numAttacker][MilitaryUnitOrder.IONCANNON.ordinal()].size() > 0) {
						shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.IONCANNON.ordinal()].size());
						attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.IONCANNON.ordinal()].get(shipRandom));
					}
					else if (numRandom < probabilities[MilitaryUnitOrder.PLASMACANNON.ordinal()] && armies[numAttacker][MilitaryUnitOrder.PLASMACANNON.ordinal()].size() > 0) {
						shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.PLASMACANNON.ordinal()].size());
						attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.PLASMACANNON.ordinal()].get(shipRandom));
					}
					numRandom = (int)(1 + Math.random() * 100);
				} while (attacker == null);
				
				do {
					numRandom = (int)(1 + Math.random() * 100);
					probabilities = new int[4];
					probabilities[0] = (int)(100 * armies[numDefender][0].size() / actualNumberUnitsEnemy);
					
					for (int i = 1; i < probabilities.length; i++) {
						probabilities[i] = probabilities[i - 1] + (int)(100 * armies[numDefender][i].size() / actualNumberUnitsEnemy);
					}
					
					do {
						if (numRandom <= probabilities[MilitaryUnitOrder.LIGHTHUNTER.ordinal()] && armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size() > 0) {
							type = MilitaryUnitOrder.LIGHTHUNTER.ordinal();
							shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size());
							defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].get(shipRandom));
						}
						else if (numRandom <= probabilities[MilitaryUnitOrder.HEAVYHUNTER.ordinal()] && armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size() > 0) {
							type = MilitaryUnitOrder.HEAVYHUNTER.ordinal();
							shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size());
							defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].get(shipRandom));
						}
						else if (numRandom <= probabilities[MilitaryUnitOrder.BATTLESHIP.ordinal()] && armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].size() > 0) {
							type = MilitaryUnitOrder.BATTLESHIP.ordinal();
							shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].size());
							defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].get(shipRandom));
						}
						else if (numRandom <= probabilities[MilitaryUnitOrder.ARMOREDSHIP.ordinal()] && armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size() > 0) {
							type = MilitaryUnitOrder.ARMOREDSHIP.ordinal();
							shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size());
							defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].get(shipRandom));
						}
						numRandom = (int)(1 + Math.random() * 100);
					} while (defender == null);
										
					battleDevelopment += "Attacks Planet: " + attacker.getClass().getName() + " attacks " + defender.getClass().getName() + "\n";
					battleDevelopment += attacker.getClass().getName() + " generates the damage = " + attacker.attack() + "\n";
					defender.takeDamage(attacker.attack());
					battleDevelopment += defender.getClass().getName() + " stays with armor = " + defender.getActualArmor() + "\n";
					
					if (defender.getActualArmor() <= 0) {
						numRandom = (int)(1 + Math.random() * 100);
						
						if (numRandom < defender.getChanceGeneratingWaste()) {
							wasteMetalDeuterium[0] += defender.getMetalCost() * PERCENTAGE_WASTE / 100;
							wasteMetalDeuterium[1] += defender.getDeuteriumCost() * PERCENTAGE_WASTE / 100;
						}
						
						enemyDrops[0] += defender.getMetalCost();
						enemyDrops[1] += defender.getDeuteriumCost();
						
						
						armies[numDefender][type].remove(shipRandom);

						actualNumberUnitsEnemy = calculateUnitNumber(numDefender);
						
						battleDevelopment += "We eliminate" + defender.getClass().getName() + "\n";
					}
					
					if (actualNumberUnitsEnemy == 0) {
						break;
					}
					
					numRandom = (int)(1 + Math.random() * 100);
					
				} while (numRandom <= attacker.getChanceAttackAgain());
				
				
				percentageArmyEnemyAlive = (int)((100 * actualNumberUnitsEnemy / initialNumberUnitsEnemy));
				System.out.println("Initial: " + initialNumberUnitsEnemy + " - Alive: " + actualNumberUnitsEnemy + " - Percentage: " + percentageArmyEnemyAlive);
				
			}
			else {
				int numAttacker = 1, numDefender = 0;
				int numRandom = (int)(1 + Math.random() * 100);
				int type = 0;
				int shipRandom = 0;
				
				probabilities = new int[4];
				probabilities[0] = CHANCE_ATTACK_ENEMY_UNITS[0];
				
				for (int i = 1; i < probabilities.length; i++) {
					probabilities[i] = probabilities[i - 1] + CHANCE_ATTACK_ENEMY_UNITS[i];
				}
				
				do {					
					if (numRandom <= probabilities[MilitaryUnitOrder.LIGHTHUNTER.ordinal()] && armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size() > 0) {
						shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size());
						attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].get(shipRandom));
					}
					else if (numRandom <= probabilities[MilitaryUnitOrder.HEAVYHUNTER.ordinal()] && armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size() > 0) {
						shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size());
						attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].get(shipRandom));
					}
					else if (numRandom <= probabilities[MilitaryUnitOrder.BATTLESHIP.ordinal()] && armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].size() > 0) {
						shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].size());
						attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.BATTLESHIP.ordinal()].get(shipRandom));
					}
					else if (numRandom <= probabilities[MilitaryUnitOrder.ARMOREDSHIP.ordinal()] && armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size() > 0) {
						shipRandom = (int)(Math.random() * armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size());
						attacker = ((MilitaryUnit) armies[numAttacker][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].get(shipRandom));
					}
					
					numRandom = (int)(1 + Math.random() * 100);
				} while (attacker == null);
				
				do {
					numRandom = (int)(1 + Math.random() * 100);

					probabilities = new int[7];
					probabilities[0] = (int)(100 * armies[numDefender][0].size() / actualNumberUnitsPlanet);
					
					for (int i = 1; i < probabilities.length; i++) {
						probabilities[i] = probabilities[i - 1] + (int)(100 * armies[numDefender][i].size() / actualNumberUnitsPlanet);
					}
					
					do {
						if (numRandom < probabilities[MilitaryUnitOrder.LIGHTHUNTER.ordinal()] && armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size() > 0) {
							type = MilitaryUnitOrder.LIGHTHUNTER.ordinal();
							shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].size());
							defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.LIGHTHUNTER.ordinal()].get(shipRandom));
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.HEAVYHUNTER.ordinal()] && armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size() > 0) {
							type = MilitaryUnitOrder.HEAVYHUNTER.ordinal();
							shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].size());
							defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.HEAVYHUNTER.ordinal()].get(shipRandom));
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.BATTLESHIP.ordinal()] && armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].size() > 0) {
							type = MilitaryUnitOrder.BATTLESHIP.ordinal();
							shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].size());
							defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.BATTLESHIP.ordinal()].get(shipRandom));
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.ARMOREDSHIP.ordinal()] && armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size() > 0) {
							type = MilitaryUnitOrder.ARMOREDSHIP.ordinal();
							shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].size());
							defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.ARMOREDSHIP.ordinal()].get(shipRandom));
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.MISSILELAUNCHER.ordinal()] && armies[numDefender][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].size() > 0) {
							type = MilitaryUnitOrder.MISSILELAUNCHER.ordinal();
							shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].size());
							defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.MISSILELAUNCHER.ordinal()].get(shipRandom));
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.IONCANNON.ordinal()] && armies[numDefender][MilitaryUnitOrder.IONCANNON.ordinal()].size() > 0) {
							type = MilitaryUnitOrder.IONCANNON.ordinal();
							shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.IONCANNON.ordinal()].size());
							defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.IONCANNON.ordinal()].get(shipRandom));
						}
						else if (numRandom < probabilities[MilitaryUnitOrder.PLASMACANNON.ordinal()] && armies[numDefender][MilitaryUnitOrder.PLASMACANNON.ordinal()].size() > 0) {
							type = MilitaryUnitOrder.PLASMACANNON.ordinal();
							shipRandom = (int)(Math.random() * armies[numDefender][MilitaryUnitOrder.PLASMACANNON.ordinal()].size());
							defender = ((MilitaryUnit) armies[numDefender][MilitaryUnitOrder.PLASMACANNON.ordinal()].get(shipRandom));
						}
						
						numRandom = (int)(1 + Math.random() * 100);
					} while (defender == null);
					
					battleDevelopment += "Attacks fleet enemy: " + attacker.getClass().getName() + " attacks " + defender.getClass().getName() + "\n";
					battleDevelopment += attacker.getClass().getName() + " generates the damage = " + attacker.attack() + "\n";
					defender.takeDamage(attacker.attack());
					battleDevelopment += defender.getClass().getName() + " stays with armor = " + defender.getActualArmor() + "\n";

					
					if (defender.getActualArmor() <= 0) {
						numRandom = (int)(1 + Math.random() * 100);
						
						if (numRandom < defender.getChanceGeneratingWaste()) {
							wasteMetalDeuterium[0] += defender.getMetalCost() * PERCENTAGE_WASTE / 100;
							wasteMetalDeuterium[1] += defender.getDeuteriumCost() * PERCENTAGE_WASTE / 100;
						}
						
						planetDrops[0] += defender.getMetalCost();
						planetDrops[1] += defender.getDeuteriumCost();
						
						
						armies[numDefender][type].remove(shipRandom);

						actualNumberUnitsPlanet = calculateUnitNumber(numDefender);
						
						battleDevelopment += "Enemy eliminates" + defender.getClass().getName() + "\n";
					}
					
					if (actualNumberUnitsPlanet == 0) {
						break;
					}
					
					numRandom = (int)(1 + Math.random() * 100);
										
				} while (numRandom <= attacker.getChanceAttackAgain());
				
				percentageArmyPlanetAlive = (int)((100 * actualNumberUnitsPlanet / initialNumberUnitsPlanet));
				
				System.out.println("Initial: " + initialNumberUnitsPlanet + " - Alive: " + actualNumberUnitsPlanet + " - Percentage: " + percentageArmyPlanetAlive);
			}	
			order++;			
		} while (percentageArmyPlanetAlive > 20 && percentageArmyEnemyAlive > 20);
		
		resourcesLosses[0][0] = planetDrops[0];
		resourcesLosses[0][1] = planetDrops[1];
		resourcesLosses[0][2] = planetDrops[0] + 5 * planetDrops[1];
		
		resourcesLosses[1][0] = enemyDrops[0];
		resourcesLosses[1][1] = enemyDrops[1];
		resourcesLosses[1][2] = enemyDrops[0] + 5 * enemyDrops[1];
		
		System.out.println("Planet Losses: " + resourcesLosses[0][2] + " vs Enemy Losses: " + resourcesLosses[1][2]);
		
		if (resourcesLosses[0][2] <= resourcesLosses[1][2]) {
			battleDevelopment += "Player Wins";
		}
		else {
			battleDevelopment += "Enemy Wins";
		}
		
		System.out.println(battleDevelopment);
	}
	
	// FUNCIÓN RESETEAR ARMADURA UNIDADES PLANETA
	
	// FUNCIÓN REPORTE DE LA BATALLA
	
	// FUNCIÓN QUE ACTUALICE LA BBDD
	
	// FUNCIÓN PARA PASAR EL REPORTE ENTERO (JUNTO CON LAS BATTLEDEVELOPMENT) A XML
	
	// FUNCIÓN PARA PASAR EL XML A HTML
}
