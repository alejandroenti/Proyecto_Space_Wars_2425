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
	
	private JPanel mainPanel;
	private PlayerPanel playerPanel;
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
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
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
		
		/*
		 * Hacemos el Layout del Panel Principal completamente flexible y
		 * posicionamos de manera absoluta en cuanto a la Ventana Principal
		 * 
		 */
		mainPanel = new JPanel();
		mainPanel.setLayout(null);

		playerPanel = new PlayerPanel();
		enemyPanel = new EnemyPanel();
		
		mainPanel.add(playerPanel);
		mainPanel.add(enemyPanel);

		this.add(mainPanel);
	}
	
	public void approachEnemy() {
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
			 enemyPanel.enemyComing();
			 if (enemyPanel.getPosX() <= (int)(FRAME_WIDTH / 2)) {
				 timer.cancel();
			 }
			}
		};
		
		timer.schedule(task, APPROACH_DELAY, (int)(APPROACH_TIME / APPROACH_STEPS));
	}
}
