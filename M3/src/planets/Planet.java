package planets;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import controllers.DatabaseController;
import controllers.InterfaceController;
import exceptions.ResourceException;
import ships.ArmoredShip;
import ships.BattleShip;
import ships.HeavyHunter;
import ships.IonCannon;
import ships.LightHunter;
import ships.MilitaryUnit;
import ships.MissileLauncher;
import ships.PlasmaCannon;
import utils.Printing;
import utils.Variables;

public class Planet implements Variables {

	private final int INIT_TECHNOLOGY_LEVEL = 0;
	private final int INIT_METAL = METAL_BASE_PLANET_ARMY;
	private final int INIT_DEUTERIUM = DEUTERIUM_BASE_PLANET_ARMY;
	private final int MAX_LINE_SIZE = 64;
	private final int MAX_NUMBER_SIZE = 10;
	
	private int technologyDefense;
	private int technologyAttack;
	private int metal;
	private int deuterium;
	private int upgradeDefenseTechnologyDeuteriumCost;
	private int upgradeAttackTechnologyDeuteriumCost;
	private ArrayList<MilitaryUnit>[] army;
	
	private int planet_id;
	
	public Planet() {
		super();
		
		this.technologyDefense = INIT_TECHNOLOGY_LEVEL;
		this.technologyAttack = INIT_TECHNOLOGY_LEVEL;
		this.metal = INIT_METAL;
		this.deuterium = INIT_DEUTERIUM;
		this.upgradeDefenseTechnologyDeuteriumCost = Variables.UPGRADE_BASE_DEFENSE_TECHNOLOGY_DEUTERIUM_COST;
		this.upgradeAttackTechnologyDeuteriumCost = Variables.UPGRADE_BASE_ATTACK_TECHNOLOGY_DEUTERIUM_COST;
		this.army = new ArrayList[PLANET_ARMY_LENGHT];
		
		for (int i = 0; i < PLANET_ARMY_LENGHT; i++) {
			army[i] = new ArrayList<MilitaryUnit>();
		}
		
		System.out.println("Estoy en planet y mi nombre es "+InterfaceController.instance.getNamePlanet());
		DatabaseController.instance.newPlanet(this,InterfaceController.instance.getNamePlanet());
		
		generateResources();
	}
	
	public int getTechnologyDefense() {
		return technologyDefense;
	}
	public int getTechnologyAttack() {
		return technologyAttack;
	}
	public int getMetal() {
		return metal;
	}
	public int getDeuterium() {
		return deuterium;
	}
	
	public void setMetal(int metal) {
		this.metal = metal;
	}

	public void setDeuterium(int deuterium) {
		this.deuterium = deuterium;
	}

	public int getUpgradeDefenseTechnologyDeuteriumCost() {
		return upgradeDefenseTechnologyDeuteriumCost;
	}
	public int getUpgradeAttackTechnologyDeuteriumCost() {
		return upgradeAttackTechnologyDeuteriumCost;
	}
	public ArrayList<MilitaryUnit>[] getArmy() {
		return army;
	}
	
	public void setArmy(ArrayList<MilitaryUnit>[] army) {
		this.army = army;
	}
	
	public int getPlanet_id() {
		return planet_id;
	}

	public void setPlanet_id(int planet_id) {
		this.planet_id = planet_id;
	}

	private void generateResources() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				metal += PLANET_METAL_GENERATED;
				deuterium += PLANET_DEUTERIUM_GENERATED;
				
