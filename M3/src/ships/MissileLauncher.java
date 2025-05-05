package ships;

public class MissileLauncher extends Defense{

	public MissileLauncher(int armor, int baseDamage) {
		super(armor, baseDamage);
		super.setInitialArmor(armor);
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
		return METAL_COST_MISSILELAUNCHER;
	}

	public int getDeuteriumCost() {
		return DEUTERIUM_COST_MISSILELAUNCHER;
	}
	
	public int getChanceGeneratingWaste() {
		return CHANCE_GENERATING_WASTE_MISSILELAUNCHER;
	}
	
	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_MISSILELAUNCHER;
	}

	public void resetArmor() {
		super.setArmor(super.getInitialArmor());
	}
	
	
	
}
