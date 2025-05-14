package main;

import controllers.DatabaseController;
import controllers.InterfaceController;

public class Main {
	
	public static void main(String[] args) {
		// Creamos planeta e interfaz gráfica
		DatabaseController db_controller = new DatabaseController();
		InterfaceController controller = new InterfaceController();
	}
}