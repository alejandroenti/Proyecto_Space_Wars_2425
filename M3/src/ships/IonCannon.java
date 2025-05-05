package ships;

public class IonCannon extends Defense{
	public IonCannon(int armor, int baseDamage) {
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
		return METAL_COST_IONCANNON;
	}

	public int getDeuteriumCost() {
		return DEUTERIUM_COST_IONCANNON;
	}
	
	public int getChanceGeneratingWaste() {
		return CHANCE_GENERATING_WASTE_IONCANNON;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_IONCANNON;
	}

	public void resetArmor() {
		super.setArmor(super.getInitialArmor());
	}
}
