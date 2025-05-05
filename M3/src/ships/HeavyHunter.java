package ships;

public class HeavyHunter extends Ship{
	public HeavyHunter(int armor, int baseDamage) {
		super(armor, baseDamage);
		super.setInitialArmor(armor);
	}
	
	public HeavyHunter() {
		super(ARMOR_HEAVYHUNTER,BASE_DAMAGE_HEAVYHUNTER);
		super.setInitialArmor(ARMOR_HEAVYHUNTER);
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
		return METAL_COST_HEAVYHUNTER;
	}

	public int getDeuteriumCost() {
		return DEUTERIUM_COST_HEAVYHUNTER;
	}
	
	public int getChanceGeneratingWaste() {
		return CHANCE_GENERATING_WASTE_HEAVYHUNTER;
	}
	
	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_HEAVYHUNTER;
	}

	public void resetArmor() {
		super.setArmor(super.getInitialArmor());
	}
}
