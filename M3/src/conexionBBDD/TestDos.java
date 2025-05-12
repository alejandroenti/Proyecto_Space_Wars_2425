package pruebaBBDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TestDos {

	public static void main(String[] args) {
		String urlDatos = "jdbc:mysql://planet-wars.clmmsosyssic.eu-north-1.rds.amazonaws.com:3306/SpaceWars?serverTimezone=UTC";
		String usuario = "admin";
		String pass = "proyecto2025";
		
		
		ArrayList<Planets> planetas = new ArrayList<Planets>();
		Planets p1 = new Planets("NewLand", 14000, 9000, 6, 8, 13, 11, 4, 2, 21, 16, 6, 3); 
		Planets p2 = new Planets("KP-17", 13000, 7000, 8, 10, 15, 13, 5, 3, 22, 17, 7, 4);
		
		planetas.add(p1);
		planetas.add(p2);
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver cargado correctamente :) ");

			Connection conn = DriverManager.getConnection(urlDatos, usuario, pass); 
			System.out.println("Conexion creada con exito :)");
			Statement stmnt = conn.createStatement(); //Esto es para la query "SELECT * FROM planet_stats;
			
			//Primera query SELECT
			String query = "SELECT * FROM planet_stats";
			ResultSet rs = stmnt.executeQuery(query); 
			System.out.println("PARTE UNO - NORMAL :D");

			while(rs.next()) {
				System.out.println("DATOS: "+ rs.getString(1) + ", "+ rs.getString(2) +", "+ rs.getInt(3)+", "+ rs.getInt(4)+", "+ rs.getInt(5)+", "+ rs.getInt(6)+ ", "+rs.getInt(7)+", "+ rs.getInt(8)+ ", "+rs.getInt(9)+", "+ rs.getInt(10)+ ", "+rs.getInt(11)+", "+ rs.getInt(12)+ ", "+rs.getInt(13)+", "+ rs.getInt(14));
			}
			

			System.out.println("\nPARTE DOS - UPDATE :D");
			//INSERT planet_stats
			String update = "INSERT INTO planet_stats (name_planet, resource_metal_amount, resource_deuterion_amount, technology_defense_level,"
				    + " technology_attack_level, battles_counter, missile_launcher_remaining, ion_canon_remaining, plasma_canon_remaining, light_hunter_remaining,"
				    + " heavy_hunter_remaining, battleship_remaining, armored_ship_remaining) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

			
			// QUERIES INSERT zzz
			
			// INSERT planet_battle_army
			String update2 = "INSERT INTO planet_battle_army(planet_id, num_battle, light_hunter_built, light_hunter_destroyed, heavy_hunter_built, heavy_hunter_destroyed, "
			               + "battleship_built, battleship_destroyed, armored_ship_built, armored_ship_destroyed) VALUES (?,?,?,?,?,?,?,?,?,?)";

			// INSERT planet_battle_defense
			String update3 = "INSERT INTO planet_battle_defense(planet_id, num_battle, missile_launcher_built, missile_launcher_destroyed, ion_cannon_built, ion_cannon_destroyed, "
			               + "plasma_cannon_built, plasma_cannon_destroyed) VALUES (?,?,?,?,?,?,?,?)";

			// INSERT battle_stats
			String update4 = "INSERT INTO battle_stats(planet_id, num_battle, resource_metal_acquired, resource_deuterion_acquired) VALUES (?,?,?,?)";

			// INSERT enemy_army
			String update5 = "INSERT INTO enemy_army(planet_id, num_battle, light_hunter_threat, light_hunter_destroyed, heavy_hunter_threat, heavy_hunter_destroyed, "
			               + "battleship_threat, battleship_destroyed, armored_ship_threat, armored_ship_destroyed) VALUES (?,?,?,?,?,?,?,?,?,?)";

			// INSERT battle_log
			String update6 = "INSERT INTO battle_log(planet_id, num_battle, num_line, log_entry) VALUES (?,?,?,?)";

			
			PreparedStatement ps = conn.prepareStatement(update, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); // Guarda correctamente datos
			for (Planets p : planetas) {
			    ps.setString(1, p.getName_planet());
			    ps.setInt(2, p.getResource_metal_amount());
			    ps.setInt(3, p.getResource_deuterion_amount());
			    ps.setInt(4, p.getTechnology_defense_level());
			    ps.setInt(5, p.getTechnology_attack_level());
			    ps.setInt(6, p.getBattles_counter());
			    ps.setInt(7, p.getMissile_launcher_remaining());
			    ps.setInt(8, p.getIon_canon_remaining());
			    ps.setInt(9, p.getPlasma_canon_remaining());
			    ps.setInt(10, p.getLight_hunter_remaining());
			    ps.setInt(11, p.getHeavy_hunter_remaining());
			    ps.setInt(12, p.getBattleship_remaining());
			    ps.setInt(13, p.getArmored_ship_remaining());
			    //ps.executeUpdate(); // <- se ejecuta aquí
			    System.out.println("Agregado :)");		
			}

			//Consulta nuevos registros:
			String queryCambios = "SELECT * FROM planet_stats";
			ps = conn.prepareStatement(queryCambios);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				System.out.println("DATOS: "+ rs.getString(1) + ", "+ rs.getString(2) +", "+ rs.getInt(3)+", "+ rs.getInt(4)+", "+ rs.getInt(5)+", "+ rs.getInt(6)+ ", "+rs.getInt(7)+", "+ rs.getInt(8)+ ", "+rs.getInt(9)+", "+ rs.getInt(10)+ ", "+rs.getInt(11)+", "+ rs.getInt(12)+ ", "+rs.getInt(13)+", "+ rs.getInt(14));
			}
			
			//BORRAR REGISTROS
			for (Planets p : planetas) {
				
				update = "DELETE FROM planet_stats WHERE name_planet = ?";
				ps = conn.prepareStatement(update);
				ps.setString(1, p.getName_planet());
				ps.executeUpdate();
				
				System.out.println("ELiminado correctamente");
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("Error en el driver");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SQL exception");
			e.printStackTrace();
		}
	}
}


