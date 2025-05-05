package planets;

import java.util.ArrayList;

import utils.Printing;
import utils.Variables;

public class Planet implements Variables {

	private final int NUM_MILITARY_UNITS = 7;
	private final int INIT_TECHNOLOGY_LEVEL = 0;
	private final int INIT_METAL = 58500;
	private final int INIT_DEUTERIUM = 28300;
	private final int MAX_LINE_SIZE = 64;
	private final int MAX_NUMBER_SIZE = 10;
	
	private enum MilitaryUnitOrder {
		LIGHTHUNTER,
		HEAVYHUNTER,
		BATTLESHIP,
		ARMOREDSHIP,
		MISSILELAUNCHER,
		IONCANNON,
		PLASMACANNON
	}
	
	private String[] militaryUnitNames = {
			"Light Hunter",
			"Heavy Hunter",
			"Battle Ship",
			"Armored Ship",
			"Missile Launcher",
			"Ion Cannon",
			"Plasma Cannon"
	}
	
	private int technologyDefense;
	private int technologyAttack;
	private int metal;
	private int deuterium;
	private int upgradeDefenseTechnologyDeuteriumCost;
	private int upgradeAttackTechnologyDeuteriumCost;
	private ArrayList<MilitaryUnity>[] army;
	
	public Planet() {
		super();
		
		this.technologyDefense = INIT_TECHNOLOGY_LEVEL;
		this.technologyAttack = INIT_TECHNOLOGY_LEVEL;
		this.metal = INIT_METAL;
		this.deuterium = INIT_DEUTERIUM;
		this.upgradeDefenseTechnologyDeuteriumCost = Variables.UPGRADE_BASE_DEFENSE_TECHNOLOGY_DEUTERIUM_COST;
		this.upgradeAttackTechnologyDeuteriumCost = Variables.UPGRADE_BASE_ATTACK_TECHNOLOGY_DEUTERIUM_COST;
		this.army = new ArrayList[NUM_MILITARY_UNITS];
	}
	
