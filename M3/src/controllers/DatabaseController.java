package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import planets.Planet;
import utils.Printing;
import utils.Variables;
import utils.Variables.MilitaryUnitOrder;

public class DatabaseController implements Variables{
	public static DatabaseController instance;

	private String urlDatos = "jdbc:mysql://planet-wars.clmmsosyssic.eu-north-1.rds.amazonaws.com:3306/SpaceWars?serverTimezone=UTC";
	private String usuario = "admin";
	private String pass = "proyecto2025";
	private Connection conn;
	
	private PreparedStatement ps;
	private Statement stmnt;
	private ResultSet rs;
	
	private String query;
	
	private ArrayList<Integer> planet_ids;
	private ArrayList<String> planet_names;
	private ArrayList<ArrayList<Integer>> num_battles;

	public DatabaseController() {
		DatabaseController.instance = this;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection(urlDatos, usuario, pass); 
			stmnt = conn.createStatement();

			} catch (ClassNotFoundException e) {
				System.out.println("Error en el driver");
				e.printStackTrace();
			} catch (SQLException e) {
				System.err.println("SQL exception");
				e.printStackTrace();
			}
	}
	
	public ArrayList<Integer> getPlanet_ids() {
		return planet_ids;
	}



	public ArrayList<String> getPlanet_names() {
		return planet_names;
	}



	public ArrayList<ArrayList<Integer>> getNum_battles() {
		return num_battles;
	}


	// METODOS PLANET_STATS
	// Crear planeta
	public void newPlanet(Planet planet, String namePlanet) {
		query = "INSERT INTO planet_stats ( name_planet, resource_metal_amount, resource_deuterium_amount, technology_defense_level, technology_attack_level, battles_counter, missile_launcher_remaining, ion_canon_remaining, plasma_canon_remaining, light_hunter_remaining, heavy_hunter_remaining, battleship_remaining, armored_ship_remaining ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, namePlanet);
			ps.setInt(2, planet.getMetal());
			ps.setInt(3, planet.getDeuterium());
			ps.setInt(4, planet.getTechnologyDefense());
			ps.setInt(5, planet.getTechnologyAttack());
			ps.setInt(6, 0);
			ps.setInt(7, planet.getArmy()[4].size());
			ps.setInt(8, planet.getArmy()[5].size());
			ps.setInt(9, planet.getArmy()[6].size());
			ps.setInt(10, planet.getArmy()[0].size());
			ps.setInt(11, planet.getArmy()[1].size());
			ps.setInt(12, planet.getArmy()[2].size());
			ps.setInt(13, planet.getArmy()[3].size());
			ps.executeUpdate();
			
			rs = stmnt.executeQuery("SELECT * FROM planet_stats");
			rs.last();
			int planet_id = rs.getInt(1);
			
			planet.setPlanet_id(planet_id);
			
		} catch (SQLException e) {
			System.err.println("newPlanet() failed!");
			e.printStackTrace();
		}
		
	}
	
	// Actualizamos metal
	public void updateMetal(int planet_id, int metal_quantity) {
		query = "UPDATE planet_stats SET resource_metal_amount = ? WHERE planet_id = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, metal_quantity);
			ps.setInt(2, planet_id);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("updateMetal() failed!");
			e.printStackTrace();
		}
	}
	
	// Actualizamos deuterio
	public void updateDeuterium(int planet_id, int deuterium_quantity) {
		query = "UPDATE planet_stats SET resource_deuterium_amount = ? WHERE planet_id = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, deuterium_quantity);
			ps.setInt(2, planet_id);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("updateDeuterium() failed!");
			e.printStackTrace();
		}
	}
	
	// Actualizamos tecnología defensa
	public void updateDefenseTechnology(Planet planet) {
		query = "UPDATE planet_stats SET technology_defense_level = ? WHERE planet_id = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet.getTechnologyDefense());
			ps.setInt(2, planet.getPlanet_id());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("updateDefenseTechnology() failed!");
			e.printStackTrace();
		}
	}
	
	// Actualizamos tecnología ataque
	public void updateAttackTechnology(Planet planet) {
		query = "UPDATE planet_stats SET technology_attack_level = ? WHERE planet_id = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet.getTechnologyAttack());
			ps.setInt(2, planet.getPlanet_id());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("updateAttackTechnology() failed!");
			e.printStackTrace();
		}
	}
	
	// Incrementamos el battles_counter
	public void updateBattlesCounter(int planet_id, int battles) {
		try {			
			
			stmnt.executeUpdate("UPDATE planet_stats SET battles_counter = " + battles + " WHERE planet_id = " + planet_id);
			
		} catch (SQLException e) {
			System.err.println("updateBattlesCounter() failed!");
			e.printStackTrace();
		}
	}
	
	
	// Actualizamos unidades restantes
	public void updateRemainingUnits(ArrayList[][] armies, int planet_id) {
		query = "UPDATE planet_stats SET missile_launcher_remaining = ?, ion_canon_remaining = ?, plasma_canon_remaining = ?, light_hunter_remaining = ?, heavy_hunter_remaining = ?, battleship_remaining = ?, armored_ship_remaining = ? WHERE planet_id = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, armies[0][4].size());
			ps.setInt(2, armies[0][5].size());
			ps.setInt(3, armies[0][6].size());
			ps.setInt(4, armies[0][0].size());
			ps.setInt(5, armies[0][1].size());
			ps.setInt(6, armies[0][2].size());
			ps.setInt(7, armies[0][3].size());
			ps.setInt(8, planet_id);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.err.println("updateRemainingUnits() failed!");
			e.printStackTrace();
		}
	}
	
	// METODOS BATTLE_STATS
	public int uploadBattleStats(int planet_id, int[] wasteMetalDeuterium, boolean win) {
		int num_battle = 0;
		
		query = "INSERT INTO battle_stats ( planet_id, resource_metal_acquired, resource_deuterium_acquired, waste_metal_generated, waste_deuterium_generated ) VALUES (?,?,?,?,?)";
		
		if (win) {
			try {
				ps = conn.prepareStatement(query);
				ps.setInt(1, planet_id);
				ps.setInt(2, wasteMetalDeuterium[0]);
				ps.setInt(3, wasteMetalDeuterium[1]);
				ps.setInt(4, wasteMetalDeuterium[0]);
				ps.setInt(5, wasteMetalDeuterium[1]);
				ps.executeUpdate();
				
			} catch (SQLException e) {
				System.err.println("uploadBattleStats() failed!");
				e.printStackTrace();
			}
			
		}else {
			try {
				ps = conn.prepareStatement(query);
				ps.setInt(1, planet_id);
				ps.setInt(2, 0);
				ps.setInt(3, 0);
				ps.setInt(4, wasteMetalDeuterium[0]);
				ps.setInt(5, wasteMetalDeuterium[1]);
				ps.executeUpdate();
				
			} catch (SQLException e) {
				System.err.println("uploadBattleStats() failed!");
				e.printStackTrace();
			}
		}
		
		try {
			rs = stmnt.executeQuery("SELECT num_battle FROM battle_stats");
			rs.last();
			num_battle = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num_battle;
	}
	
	// METODOS BATTLE_LOG
	public void uploadBattleLog(int planet_id, int num_battle, String battleDevelopment) {		
		String line = "";
		int last_new_line = 0;
		
		for (int i = 0; i < battleDevelopment.length(); i++) {
			if (battleDevelopment.charAt(i) == '\n') {
				if (last_new_line == 0) {
					line = battleDevelopment.substring(last_new_line, i);
				}else {
					line = battleDevelopment.substring(last_new_line+1, i);
				}
				
				query = "INSERT INTO battle_log (planet_id, num_battle, log_entry) VALUES (?,?,?)";
				
				try {
					ps = conn.prepareStatement(query);
					ps.setInt(1, planet_id);
					ps.setInt(2, num_battle);
					ps.setString(3, line);
					ps.executeUpdate();
					
				} catch (SQLException e) {
					System.err.println("uploadBattleLog() failed!");
					e.printStackTrace();
				}
				
				last_new_line = i;
			}
		}
	}
	
	// METODOS PLANET_BATTLE_DEFENSE
	// Creamos una entrada para las defensas del planeta en la batalla (al final de la batalla)
	public void uploadPlanetBattleDefense(int planet_id, int num_battle, int[][] initialArmies, ArrayList[][] armies) {
		
		query = "INSERT INTO planet_battle_defense (planet_id, num_battle, missile_launcher_built, missile_launcher_destroyed, ion_cannon_built, ion_cannon_destroyed, plasma_canon_built, plasma_canon_destroyed) VALUES (?,?,?,?,?,?,?,?)";
		

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, num_battle);
			ps.setInt(3, initialArmies[0][4]);
			ps.setInt(4, initialArmies[0][4] - armies[0][4].size());
			ps.setInt(5, initialArmies[0][5]);
			ps.setInt(6, initialArmies[0][5] -  armies[0][5].size());
			ps.setInt(7, initialArmies[0][6]);
			ps.setInt(8, initialArmies[0][6] - armies[0][6].size());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.err.println("uploadPlanetBattleDefense() failed!");
			e.printStackTrace();
		}
	}
	
	// METODOS PLANET_BATTLE_ARMY
	// Creamos una entrada para la flota del planeta en la batalla (al final de la batalla)
	public void uploadPlanetBattleArmy(int planet_id, int num_battle, int[][] initialArmies, ArrayList[][] armies) {
		
		query = "INSERT INTO planet_battle_army (planet_id, num_battle, light_hunter_built, light_hunter_destroyed, heavy_hunter_built, heavy_hunter_destroyed, battleship_built, battleship_destroyed, armored_ship_built, armored_ship_destroyed) VALUES (?,?,?,?,?,?,?,?,?,?)";
		

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, num_battle);
			ps.setInt(3, initialArmies[0][0]);
			ps.setInt(4, initialArmies[0][0] - armies[0][0].size());
			ps.setInt(5, initialArmies[0][1]);
			ps.setInt(6, initialArmies[0][1] -  armies[0][1].size());
			ps.setInt(7, initialArmies[0][2]);
			ps.setInt(8, initialArmies[0][2] - armies[0][2].size());
			ps.setInt(9, initialArmies[0][3]);
			ps.setInt(10, initialArmies[0][3] - armies[0][3].size());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.err.println("uploadPlanetBattleArmy() failed!");
			e.printStackTrace();
		}
	}
	
	// METODOS ENEMY_ARMY
	public void uploadEnemyArmy(int planet_id, int num_battle, int[][] initialArmies, ArrayList[][] armies) {
		
		query = "INSERT INTO enemy_army (planet_id, num_battle, light_hunter_threat, light_hunter_destroyed, heavy_hunter_threat, heavy_hunter_destroyed, battleship_threat, battleship_destroyed, armored_ship_threat, armored_ship_destroyed) VALUES (?,?,?,?,?,?,?,?,?,?)";
		

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, num_battle);
			ps.setInt(3, initialArmies[1][0]);
			ps.setInt(4, initialArmies[1][0] - armies[1][0].size());
			ps.setInt(5, initialArmies[1][1]);
			ps.setInt(6, initialArmies[1][1] -  armies[1][1].size());
			ps.setInt(7, initialArmies[1][2]);
			ps.setInt(8, initialArmies[1][2] - armies[1][2].size());
			ps.setInt(9, initialArmies[1][3]);
			ps.setInt(10, initialArmies[1][3] - armies[1][3].size());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.err.println("uploadEnemyArmy() failed!");
			e.printStackTrace();
		}
	}
	
	// METODOS REPORT
	public String getBattleLog(int planet_id, int num_battle) {
		String battle_log = "";
		
		try {
			rs = stmnt.executeQuery("SELECT log_entry FROM battle_log WHERE planet_id = " + planet_id + " AND num_battle = " + num_battle);
			while(rs.next()) {
				battle_log += rs.getString(1) + "\n";
			}
			
		} catch (SQLException e) {
			System.err.println("getBattleLog() failed!");
			e.printStackTrace();
		}
		
		return battle_log;
	}
	
	public int getBattlesCounter(int planet_id) {
		int battles_counter = 0;
		
		try {
			rs = stmnt.executeQuery("SELECT battles_counter FROM planet_stats WHERE planet_id = " + planet_id);
			rs.next();
			battles_counter = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return battles_counter;
	}
	
	public int[] getInitialPlanetUnits(int planet_id, int num_battle) {
		int[] initialPlanetUnits = new int[7];
		
		query = "SELECT light_hunter_built, heavy_hunter_built, battleship_built, armored_ship_built FROM planet_battle_army WHERE planet_id = ? AND num_battle = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, num_battle);
			rs = ps.executeQuery();
			
			rs.next();
			initialPlanetUnits[0] = rs.getInt(1);
			initialPlanetUnits[1] = rs.getInt(2);
			initialPlanetUnits[2] = rs.getInt(3);
			initialPlanetUnits[3] = rs.getInt(4);
			
		} catch (SQLException e) {
			System.err.println("getInitialPlanetUnits(), table: planet_battle_army failed!");
			e.printStackTrace();
		}
		
		query = "SELECT missile_launcher_built, ion_cannon_built, plasma_canon_built FROM planet_battle_defense WHERE planet_id = ? AND num_battle = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, num_battle);
			rs = ps.executeQuery();
			
			rs.next();
			initialPlanetUnits[4] = rs.getInt(1);
			initialPlanetUnits[5] = rs.getInt(2);
			initialPlanetUnits[6] = rs.getInt(3);
			
		} catch (SQLException e) {
			System.err.println("getInitialPlanetUnits(), table: planet_battle_defense failed!");
			e.printStackTrace();
		}
		
		return initialPlanetUnits;
	}
	
	public int[] getDestroyedPlanetUnits(int planet_id, int num_battle) {
		int[] destroyedPlanetUnits = new int[7];
		
		query = "SELECT light_hunter_destroyed, heavy_hunter_destroyed, battleship_destroyed, armored_ship_destroyed FROM planet_battle_army WHERE planet_id = ? AND num_battle = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, num_battle);
			rs = ps.executeQuery();
			
			rs.next();
			destroyedPlanetUnits[0] = rs.getInt(1);
			destroyedPlanetUnits[1] = rs.getInt(2);
			destroyedPlanetUnits[2] = rs.getInt(3);
			destroyedPlanetUnits[3] = rs.getInt(4);
			
		} catch (SQLException e) {
			System.err.println("getDestroyedPlanetUnits(), table: planet_battle_army failed!");
			e.printStackTrace();
		}
		
		query = "SELECT missile_launcher_destroyed, ion_cannon_destroyed, plasma_canon_destroyed FROM planet_battle_defense WHERE planet_id = ? AND num_battle = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, num_battle);
			rs = ps.executeQuery();
			
			rs.next();
			destroyedPlanetUnits[4] = rs.getInt(1);
			destroyedPlanetUnits[5] = rs.getInt(2);
			destroyedPlanetUnits[6] = rs.getInt(3);
			
		} catch (SQLException e) {
			System.err.println("getDestroyedPlanetUnits(), table: planet_battle_defense failed!");
			e.printStackTrace();
		}
		
		return destroyedPlanetUnits;
	}
	
	public int[] getInitialEnemyUnits(int planet_id, int num_battle) {
		int[] initialEnemyUnits = new int[4];
		
		query = "SELECT light_hunter_threat, heavy_hunter_threat, battleship_threat, armored_ship_threat FROM enemy_army WHERE planet_id = ? AND num_battle = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, num_battle);
			rs = ps.executeQuery();
			
			rs.next();
			initialEnemyUnits[0] = rs.getInt(1);
			initialEnemyUnits[1] = rs.getInt(2);
			initialEnemyUnits[2] = rs.getInt(3);
			initialEnemyUnits[3] = rs.getInt(4);
			
		} catch (SQLException e) {
			System.err.println("getInitialEnemyUnits() failed!");
			e.printStackTrace();
		}
		
		return initialEnemyUnits;
	}
	
	public int[] getDestroyedEnemyUnits(int planet_id, int num_battle) {
		int[] destroyedEnemyUnits = new int[4];
		
		query = "SELECT light_hunter_destroyed, heavy_hunter_destroyed, battleship_destroyed, armored_ship_destroyed FROM enemy_army WHERE planet_id = ? AND num_battle = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, num_battle);
			rs = ps.executeQuery();
			
			rs.next();
			destroyedEnemyUnits[0] = rs.getInt(1);
			destroyedEnemyUnits[1] = rs.getInt(2);
			destroyedEnemyUnits[2] = rs.getInt(3);
			destroyedEnemyUnits[3] = rs.getInt(4);
			
		} catch (SQLException e) {
			System.err.println("getDestroyedEnemyUnits() failed!");
			e.printStackTrace();
		}
		
		return destroyedEnemyUnits;
	}
	
	public int[][] calculateInitialCostFleet(int[] initialPlanetUnits, int[] initialEnemyUnits){
		int[][] initialCostFleet = new int[2][2];
		
		int totalMetalCostPlanetFleet = 0;
		int totalDeuteriumCostPlanetFleet = 0;
		
		for (int i=0; i < initialPlanetUnits.length; i++) {
			totalMetalCostPlanetFleet += initialPlanetUnits[i]*METAL_COST_UNITS[i];
			totalDeuteriumCostPlanetFleet += initialPlanetUnits[i]*DEUTERIUM_COST_UNITS[i];
		}
		
		int totalMetalCostEnemyFleet = 0;
		int totalDeuteriumCostEnemyFleet = 0;
		
		for (int i=0; i < initialEnemyUnits.length; i++) {
			totalMetalCostEnemyFleet += initialEnemyUnits[i]*METAL_COST_UNITS[i];
			totalDeuteriumCostEnemyFleet += initialEnemyUnits[i]*DEUTERIUM_COST_UNITS[i];
		}
		
		initialCostFleet[0][0] = totalMetalCostPlanetFleet;
		initialCostFleet[0][1] = totalDeuteriumCostPlanetFleet;
		initialCostFleet[1][0] = totalMetalCostEnemyFleet;
		initialCostFleet[1][1] = totalDeuteriumCostEnemyFleet;
		
		return initialCostFleet;
	}
	
	public int[][] calculateResourcesLosses(int[] destroyedPlanetUnits, int[] destroyedEnemyUnits){
		int[][] resourcesLosses = new int[2][3];
		
		int totalMetalLossesPlanetFleet = 0;
		int totalDeuteriumLossesPlanetFleet = 0;
		
		for (int i=0; i < destroyedPlanetUnits.length; i++) {
			totalMetalLossesPlanetFleet += destroyedPlanetUnits[i]*METAL_COST_UNITS[i];
			totalDeuteriumLossesPlanetFleet += destroyedPlanetUnits[i]*DEUTERIUM_COST_UNITS[i];
		}
		
		int totalMetalLossesEnemyFleet = 0;
		int totalDeuteriumLossesEnemyFleet = 0;
		
		for (int i=0; i < destroyedEnemyUnits.length; i++) {
			totalMetalLossesEnemyFleet += destroyedEnemyUnits[i]*METAL_COST_UNITS[i];
			totalDeuteriumLossesEnemyFleet += destroyedEnemyUnits[i]*DEUTERIUM_COST_UNITS[i];
		}
		
		resourcesLosses[0][0] = totalMetalLossesPlanetFleet;
		resourcesLosses[0][1] = totalDeuteriumLossesPlanetFleet;
		resourcesLosses[0][2] = totalMetalLossesPlanetFleet + (5 * totalDeuteriumLossesPlanetFleet);
		resourcesLosses[1][0] = totalMetalLossesEnemyFleet;
		resourcesLosses[1][1] = totalDeuteriumLossesEnemyFleet;
		resourcesLosses[1][2] = totalMetalLossesEnemyFleet + (5 * totalDeuteriumLossesEnemyFleet);
		
		return resourcesLosses;
	}
	
	public int[] getWasteMetalDeuterium(int planet_id, int num_battle) {
		int[] wasteMetalDeuterium = new int[2];
		
		query = "SELECT waste_metal_generated, waste_deuterium_generated FROM battle_stats WHERE planet_id = ? AND num_battle = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, num_battle);
			rs = ps.executeQuery();
			
			rs.next();
			wasteMetalDeuterium[0] = rs.getInt(1);
			wasteMetalDeuterium[1] = rs.getInt(2);
			
		} catch (SQLException e) {
			System.err.println("getDestroyedEnemyUnits() failed!");
			e.printStackTrace();
		}
		
		return wasteMetalDeuterium;
	}
	
	public String getBattleSummary(int planet_id, int num_battle) {
		
		String battle_summary = "";
		int battles_counter = getBattlesCounter(planet_id);
		int[] initialPlanetUnits = getInitialPlanetUnits(planet_id,num_battle);
		int[] destroyedPlanetUnits = getDestroyedPlanetUnits(planet_id,num_battle);
		int[] initialEnemyUnits = getInitialEnemyUnits(planet_id,num_battle);
		int[] destroyedEnemyUnits = getDestroyedEnemyUnits(planet_id, num_battle);
		
		int[][] initialCostFleet = calculateInitialCostFleet(initialPlanetUnits, initialEnemyUnits);
		int[][] resourcesLosses = calculateResourcesLosses(destroyedPlanetUnits, destroyedEnemyUnits);
		
		int[] wasteMetalDeuterium = getWasteMetalDeuterium(planet_id,num_battle);
		
		
		battle_summary = "BATTLE NUMBER: " + battles_counter + "\n" + "\n" + "BATTLE STATISTICS" + "\n" + "\n";
		
		battle_summary += String.format("%-27s%10s%10s      %-27s%10s%10s", "PLANET ARMY", "Units", "Drops", "ENEMY ARMY","Units","Drops") + "\n" + "\n";
		
		
		battle_summary += String.format("%-27s%10d%10d      %-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.LIGHTHUNTER.ordinal()],initialPlanetUnits[0],destroyedPlanetUnits[0],MILITARY_UNIT_NAMES[MilitaryUnitOrder.LIGHTHUNTER.ordinal()],initialEnemyUnits[0],destroyedEnemyUnits[0]) + "\n";
		battle_summary += String.format("%-27s%10d%10d      %-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.HEAVYHUNTER.ordinal()],initialPlanetUnits[1],destroyedPlanetUnits[1],MILITARY_UNIT_NAMES[MilitaryUnitOrder.HEAVYHUNTER.ordinal()],initialEnemyUnits[1],destroyedEnemyUnits[1]) + "\n";
		battle_summary += String.format("%-27s%10d%10d      %-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.BATTLESHIP.ordinal()],initialPlanetUnits[2],destroyedPlanetUnits[2],MILITARY_UNIT_NAMES[MilitaryUnitOrder.BATTLESHIP.ordinal()],initialEnemyUnits[2],destroyedEnemyUnits[2]) + "\n";
		battle_summary += String.format("%-27s%10d%10d      %-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.ARMOREDSHIP.ordinal()],initialPlanetUnits[3],destroyedPlanetUnits[3],MILITARY_UNIT_NAMES[MilitaryUnitOrder.ARMOREDSHIP.ordinal()],initialEnemyUnits[3],destroyedEnemyUnits[3]) + "\n";
		battle_summary += String.format("%-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.MISSILELAUNCHER.ordinal()],initialPlanetUnits[4],destroyedPlanetUnits[4]) + "\n";
		battle_summary += String.format("%-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.IONCANNON.ordinal()],initialPlanetUnits[5],destroyedPlanetUnits[5]) + "\n";
		battle_summary += String.format("%-27s%10d%10d", MILITARY_UNIT_NAMES[MilitaryUnitOrder.PLASMACANNON.ordinal()],initialPlanetUnits[6],destroyedPlanetUnits[6]) + "\n" + "\n";
		
		
		battle_summary += Printing.printLineChar('*', 100);
		battle_summary += String.format("%-47s      %-47s", "Cost Planet Army", "Cost Enemy Army") + "\n" + "\n";
		battle_summary += String.format("%-15s%15d%23s%-15s%15d", "Metal:", initialCostFleet[0][0], "", "Metal:",initialCostFleet[1][0])+"\n";
		battle_summary += String.format("%-15s%15d%23s%-15s%15d", "Deuterium:", initialCostFleet[0][1], "", "Deuterium:",initialCostFleet[1][1])+"\n"+"\n";
		
		battle_summary += Printing.printLineChar('*', 100);
		battle_summary += String.format("%-47s      %-47s", "Losses Planet Army", "Losses Enemy Army") + "\n" + "\n";
		battle_summary += String.format("%-15s%15d%23s%-15s%15d", "Metal:", resourcesLosses[0][0], "", "Metal:",resourcesLosses[1][0])+"\n";
		battle_summary += String.format("%-15s%15d%23s%-15s%15d", "Deuterium:", resourcesLosses[0][1], "", "Deuterium:",resourcesLosses[1][1])+"\n";
		battle_summary += String.format("%-15s%15d%23s%-15s%15d", "Weighted:", resourcesLosses[0][2], "", "Weighted:",resourcesLosses[1][2])+"\n"+"\n";
		
		battle_summary += Printing.printLineChar('*', 100);
		battle_summary += "Waste Generated:\n";
		battle_summary += String.format("%-15s%15d", "Metal:", wasteMetalDeuterium[0])+"\n";
		battle_summary += String.format("%-15s%15d", "Deuterium:", wasteMetalDeuterium[1])+"\n";
		
		if (resourcesLosses[0][2] <= resourcesLosses[1][2]) {
			battle_summary += "\n" + "Battle winned by PLANET. We collect rubble." + "\n";
		}
		else {
			battle_summary += "\n" + "Battle winned by ENEMY. We do not collect rubble." + "\n";
			battle_summary += "\n" + Printing.printStringCentred("ENEMY WINS!!", '=', 60);
		}
		
		return battle_summary;
	}
	
	
	// CONVERT INTO XML
	public Document convertIntoXML(int planet_id, int num_battle) {
		// Gather data from the database
		
		int[] initialPlanetUnits = getInitialPlanetUnits(planet_id,num_battle);
		int[] destroyedPlanetUnits = getDestroyedPlanetUnits(planet_id,num_battle);
		int[] initialEnemyUnits = getInitialEnemyUnits(planet_id,num_battle);
		int[] destroyedEnemyUnits = getDestroyedEnemyUnits(planet_id, num_battle);
		
		int[][] initialCostFleet = calculateInitialCostFleet(initialPlanetUnits, initialEnemyUnits);
		int[][] resourcesLosses = calculateResourcesLosses(destroyedPlanetUnits, destroyedEnemyUnits);
		
		int[] wasteMetalDeuterium = getWasteMetalDeuterium(planet_id,num_battle);
		
		// Build the XML document
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			
			Element battle_summary = doc.createElement("battle_summary");
			
			// PLANET STATS
			Element planet = doc.createElement("planet");
			Element planet_troops = doc.createElement("troops");
			
			for (int i=0; i < initialPlanetUnits.length; i++) {
				Element planet_troop = doc.createElement("troop");
				Element troop_name = doc.createElement("name");
				troop_name.setTextContent(MILITARY_UNIT_NAMES[i]);
				Element troop_units = doc.createElement("units");
				troop_units.setTextContent(String.valueOf(initialPlanetUnits[i]));
				Element troop_drops = doc.createElement("drops");
				troop_drops.setTextContent(String.valueOf(destroyedPlanetUnits[i]));
				
				planet_troop.appendChild(troop_name);
				planet_troop.appendChild(troop_units);
				planet_troop.appendChild(troop_drops);
				
				planet_troops.appendChild(planet_troop);
			}
			
			planet.appendChild(planet_troops);
			
			Element planet_cost = doc.createElement("cost");
			
			Element planet_metal_cost = doc.createElement("metal");
			planet_metal_cost.setTextContent(String.valueOf(initialCostFleet[0][0]));
			
			Element planet_deuterium_cost = doc.createElement("deuterium");
			planet_deuterium_cost.setTextContent(String.valueOf(initialCostFleet[0][1]));
			
			planet_cost.appendChild(planet_metal_cost);
			planet_cost.appendChild(planet_deuterium_cost);
			planet.appendChild(planet_cost);
			
			Element planet_losses = doc.createElement("losses");
			
			Element planet_metal_losses = doc.createElement("metal");
			planet_metal_losses.setTextContent(String.valueOf(resourcesLosses[0][0]));
			
			Element planet_deuterium_losses = doc.createElement("deuterium");
			planet_deuterium_losses.setTextContent(String.valueOf(resourcesLosses[0][1]));
			
			Element planet_weighed_losses = doc.createElement("weighed");
			planet_weighed_losses.setTextContent(String.valueOf(resourcesLosses[0][2]));
			
			planet_losses.appendChild(planet_metal_losses);
			planet_losses.appendChild(planet_deuterium_losses);
			planet_losses.appendChild(planet_weighed_losses);
			planet.appendChild(planet_losses);
			
			battle_summary.appendChild(planet);
			
			
			// ENEMY STATS
			Element enemy = doc.createElement("enemy");
			Element enemy_troops = doc.createElement("troops");
			
			for (int i=0; i < initialEnemyUnits.length; i++) {
				Element enemy_troop = doc.createElement("troop");
				Element troop_name = doc.createElement("name");
				troop_name.setTextContent(MILITARY_UNIT_NAMES[i]);
				Element troop_units = doc.createElement("units");
				troop_units.setTextContent(String.valueOf(initialEnemyUnits[i]));
				Element troop_drops = doc.createElement("drops");
				troop_drops.setTextContent(String.valueOf(destroyedEnemyUnits[i]));
				
				enemy_troop.appendChild(troop_name);
				enemy_troop.appendChild(troop_units);
				enemy_troop.appendChild(troop_drops);
				
				enemy_troops.appendChild(enemy_troop);
			}
			
			enemy.appendChild(enemy_troops);
			
			Element enemy_cost = doc.createElement("cost");
			
			Element enemy_metal_cost = doc.createElement("metal");
			enemy_metal_cost.setTextContent(String.valueOf(initialCostFleet[1][0]));
			
			Element enemy_deuterium_cost = doc.createElement("deuterium");
			enemy_deuterium_cost.setTextContent(String.valueOf(initialCostFleet[1][1]));
			
			enemy_cost.appendChild(enemy_metal_cost);
			enemy_cost.appendChild(enemy_deuterium_cost);
			enemy.appendChild(enemy_cost);
			
			Element enemy_losses = doc.createElement("losses");
			
			Element enemy_metal_losses = doc.createElement("metal");
			enemy_metal_losses.setTextContent(String.valueOf(resourcesLosses[1][0]));
			
			Element enemy_deuterium_losses = doc.createElement("deuterium");
			enemy_deuterium_losses.setTextContent(String.valueOf(resourcesLosses[1][1]));
			
			Element enemy_weighed_losses = doc.createElement("weighed");
			enemy_weighed_losses.setTextContent(String.valueOf(resourcesLosses[1][2]));
			
			enemy_losses.appendChild(enemy_metal_losses);
			enemy_losses.appendChild(enemy_deuterium_losses);
			enemy_losses.appendChild(enemy_weighed_losses);
			enemy.appendChild(enemy_losses);
			
			battle_summary.appendChild(enemy);
			
			// WASTE
			Element generated_waste = doc.createElement("generated_waste");
			Element metal_waste = doc.createElement("metal");
			metal_waste.setTextContent(String.valueOf(wasteMetalDeuterium[0]));
			
			Element deuterium_waste = doc.createElement("deuterium");
			deuterium_waste.setTextContent(String.valueOf(wasteMetalDeuterium[1]));
			
			generated_waste.appendChild(metal_waste);
			generated_waste.appendChild(deuterium_waste);
			
			battle_summary.appendChild(generated_waste);
			
			// WINNER
			Element winner = doc.createElement("winner");
			
			if (resourcesLosses[0][2] < resourcesLosses[1][2]) {
				winner.setTextContent("Planet");
			}else {
				winner.setTextContent("Enemy");
			}			
			
			battle_summary.appendChild(winner);
			doc.appendChild(battle_summary);
			
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		try {
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		    transformer.transform(new DOMSource(doc), new StreamResult(System.out)); // esto es para mostrar por consola, el de abajo es para guardar el xml
		    //transformer.transform(new DOMSource(doc), new StreamResult(new File("batalla"+num_battle+".xml")));
		    //añadir ruta completa dentro de File()
		} catch (Exception e) {
		    e.printStackTrace();
		}

		
		return doc;
	}
	
	public void getEveryPlanetIdNamesBattles(){
		ArrayList<Integer> planet_ids = new ArrayList<Integer>();
		ArrayList<String> planet_names = new ArrayList<String>();
		ArrayList<ArrayList<Integer>> num_battles = new ArrayList<ArrayList<Integer>>(); 
		
		try {
			rs = stmnt.executeQuery("SELECT planet_id, name_planet FROM planet_stats WHERE battles_counter > 0");
			
			while (rs.next()) {
				planet_ids.add(rs.getInt(1));
				planet_names.add(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < planet_ids.size(); i++) {
			try {
				rs = stmnt.executeQuery("SELECT num_battle FROM battle_stats WHERE planet_id = " + planet_ids.get(i));
				num_battles.add(new ArrayList<Integer>());
				while (rs.next()) {
					num_battles.get(i).add(rs.getInt(1));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		this.planet_ids = planet_ids;
		this.planet_names = planet_names;
		this.num_battles = num_battles;
		
	}
	
	
}
