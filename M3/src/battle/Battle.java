package battle;

import java.util.ArrayList;

import ships.MilitaryUnit;

public class Battle {
	
	private int battleNumber;

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
	
	public Battle(ArrayList<MilitaryUnit>[] planetArmy, ArrayList<MilitaryUnit>[] enemyArmy) {
		super();
		
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
			for (int j = 0; j < armies[j].length; j++) {
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
		
		int percentageArmyPlanetAlive = (int)(actualNumberUnitsPlanet / initialNumberUnitsPlanet) * 100;
		int percentageArmyEnemyAlive = (int)(actualNumberUnitsEnemy / initialNumberUnitsEnemy) * 100;
		
		do {
			
			if (order % 2 == 0) {
				int numRandom = (int)(1 + Math.random() * 100);
				MilitaryUnit attacker = null;
			}
			
			order++;
			
			
		} while (percentageArmyPlanetAlive > 20 && percentageArmyEnemyAlive > 20);
	}
}
