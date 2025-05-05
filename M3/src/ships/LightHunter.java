package ships;

public class LightHunter extends Ship{

	public LightHunter(int armor, int baseDamage) {
		super(armor, baseDamage);
		super.setInitialArmor(armor);
	}
	
	public LightHunter() {
		super(ARMOR_LIGHTHUNTER,BASE_DAMAGE_LIGHTHUNTER);
		super.setInitialArmor(ARMOR_LIGHTHUNTER);
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
		return METAL_COST_LIGHTHUNTER;
	}

	public int getDeuteriumCost() {
		return DEUTERIUM_COST_LIGHTHUNTER;
	}
	
	public int getChanceGeneratingWaste() {
		return CHANCE_GENERATING_WASTE_LIGHTHUNTER;
	}
	
	public int getChanceAttackAgain() {
		return CHANCE_ATTACK_AGAIN_LIGHTHUNTER;
	}

	public void resetArmor() {
		super.setArmor(super.getInitialArmor());
	}
	
}
