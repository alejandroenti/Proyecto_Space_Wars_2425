package pruebaBBDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUno {

	public static void main(String[] args) {
		String urlDatos = "jdbc:mysql://planet-wars.clmmsosyssic.eu-north-1.rds.amazonaws.com:3306/SpaceWars?serverTimezone=UTC";

		String usuario = "admin";
		String pass = "proyecto2025";
		String query = "select * from planet_battle_army";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection conn = DriverManager.getConnection(urlDatos, usuario, pass); 
			System.out.println("Conexion creada con exito :)");

			Statement stmnt = conn.createStatement(); 
			ResultSet rs = stmnt.executeQuery(query);
			
			while(rs.next()) {
				System.out.println("PlanetID: " + rs.getString(1) + " - NumBatalla: " + rs.getInt(2));
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