				DatabaseController.instance.updateMetal(planet_id, metal);
				DatabaseController.instance.updateDeuterium(planet_id, deuterium);
			 }
		 };
		 timer.schedule(task, 60000, 60000);
	}
	
	public void generateInitShips() {

		// Generate Initial Ships, 1 of every type
		newLightHunter(1);
		newHeavytHunter(1);
		newBattleShip(1);
		newArmoredShip(1);
		newMissileLauncher(1);
		newIonCannon(1);
		newPlasmaCannon(1);
	}
	
	private void substractMaterials(int metalQuantity, int deuteriumQuantity) throws ResourceException {
		
		// Check if can buy
		if (metal < metalQuantity) throw new ResourceException();
		if (deuterium < deuteriumQuantity) throw new ResourceException();
		metal -= metalQuantity;
		deuterium -= deuteriumQuantity;
	}
	
	private int calculateNewUpgradePrice(int upgradeCost, int upgradePrecentage) {
		return upgradeCost + (int)(upgradeCost * upgradePrecentage / 100);
	}
	
	public void upgradeTechnologyDefense() {
		try {
			substractMaterials(0, upgradeDefenseTechnologyDeuteriumCost);
			upgradeDefenseTechnologyDeuteriumCost = calculateNewUpgradePrice(upgradeDefenseTechnologyDeuteriumCost, Variables.UPGRADE_PLUS_DEFENSE_TECHNOLOGY_DEUTERIUM_COST);
			technologyDefense++;
			
			DatabaseController.instance.updateDefenseTechnology(this);
		}
		catch (ResourceException re) {
			re.printStackTrace();
		}
	}
	
	public void upgradeTechnologyAttack() {
		try {
			substractMaterials(0, upgradeAttackTechnologyDeuteriumCost);
			upgradeAttackTechnologyDeuteriumCost = calculateNewUpgradePrice(upgradeAttackTechnologyDeuteriumCost, Variables.UPGRADE_PLUS_ATTACK_TECHNOLOGY_DEUTERIUM_COST);
			technologyAttack++;
			
			DatabaseController.instance.updateAttackTechnology(this);
		}
		catch (ResourceException re) {
			re.printStackTrace();
		}
	}
	
	/*
	 * Add new Unit/s:
	 * 	- Get the index of arrays
	 * 	- Calculate unitary metal and deuterium cost
	 * 	- Calculate armour and damage, rely on attack and defense technology level
	 * 		+ Base Stat + (Tech * Plus) * Base Stat / 100 --> Porcentual upgrade
	 * 	- Iterate:
	 * 		+ Substract materials
	 * 		+ Add unit to army
	 * 		+ Update buy counter
	 * 		+ If there is an exception, pass info and break for loop
	 */
	
	public void newLightHunter(int n) {
		
		int order = MilitaryUnitOrder.LIGHTHUNTER.ordinal();
		int metalCost = Variables.METAL_COST_UNITS[order];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_LIGHTHUNTER + (technologyDefense * Variables.PLUS_ARMOR_LIGHTHUNTER_BY_TECHNOLOGY) * Variables.ARMOR_LIGHTHUNTER / 100;
		int baseDamage = Variables.BASE_DAMAGE_LIGHTHUNTER + (technologyDefense * Variables.PLUS_ATTACK_LIGHTHUNTER_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_LIGHTHUNTER / 100;
		
		for (int i = 0; i < n; i++) {
			try {
				substractMaterials(metalCost, deuteriumCost);
				army[order].add(new LightHunter(initialArmour, baseDamage));
				unitsAdded++;
			}
			catch (ResourceException re) {
				InterfaceController.instance.addBuyInfo(re.getMessage() + MILITARY_UNIT_NAMES[order] + "!");
				break;
			}
		}
		
		InterfaceController.instance.addBuyInfo("[*] " + unitsAdded + " " + MILITARY_UNIT_NAMES[order] + " added to army!");
	}
	
	public void newHeavytHunter(int n) {
		
		int order = MilitaryUnitOrder.HEAVYHUNTER.ordinal();
		int metalCost = Variables.METAL_COST_UNITS[order];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_HEAVYHUNTER + (technologyDefense * Variables.PLUS_ARMOR_HEAVYHUNTER_BY_TECHNOLOGY) * Variables.ARMOR_HEAVYHUNTER / 100;
		int baseDamage = Variables.BASE_DAMAGE_HEAVYHUNTER + (technologyDefense * Variables.PLUS_ATTACK_HEAVYHUNTER_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_HEAVYHUNTER / 100;
		
		for (int i = 0; i < n; i++) {
			try {
				substractMaterials(metalCost, deuteriumCost);
				army[order].add(new HeavyHunter(initialArmour, baseDamage));
				unitsAdded++;
			}
			catch (ResourceException re) {
				InterfaceController.instance.addBuyInfo(re.getMessage() + MILITARY_UNIT_NAMES[order] + "!");
				break;
			}
		}
		
		InterfaceController.instance.addBuyInfo("[*] " + unitsAdded + " " + MILITARY_UNIT_NAMES[order] + " added to army!");
	}
	
	public void newBattleShip(int n) {
		
		int order = MilitaryUnitOrder.BATTLESHIP.ordinal();
		int metalCost = Variables.METAL_COST_UNITS[order];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_BATTLESHIP + (technologyDefense * Variables.PLUS_ARMOR_BATTLESHIP_BY_TECHNOLOGY) * Variables.ARMOR_BATTLESHIP / 100;
		int baseDamage = Variables.BASE_DAMAGE_BATTLESHIP + (technologyDefense * Variables.PLUS_ATTACK_BATTLESHIP_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_BATTLESHIP / 100;
		
		for (int i = 0; i < n; i++) {
			try {
				substractMaterials(metalCost, deuteriumCost);
				army[order].add(new BattleShip(initialArmour, baseDamage));
				unitsAdded++;
			}
			catch (ResourceException re) {
				InterfaceController.instance.addBuyInfo(re.getMessage() + MILITARY_UNIT_NAMES[order] + "!");
				break;
			}
		}
		
		InterfaceController.instance.addBuyInfo("[*] " + unitsAdded + " " + MILITARY_UNIT_NAMES[order] + " added to army!");
	}
	
	public void newArmoredShip(int n) {
		
		int order = MilitaryUnitOrder.ARMOREDSHIP.ordinal();
		int metalCost = Variables.METAL_COST_UNITS[order];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_ARMOREDSHIP + (technologyDefense * Variables.PLUS_ARMOR_ARMOREDSHIP_BY_TECHNOLOGY) * Variables.ARMOR_ARMOREDSHIP / 100;
		int baseDamage = Variables.BASE_DAMAGE_ARMOREDSHIP + (technologyDefense * Variables.PLUS_ATTACK_ARMOREDSHIP_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_ARMOREDSHIP / 100;
		
		for (int i = 0; i < n; i++) {
			try {
				substractMaterials(metalCost, deuteriumCost);
				army[order].add(new ArmoredShip(initialArmour, baseDamage));
				unitsAdded++;
			}
			catch (ResourceException re) {
				InterfaceController.instance.addBuyInfo(re.getMessage() + MILITARY_UNIT_NAMES[order] + "!");
				break;
			}
		}
		
		InterfaceController.instance.addBuyInfo("[*] " + unitsAdded + " " + MILITARY_UNIT_NAMES[order] + " added to army!");
	}
	
	public void newMissileLauncher(int n) {
		
		int order = MilitaryUnitOrder.MISSILELAUNCHER.ordinal();
		int metalCost = Variables.METAL_COST_UNITS[order];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_MISSILELAUNCHER + (technologyDefense * Variables.PLUS_ARMOR_MISSILELAUNCHER_BY_TECHNOLOGY) * Variables.ARMOR_MISSILELAUNCHER / 100;
		int baseDamage = Variables.BASE_DAMAGE_MISSILELAUNCHER + (technologyDefense * Variables.PLUS_ATTACK_MISSILELAUNCHER_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_MISSILELAUNCHER / 100;
		
		for (int i = 0; i < n; i++) {
			try {
				substractMaterials(metalCost, deuteriumCost);
				army[order].add(new MissileLauncher(initialArmour, baseDamage));
				unitsAdded++;
			}
			catch (ResourceException re) {
				InterfaceController.instance.addBuyInfo(re.getMessage() + MILITARY_UNIT_NAMES[order] + "!");
				break;
			}
		}
		
		InterfaceController.instance.addBuyInfo("[*] " + unitsAdded + " " + MILITARY_UNIT_NAMES[order] + " added to army!");
	}
	
	public void newIonCannon(int n) {
		
		int order = MilitaryUnitOrder.IONCANNON.ordinal();
		int metalCost = Variables.METAL_COST_UNITS[order];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_IONCANNON + (technologyDefense * Variables.PLUS_ARMOR_IONCANNON_BY_TECHNOLOGY) * Variables.ARMOR_IONCANNON / 100;
		int baseDamage = Variables.BASE_DAMAGE_IONCANNON + (technologyDefense * Variables.PLUS_ATTACK_IONCANNON_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_ARMOREDSHIP / 100;
		
		for (int i = 0; i < n; i++) {
			try {
				substractMaterials(metalCost, deuteriumCost);
				army[order].add(new IonCannon(initialArmour, baseDamage));
				unitsAdded++;
			}
			catch (ResourceException re) {
				InterfaceController.instance.addBuyInfo(re.getMessage() + MILITARY_UNIT_NAMES[order] + "!");
				break;
			}
		}
		
		InterfaceController.instance.addBuyInfo("[*] " + unitsAdded + " " + MILITARY_UNIT_NAMES[order] + " added to army!");
	}
	
	public void newPlasmaCannon(int n) {
		
		int order = MilitaryUnitOrder.PLASMACANNON.ordinal();
		int metalCost = Variables.METAL_COST_UNITS[order];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_PLASMACANNON + (technologyDefense * Variables.PLUS_ARMOR_PLASMACANNON_BY_TECHNOLOGY) * Variables.ARMOR_PLASMACANNON / 100;
		int baseDamage = Variables.BASE_DAMAGE_PLASMACANNON+ (technologyDefense * Variables.PLUS_ATTACK_PLASMACANNON_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_PLASMACANNON / 100;
		
		for (int i = 0; i < n; i++) {
			try {
				substractMaterials(metalCost, deuteriumCost);
				army[order].add(new PlasmaCannon(initialArmour, baseDamage));
				unitsAdded++;
			}
			catch (ResourceException re) {
				InterfaceController.instance.addBuyInfo(re.getMessage() + MILITARY_UNIT_NAMES[order] + "!");
				break;
			}
		}
		
		InterfaceController.instance.addBuyInfo("[*] " + unitsAdded + " " + MILITARY_UNIT_NAMES[order] + " added to army!");
	}
	
	public String printStats() {
		
		// Print Planet Stats for ToolTip
		
		String result = "<html>";
		
		result += "<b>" + Printing.printTitle("Planet Stats:") + "</b><br><br>";
		result += Printing.printTitle("technology".toUpperCase()) + "</b><br><br>";
		result += Printing.printStringSized("Attack Technology -> ", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(technologyAttack, MAX_NUMBER_SIZE) + "<br>";
		result += Printing.printStringSized("Defense Technology -> ", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(technologyDefense, MAX_NUMBER_SIZE) + "<br><br>";
		result += "<b>" + Printing.printTitle("defenses".toUpperCase()) + "</b><br><br>";
		for (int i = MilitaryUnitOrder.MISSILELAUNCHER.ordinal(); i <= MilitaryUnitOrder.PLASMACANNON.ordinal(); i++) {
			result += Printing.printStringSized(MILITARY_UNIT_NAMES[i] + " -> ", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(army[i].size(), MAX_NUMBER_SIZE) + "<br>";			
		}
		result += "<br>";
		result += "<b>" + Printing.printTitle("fleet".toUpperCase()) + "</b><br><br>";
		for (int i = 0; i < MilitaryUnitOrder.MISSILELAUNCHER.ordinal(); i++) {
			result += Printing.printStringSized(MILITARY_UNIT_NAMES[i] + " -> ", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(army[i].size(), MAX_NUMBER_SIZE) + "<br>";			
		}
		result += "<br>";
		result += "<b>" + Printing.printTitle("resources".toUpperCase()) + "</b><br><br>";
		result += Printing.printStringSized("Metal -> ", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(metal, MAX_NUMBER_SIZE)+  "<br>";
		result += Printing.printStringSized("Deuterium -> ", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(deuterium, MAX_NUMBER_SIZE);
		
		result += "</html>";
		
		return result;
	}
}