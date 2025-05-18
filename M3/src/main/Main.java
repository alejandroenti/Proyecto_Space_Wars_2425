package main;

import controllers.DatabaseController;
import controllers.InterfaceController;

public class Main {
	
	public static void main(String[] args) {
		// Instancing and constructing both controllers (which will initialize the program)
		DatabaseController db_controller = new DatabaseController();
		InterfaceController controller = new InterfaceController();
	}
}