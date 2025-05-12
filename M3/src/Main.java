<<<<<<< Updated upstream
import controllers.InterfaceController;
=======
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import controller.InterfaceController;
>>>>>>> Stashed changes
import gui.MainWindow;
import planets.Planet;

public class Main {
	
	public static void main(String[] args) {
		// Creamos planeta e interfaz gráfica
		InterfaceController controller = new InterfaceController(new Planet(), new MainWindow());
	}
}