class Planets{
	private String name_planet;
	private int resource_metal_amount;
	private int resource_deuterion_amount;
	private int technology_defense_level;
	private int technology_attack_level;
	private int battles_counter;
	private int missile_launcher_remaining;
	private int ion_canon_remaining;
	private int plasma_canon_remaining;
	private int light_hunter_remaining;
	private int heavy_hunter_remaining;
	private int battleship_remaining;
	private int armored_ship_remaining;
	
	public Planets(String name_planet, int resource_metal_amount, int resource_deuterion_amount,
			int technology_defense_level, int technology_attack_level, int battles_counter,
			int missile_launcher_remaining, int ion_canon_remaining, int plasma_canon_remaining,
			int light_hunter_remaining, int heavy_hunter_remaining, int battleship_remaining,
			int armored_ship_remaining) {
		super();
		this.name_planet = name_planet;
		this.resource_metal_amount = resource_metal_amount;
		this.resource_deuterion_amount = resource_deuterion_amount;
		this.technology_defense_level = technology_defense_level;
		this.technology_attack_level = technology_attack_level;
		this.battles_counter = battles_counter;
		this.missile_launcher_remaining = missile_launcher_remaining;
		this.ion_canon_remaining = ion_canon_remaining;
		this.plasma_canon_remaining = plasma_canon_remaining;
		this.light_hunter_remaining = light_hunter_remaining;
		this.heavy_hunter_remaining = heavy_hunter_remaining;
		this.battleship_remaining = battleship_remaining;
		this.armored_ship_remaining = armored_ship_remaining;
	}

	public String getName_planet() {
		return name_planet;
	}

	public void setName_planet(String name_planet) {
		this.name_planet = name_planet;
	}

	public int getResource_metal_amount() {
		return resource_metal_amount;
	}

	public void setResource_metal_amount(int resource_metal_amount) {
		this.resource_metal_amount = resource_metal_amount;
	}

	public int getResource_deuterion_amount() {
		return resource_deuterion_amount;
	}

	public void setResource_deuterion_amount(int resource_deuterion_amount) {
		this.resource_deuterion_amount = resource_deuterion_amount;
	}

	public int getTechnology_defense_level() {
		return technology_defense_level;
	}

	public void setTechnology_defense_level(int technology_defense_level) {
		this.technology_defense_level = technology_defense_level;
	}

	public int getTechnology_attack_level() {
		return technology_attack_level;
	}

	public void setTechnology_attack_level(int technology_attack_level) {
		this.technology_attack_level = technology_attack_level;
	}

	public int getBattles_counter() {
		return battles_counter;
	}

	public void setBattles_counter(int battles_counter) {
		this.battles_counter = battles_counter;
	}

	public int getMissile_launcher_remaining() {
		return missile_launcher_remaining;
	}

	public void setMissile_launcher_remaining(int missile_launcher_remaining) {
		this.missile_launcher_remaining = missile_launcher_remaining;
	}

	public int getIon_canon_remaining() {
		return ion_canon_remaining;
	}

	public void setIon_canon_remaining(int ion_canon_remaining) {
		this.ion_canon_remaining = ion_canon_remaining;
	}

	public int getPlasma_canon_remaining() {
		return plasma_canon_remaining;
	}

	public void setPlasma_canon_remaining(int plasma_canon_remaining) {
		this.plasma_canon_remaining = plasma_canon_remaining;
	}

	public int getLight_hunter_remaining() {
		return light_hunter_remaining;
	}

	public void setLight_hunter_remaining(int light_hunter_remaining) {
		this.light_hunter_remaining = light_hunter_remaining;
	}

	public int getHeavy_hunter_remaining() {
		return heavy_hunter_remaining;
	}

	public void setHeavy_hunter_remaining(int heavy_hunter_remaining) {
		this.heavy_hunter_remaining = heavy_hunter_remaining;
	}

	public int getBattleship_remaining() {
		return battleship_remaining;
	}

	public void setBattleship_remaining(int battleship_remaining) {
		this.battleship_remaining = battleship_remaining;
	}

	public int getArmored_ship_remaining() {
		return armored_ship_remaining;
	}

	public void setArmored_ship_remaining(int armored_ship_remaining) {
		this.armored_ship_remaining = armored_ship_remaining;
	}

}








