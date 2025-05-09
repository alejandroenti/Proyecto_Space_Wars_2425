import java.util.ArrayList;

import battle.Battle;
import gui.MainWindow;
import planets.Planet;
import ships.ArmoredShip;
import ships.BattleShip;
import ships.HeavyHunter;
import ships.IonCannon;
import ships.LightHunter;
import ships.MilitaryUnit;
import ships.MissileLauncher;
import ships.PlasmaCannon;
import utils.Variables;

public class Main implements Variables {
	
	private Planet planet;
	private MainWindow window;
	
	public Main() {
		super();
		
		this.planet = new Planet();
		this.window = new MainWindow();
	}
	
	public static void main(String[] args) {
		
		Main instance = new Main();
		
		instance.window.getPlayerPanel().setPlayerArmy(instance.planet.getArmy());
		
//		window.getPlayerPanel().addUnit(MilitaryUnitOrder.ARMOREDSHIP.ordinal(), new ArmoredShip());
//		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		window.getPlayerPanel().removeUnit(MilitaryUnitOrder.PLASMACANNON.ordinal(), planet.getArmy()[MilitaryUnitOrder.PLASMACANNON.ordinal()].get(0));
//		window.getPlayerPanel().removeUnit(MilitaryUnitOrder.LIGHTHUNTER.ordinal(), planet.getArmy()[MilitaryUnitOrder.LIGHTHUNTER.ordinal()].get(0));

		
//		ArrayList<MilitaryUnit>[] army = new ArrayList[4];
//		army[0] = new ArrayList<MilitaryUnit>();
//		army[0].add(new LightHunter());
//		army[1] = new ArrayList<MilitaryUnit>();
//		army[1].add(new HeavyHunter());
//		army[2] = new ArrayList<MilitaryUnit>();
//		army[2].add(new BattleShip());
//		army[3] = new ArrayList<MilitaryUnit>();
//		army[3].add(new ArmoredShip());
//		Battle battle = new Battle(planet.getArmy(), army, window.getPlayerPanel(), window.getEnemyPanel());	
//		//battle.startBattle();
	}

	public void buyMilitaryUnit(MilitaryUnit unit, int quantity) {
		
		if (unit instanceof LightHunter) {
			planet.newLightHunter(quantity);
		}
		else if (unit instanceof HeavyHunter) {
			planet.newHeavytHunter(quantity);
		}
		else if (unit instanceof BattleShip) {
			planet.newBattleShip(quantity);
		}
		else if (unit instanceof ArmoredShip) {
			planet.newArmoredShip(quantity);
		}
		else if (unit instanceof MissileLauncher) {
			planet.newMissileLauncher(quantity);
		}
		else if (unit instanceof IonCannon) {
			planet.newIonCannon(quantity);
		}
		else if (unit instanceof PlasmaCannon) {
			planet.newPlasmaCannon(quantity);
		}
		
	}
}
