import java.util.ArrayList;

import battle.Battle;
import gui.MainWindow;
import planets.Planet;
import ships.ArmoredShip;
import ships.BattleShip;
import ships.HeavyHunter;
import ships.LightHunter;
import ships.MilitaryUnit;
import utils.Variables;

public class Main implements Variables {

	public static void main(String[] args) {
		Planet planet = new Planet();
		MainWindow window = new MainWindow();
		
		window.getPlayerPanel().setPlayerArmy(planet.getArmy());
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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

}
