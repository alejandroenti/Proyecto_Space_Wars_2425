package ships;

public class ArmoredShip extends Ship{
	public ArmoredShip(int armor, int baseDamage) {
		super(armor, baseDamage);
		super.setInitialArmor(armor);
	}
	
	public ArmoredShip() {
		super(ARMOR_ARMOREDSHIP,BASE_DAMAGE_ARMOREDSHIP);
		super.setInitialArmor(ARMOR_ARMOREDSHIP);
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
		return METAL_COST_ARMOREDSHIP;
	}

	public int getDeuteriumCost() {
		return DEUTERIUM_COST_ARMOREDSHIP;
	}
	
	public int getChanceGeneratingWaste() {
		return CHANCE_GENERATING_WASTE_ARMOREDSHIP;
	}
	
	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_ARMOREDSHIP;
	}

	public void resetArmor() {
		super.setArmor(super.getInitialArmor());
	}
}