	public int getNUM_MILITARY_UNITS() {
		return NUM_MILITARY_UNITS;
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
	public int getUpgradeDefenseTechnologyDeuteriumCost() {
		return upgradeDefenseTechnologyDeuteriumCost;
	}
	public int getUpgradeAttackTechnologyDeuteriumCost() {
		return upgradeAttackTechnologyDeuteriumCost;
	}
	public ArrayList<MilitaryUnity>[] getArmy() {
		return army;
	}
	
	private void generateInitShips() {
		
		army[0] = new ArrayList<LightHunter>();
		army[1] = new ArrayList<HeavyHunter>();
		army[2] = new ArrayList<BattleShip>();
		army[3] = new ArrayList<ArmoredShip>();
		army[4] = new ArrayList<MissileLauncher>();
		army[5] = new ArrayList<IonCannon>();
		army[6] = new ArrayList<PlasmaCannon>();
		
		newLightHunter(1);
		newHeavytHunter(1);
		newBattleShip(1);
		newArmoredShip(1);
		newMissileLauncher(1);
		newIonCannon(1);
		newPlasmaCannon(1);
	}
	
	private void substractMaterials(int metalQuantity, int deuteriumQuantity) throws ResourceException {
		if (metal < metalQuantity) throw new ResourceException();
		if (deuterium < deuteriumQuantity) throw new ResourceException();
		metal -= metalQuantity;
		deuterium -= deuteriumQuantity;
	}
	
	private int calculateNewUpgradePrice(int upgradeCost, int upgradePrecentage) {
		return upgradeCost + (int)(upgradeCost * upgradePrecentage / 100);
	}
	
	private void generateArmy(int metalCost, int deuteriumCost, MilitaryUnitOrder order, int initialArmour, int baseDamage, int n) {
		
		int unitsAdded = 0;
		for (int i = 0; i < n; i++) {
			try {
				substractMaterials(metalCost, deuteriumCost);
				if (order.ordinal() < MilitaryUnitOrder.MISSILELAUNCHER.ordinal()) {
					army[order.ordinal()].add(new Ship(initialArmour, baseDamage));					
				}
				else {
					army[order.ordinal()].add(new Defense(initialArmour, baseDamage));
				}
				unitsAdded++;
			}
			catch (ResourceException re) {
				re.printStackTrace();
				break;
			}
		}
	}
	
	public void upgradeTechnologyDefense() {
		try {
			substractMaterials(0, upgradeDefenseTechnologyDeuteriumCost);
			upgradeDefenseTechnologyDeuteriumCost = calculateNewUpgradePrice(upgradeDefenseTechnologyDeuteriumCost, Variables.UPGRADE_PLUS_DEFENSE_TECHNOLOGY_DEUTERIUM_COST);
			technologyDefense++;
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
		}
		catch (ResourceException re) {
			re.printStackTrace();
		}
	}
	
	public void newLightHunter(int n) {
		MilitaryUnitOrder order = MilitaryUnitOrder.LIGHTHUNTER;
		int metalCost = Variables.METAL_COST_UNITS[order.ordinal()];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order.ordinal()];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_LIGHTHUNTER + (technologyDefense * Variables.PLUS_ARMOR_LIGHTHUNTER_BY_TECHNOLOGY) * Variables.ARMOR_LIGHTHUNTER / 100;
		int baseDamage = Variables.BASE_DAMAGE_LIGHTHUNTER + (technologyDefense * Variables.PLUS_ATTACK_LIGHTHUNTER_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_LIGHTHUNTER / 100;
		
		generateArmy(metalCost, deuteriumCost, order, initialArmour, baseDamage, n);
	}
	
	public void newHeavytHunter(int n) {
		MilitaryUnitOrder order = MilitaryUnitOrder.HEAVYHUNTER;
		int metalCost = Variables.METAL_COST_UNITS[order.ordinal()];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order.ordinal()];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_HEAVYHUNTER + (technologyDefense * Variables.PLUS_ARMOR_HEAVYHUNTER_BY_TECHNOLOGY) * Variables.ARMOR_HEAVYHUNTER / 100;
		int baseDamage = Variables.BASE_DAMAGE_HEAVYHUNTER + (technologyDefense * Variables.PLUS_ATTACK_HEAVYHUNTER_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_HEAVYHUNTER / 100;
		
		generateArmy(metalCost, deuteriumCost, order, initialArmour, baseDamage, n);
	}
	
	public void newBattleShip(int n) {
		MilitaryUnitOrder order = MilitaryUnitOrder.BATTLESHIP;
		int metalCost = Variables.METAL_COST_UNITS[order.ordinal()];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order.ordinal()];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_BATTLESHIP + (technologyDefense * Variables.PLUS_ARMOR_BATTLESHIP_BY_TECHNOLOGY) * Variables.ARMOR_BATTLESHIP / 100;
		int baseDamage = Variables.BASE_DAMAGE_BATTLESHIP + (technologyDefense * Variables.PLUS_ATTACK_BATTLESHIP_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_BATTLESHIP / 100;
		
		generateArmy(metalCost, deuteriumCost, order, initialArmour, baseDamage, n);
	}
	
	public void newArmoredShip(int n) {
		MilitaryUnitOrder order = MilitaryUnitOrder.ARMOREDSHIP;
		int metalCost = Variables.METAL_COST_UNITS[order.ordinal()];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order.ordinal()];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_ARMOREDSHIP + (technologyDefense * Variables.PLUS_ARMOR_ARMOREDSHIP_BY_TECHNOLOGY) * Variables.ARMOR_ARMOREDSHIP / 100;
		int baseDamage = Variables.BASE_DAMAGE_ARMOREDSHIP + (technologyDefense * Variables.PLUS_ATTACK_ARMOREDSHIP_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_ARMOREDSHIP / 100;
		
		generateArmy(metalCost, deuteriumCost, order, initialArmour, baseDamage, n);
	}
	
	public void newMissileLauncher(int n) {
		MilitaryUnitOrder order = MilitaryUnitOrder.MISSILELAUNCHER;
		int metalCost = Variables.METAL_COST_UNITS[order.ordinal()];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order.ordinal()];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_MISSILELAUNCHER + (technologyDefense * Variables.PLUS_ARMOR_MISSILELAUNCHER_BY_TECHNOLOGY) * Variables.ARMOR_MISSILELAUNCHER / 100;
		int baseDamage = Variables.BASE_DAMAGE_MISSILELAUNCHER + (technologyDefense * Variables.PLUS_ATTACK_MISSILELAUNCHER_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_MISSILELAUNCHER / 100;
		
		generateArmy(metalCost, deuteriumCost, order, initialArmour, baseDamage, n);
	}
	
	public void newIonCannon(int n) {
		MilitaryUnitOrder order = MilitaryUnitOrder.IONCANNON;
		int metalCost = Variables.METAL_COST_UNITS[order.ordinal()];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order.ordinal()];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_IONCANNON + (technologyDefense * Variables.PLUS_ARMOR_IONCANNON_BY_TECHNOLOGY) * Variables.ARMOR_IONCANNON / 100;
		int baseDamage = Variables.BASE_DAMAGE_IONCANNON + (technologyDefense * Variables.PLUS_ATTACK_IONCANNON_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_ARMOREDSHIP / 100;
		
		generateArmy(metalCost, deuteriumCost, order, initialArmour, baseDamage, n);
	}
	
	public void newPlasmaCannon(int n) {
		MilitaryUnitOrder order = MilitaryUnitOrder.PLASMACANNON;
		int metalCost = Variables.METAL_COST_UNITS[order.ordinal()];
		int deuteriumCost = Variables.DEUTERIUM_COST_UNITS[order.ordinal()];
		int unitsAdded = 0;
		
		int initialArmour = Variables.ARMOR_PLASMACANNON + (technologyDefense * Variables.PLUS_ARMOR_PLASMACANNON_BY_TECHNOLOGY) * Variables.ARMOR_PLASMACANNON / 100;
		int baseDamage = Variables.BASE_DAMAGE_PLASMACANNON+ (technologyDefense * Variables.PLUS_ATTACK_PLASMACANNON_BY_TECHNOLOGY) * Variables.BASE_DAMAGE_PLASMACANNON / 100;
		
		generateArmy(metalCost, deuteriumCost, order, initialArmour, baseDamage, n);
	}
	
	public void printStats() {
		String result = "";
		
		result += Printing.printTitle("Planet Stats:");
		result += Printing.printTitle("technology".toUpperCase());
		result += Printing.printStringSized("Attack Technology", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(technologyAttack, MAX_NUMBER_SIZE);
		result += Printing.printStringSized("Defense Technology", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(technologyDefense, MAX_NUMBER_SIZE) + "\n\n";
		result += Printing.printTitle("defenses".toUpperCase());
		for (int i = MilitaryUnitOrder.MISSILELAUNCHER.ordinal(); i <= MilitaryUnitOrder.PLASMACANNON.ordinal(); i++) {
			result += Printing.printStringSized(militaryUnitNames[i], MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(army[i].size(), MAX_NUMBER_SIZE) + "\n";			
		}
		result += "\n";
		result += Printing.printTitle("fleet".toUpperCase());
		for (int i = 0; i <= MilitaryUnitOrder.MISSILELAUNCHER.ordinal(); i++) {
			result += Printing.printStringSized(militaryUnitNames[i], MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(army[i].size(), MAX_NUMBER_SIZE) + "\n";			
		}
		result += "\n";
		result += Printing.printTitle("resources".toUpperCase());
		result += Printing.printStringSized("Metal", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(metal, MAX_NUMBER_SIZE);
		result += Printing.printStringSized("Deuterium", MAX_LINE_SIZE - MAX_NUMBER_SIZE) + Printing.printNumberSized(deuterium, MAX_NUMBER_SIZE) + "\n\n";
		
		System.out.println(result);
	}
}
