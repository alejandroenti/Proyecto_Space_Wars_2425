package ships;

public interface MilitaryUnit {
	abstract int attack();
	abstract void takeDamage(int receivedDamage);
	abstract int getActualArmor();
	abstract int getMetalCost();
	abstract int getDeuteriumCost();
	abstract int getChanceGeneratingWaste();
	abstract int getChanceAttackAgain();
	abstract void resetArmor();
}
