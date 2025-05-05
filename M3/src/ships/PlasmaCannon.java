package ships;

public class PlasmaCannon extends Defense implements MilitaryUnit{
	public PlasmaCannon(int armor, int baseDamage) {
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
		return METAL_COST_PLASMACANNON;
	}

	public int getDeuteriumCost() {
		return DEUTERIUM_COST_PLASMACANNON;
	}
	
	public int getChanceGeneratingWaste() {
		return CHANCE_GENERATING_WASTE_PLASMACANNON;
	}

	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_PLASMACANNON;
	}

	public void resetArmor() {
		super.setArmor(super.getInitialArmor());
	}
}
