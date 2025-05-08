import java.util.ArrayList;

import battle.Battle;
import gui.MainWindow;
import planets.Planet;
import ships.ArmoredShip;
import ships.BattleShip;
import ships.HeavyHunter;
import ships.LightHunter;
import ships.MilitaryUnit;

public class Main {

	public static void main(String[] args) {
		Planet planet = new Planet();
		
		ArrayList<MilitaryUnit>[] army = new ArrayList[4];
		army[0] = new ArrayList<MilitaryUnit>();
		army[0].add(new LightHunter());
		army[1] = new ArrayList<MilitaryUnit>();
		army[1].add(new HeavyHunter());
		army[2] = new ArrayList<MilitaryUnit>();
		army[2].add(new BattleShip());
		army[3] = new ArrayList<MilitaryUnit>();
		army[3].add(new ArmoredShip());
		Battle battle = new Battle(planet.getArmy(), army);	
		battle.startBattle();
		new MainWindow();
	}

}
