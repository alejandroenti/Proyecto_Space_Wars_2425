package controller;

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
	
	
	public DatabaseController() {
		this.instance = this;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection(urlDatos, usuario, pass); 

			} catch (ClassNotFoundException e) {
				System.out.println("Error en el driver");
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("SQL exception");
				e.printStackTrace();
			}
	}
	
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
			e.printStackTrace();
		}
	}
	
	// Nueva batalla (creamos la batalla e incrementamos el battles_counter)
	
	
	// Actualizamos unidades restantes
	public void updateRemainingUnits(ArrayList[][] armies) {
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
			ps.setInt(8, 0);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
