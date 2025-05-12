package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import planets.Planet;
import utils.Variables;

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
	
	
	
	private int battle_id; 
	
	public int getBattle_id() {
		return battle_id;
	}

	public void setBattle_id(int battle_id) {
		this.battle_id = battle_id;
	}
	
	

	public DatabaseController() {
		this.instance = this;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection(urlDatos, usuario, pass); 

			} catch (ClassNotFoundException e) {
				System.out.println("Error en el driver");
				e.printStackTrace();
			} catch (SQLException e) {
				System.err.println("SQL exception");
				e.printStackTrace();
			}
	}
	
	// METODOS PLANET_STATS
	// Crear planeta
	public void newPlanet(Planet planet) {
		query = "INSERT INTO planet_stats ( name, resource_metal_amount, resource_deuterion_amount, technology_defense_level, technology_attack_level, battles_counter, missile_launcher_remaining, ion_canon_remaining, plasma_canon_remaining, light_hunter_remaining, heavy_hunter_remaining, battleship_remaining, armored_ship_remaining ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, "Earth");
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
	public void updateMetal(Planet planet) {
		query = "UPDATE planet_stats SET resource_metal_amount = ? WHERE planet_id = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet.getMetal());
			ps.setInt(2, planet.getPlanet_id());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("updateMetal() failed!");
			e.printStackTrace();
		}
	}
	
	// Actualizamos deuterio
	public void updateDeuterium(Planet planet) {
		query = "UPDATE planet_stats SET resource_deuterion_amount = ? WHERE planet_id = ?";
		
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet.getDeuterium());
			ps.setInt(2, planet.getPlanet_id());
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
			
			stmnt.executeQuery("UPDATE planet_stats SET battles_counter = " + battles + " WHERE planet_id = " + planet_id);
			
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
	public void uploadBattleStats(int planet_id, int[] wasteMetalDeuterium, boolean win) {
		query = "INSERT INTO battle_stats ( planet_id, resource_metal_acquired, resource_deuterion_acquired ) VALUES (?,?,?)";
		
		if (win) {
			try {
				ps = conn.prepareStatement(query);
				ps.setInt(1, planet_id);
				ps.setInt(2, wasteMetalDeuterium[0]);
				ps.setInt(3, wasteMetalDeuterium[1]);
				ps.executeUpdate();
				
				rs = stmnt.executeQuery("SELECT battle_id FROM battle_stats");
				rs.last();
				int battle_id = rs.getInt(1);
				
				this.setBattle_id(battle_id);
				
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
				ps.executeUpdate();
				
			} catch (SQLException e) {
				System.err.println("uploadBattleStats() failed!");
				e.printStackTrace();
			}
		}
	}
	
	// METODOS BATTLE_LOG
	public void uploadBattleLog(int planet_id, String battleDevelopment) {
		int battle_id = this.getBattle_id();
		
		String line = "";
		int last_new_line = 0;
		
		for (int i = 0; i < battleDevelopment.length(); i++) {
			if (battleDevelopment.charAt(i) == '\n') {
				if (last_new_line == 0) {
					line = battleDevelopment.substring(last_new_line, i);
				}else {
					line = battleDevelopment.substring(last_new_line+1, i);
				}
				
				query = "INSERT INTO battle_log (planet_id, battle_id, log_entry) VALUES (?,?,?)";
				
				try {
					ps = conn.prepareStatement(query);
					ps.setInt(1, planet_id);
					ps.setInt(2, battle_id);
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
	public void uploadPlanetBattleDefense(int planet_id, int[][] initialArmies, ArrayList[][] armies) {
		int battle_id = this.getBattle_id();
		
		query = "INSERT INTO planet_battle_defense (planet_id, battle_id, missile_launcher_built, missile_launcher_destroyed, ion_cannon_built, ion_cannon_destroyed, plasma_canon_built, plasma_canon_destroyed) VALUES (?,?,?,?,?,?,?,?)";
		

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, battle_id);
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
	public void uploadPlanetBattleArmy(int planet_id, int[][] initialArmies, ArrayList[][] armies) {
		int battle_id = this.getBattle_id();
		
		query = "INSERT INTO planet_battle_army (planet_id, battle_id, light_hunter_built, light_hunter_destroyed, heavy_hunter_built, heavy_hunter_destroyed, battleship_built, battleship_destroyed, armored_ship_built, armored_ship_destroyed) VALUES (?,?,?,?,?,?,?,?,?,?)";
		

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, battle_id);
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
	public void uploadEnemyArmy(int planet_id, int[][] initialArmies, ArrayList[][] armies) {
		int battle_id = this.getBattle_id();
		query = "INSERT INTO enemy_army (planet_id, battle_id, light_hunter_threat, light_hunter_destroyed, heavy_hunter_threat, heavy_hunter_destroyed, battleship_threat, battleship_destroyed, armored_ship_threat, armored_ship_destroyed) VALUES (?,?,?,?,?,?,?,?,?,?)";
		

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, planet_id);
			ps.setInt(2, battle_id);
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
	
}
