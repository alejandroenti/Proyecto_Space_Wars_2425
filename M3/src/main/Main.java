package main;

import controllers.DatabaseController;
import controllers.InterfaceController;
import gui.MainWindow;
import planets.Planet;

public class Main {
	
	public static void main(String[] args) {
		// Creamos planeta e interfaz gráfica
		DatabaseController db_controller = new DatabaseController();
		InterfaceController controller = new InterfaceController(new Planet(), new MainWindow());
	}
}