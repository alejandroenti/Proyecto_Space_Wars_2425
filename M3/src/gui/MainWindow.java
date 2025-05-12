package gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import controllers.InterfaceController;
import utils.VariablesWindow;

public class MainWindow extends JFrame implements VariablesWindow {
	
	private ImagePanel mainPanel, attackerSelectorPanel, defenderSelectorPanel;
	private PlayerPanel playerPanel;
	private EnemyPanel enemyPanel;
	private ButtonsPanel buttonsPanel;
	private BufferedImage appLogo;
	
	public MainWindow() {
		super();
		
		setupFrame();
		initMainPanel();
		
		this.setVisible(true);
	}
	
	public PlayerPanel getPlayerPanel() {
		return playerPanel;
	}

	public EnemyPanel getEnemyPanel() {
		return enemyPanel;
	}
	
	public ImagePanel getAttackerSelectorPanel() {
		return attackerSelectorPanel;
	}
	
	public ImagePanel getDefendeerSelectorPanel() {
		return defenderSelectorPanel;
	}
	
	public ButtonsPanel getButtonsPanel() {
		return buttonsPanel;
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
		mainPanel = new ImagePanel(BACKGROUND_IMAGE);
		mainPanel.setLayout(null);

		playerPanel = new PlayerPanel();
		enemyPanel = new EnemyPanel();
		buttonsPanel = new ButtonsPanel();
		
		attackerSelectorPanel = new ImagePanel(BASE_URL + "selector_attacker.png");
		attackerSelectorPanel.setMaximumSize(new Dimension(48, 48));
		attackerSelectorPanel.setLocation(-1000, 0);
		
		defenderSelectorPanel = new ImagePanel(BASE_URL + "selector_defender.png");
		defenderSelectorPanel.setMaximumSize(new Dimension(48, 48));
		defenderSelectorPanel.setLocation(-1000, 0);
		
		mainPanel.add(defenderSelectorPanel);
		mainPanel.add(buttonsPanel);
		mainPanel.add(playerPanel);
		mainPanel.add(enemyPanel);
		mainPanel.add(attackerSelectorPanel);

		this.add(mainPanel);
	}
	
	public void approachEnemy() {
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
			 enemyPanel.enemyComing();
			 if (enemyPanel.getPosX() <= (int)(FRAME_WIDTH / 2)) {
				 timer.cancel();
				 startBattle();
			 }
			}
		};
		
		timer.schedule(task, APPROACH_DELAY, (int)(APPROACH_TIME / APPROACH_STEPS));
		InterfaceController.instance.createEnemyArmy();
	}
	
	public void startBattle() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				timer.cancel();
				InterfaceController.instance.startBattle();
			}
		};
		
		timer.schedule(task, 6000, 1000);
	}
}
