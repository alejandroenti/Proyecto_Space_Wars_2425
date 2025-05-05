package ships;

public class BattleShip extends Ship{
	public BattleShip(int armor, int baseDamage) {
		super(armor, baseDamage);
		super.setInitialArmor(armor);
	}
	
	public BattleShip() {
		super(ARMOR_BATTLESHIP,BASE_DAMAGE_BATTLESHIP);
		super.setInitialArmor(ARMOR_BATTLESHIP);
	}
	
	public int attack() {
		return super.getBaseDamage();
	}

	public void takeDamage(int receivedDamage) {
		super.setArmor(super.getArmor()-receivedDamage);
	}

	public int getActualArmor() {
		return super.getArmor();
	}

	public int getMetalCost() {
		return METAL_COST_BATTLESHIP;
	}

	public int getDeuteriumCost() {
		return DEUTERIUM_COST_BATTLESHIP;
	}
	
	public int getChanceGeneratingWaste() {
		return CHANCE_GENERATING_WASTE_BATTLESHIP;
	}
	
	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_BATTLESHIP;
	}

	public void resetArmor() {
		super.setArmor(super.getInitialArmor());
	}
}
