package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import utils.VariablesWindow;

public class MainWindow extends JFrame implements VariablesWindow {
	
	private final String FRAME_TITLE = "Space Wars";
	private final String URL_LOGO = "./src/art/logo.png";
	
	private JPanel mainPanel;
	private EnemyPanel enemyPanel;
	private BufferedImage appLogo;
	
	public MainWindow() {
		
		setupFrame();
		initMainPanel();
		
		approachEnemy();
		
		this.setVisible(true);
	}
	
	private void setupFrame() {
		
		this.setTitle(FRAME_TITLE);
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		loadIconImage();
	}
	
	private void loadIconImage() {
		
		try {
            appLogo = ImageIO.read(new File(URL_LOGO));
            this.setIconImage(appLogo);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void initMainPanel() {
		
		mainPanel = new JPanel();
		mainPanel.setLayout(null); // Hacemos el Layout completamente flexible y posicionamos de manera absoluta en cuanto a la Ventana

		
		/*
		 * 1. Inicializar Panel del Jugador
		 * 2. Inicializar Panel del Enemigo
		 * 3. Llevar el Panel del Jugador a la posición 0,0
		 * 4. Llevar el Panel del Enemigo a Parla
		 * 5. Inicializar Panel de Opciones
		 * 6. Llevar el Panel de Opciones al Centro Abajo de la Ventana 
		 */
		
		enemyPanel = new EnemyPanel((int)(WIDTH / 2), HEIGHT);
		
		mainPanel.add(enemyPanel);

		this.add(mainPanel);
	}
	
	public void approachEnemy() {
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
			 enemyPanel.enemyComing();
			 
			 if (enemyPanel.getPosX() <= (int)(WIDTH / 2)) {
				 timer.cancel();
			 }
			}
		};
		
		timer.schedule(task, DELAY, (int)(TIME / STEPS));
	}
}
