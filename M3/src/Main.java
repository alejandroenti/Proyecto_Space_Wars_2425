import controllers.InterfaceController;
import gui.MainWindow;
import planets.Planet;

public class Main {
	
	public static void main(String[] args) {
		InterfaceController controller = new InterfaceController(new Planet(), new MainWindow());
	}
}
