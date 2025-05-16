package gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controllers.InterfaceController;
import utils.VariablesWindow;

public class MainWindow extends JFrame implements VariablesWindow {
	
	private ImagePanel mainPanel, attackerSelectorPanel, defenderSelectorPanel, bulletPanel, explosionPanel;
	private PlayerPanel playerPanel;
	private String namePlanet;
	private EnemyPanel enemyPanel;
	private ButtonsPanel buttonsPanel;
	private BufferedImage appLogo;
	private ArrayList<BufferedImage> explosionSprites;
	private int remainingSeconds;
	
	public MainWindow() {
		super();
		
		setupFrame();
		
		// Demand Planet Name
		do {
			namePlanet = JOptionPane.showInputDialog(null, "¿What's the name of your planet?", "Name your planet", JOptionPane.QUESTION_MESSAGE);			
		} while (namePlanet == null || namePlanet.contentEquals(""));
		
		System.out.println("Name planet: " + namePlanet);
		
		initMainPanel();
		
		this.setVisible(true);
	}
	
	public PlayerPanel getPlayerPanel() {
		return playerPanel;
	}

	public String getNamePlanet() {
		return namePlanet;
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
	
	public ImagePanel getBullPanel() {
		return bulletPanel;
	}
	
	public ImagePanel getExplosionPanel() {
		return explosionPanel;
	}
	
	public ArrayList<BufferedImage> getExplosionSprites() {
		return explosionSprites;
	}

	private void setupFrame() {
		
		// Setup Window
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
		 * Made MainPanel's layout null for maximum flexibility.
		 * We can position freely any component with absolute positions
		 * 
		 */
		mainPanel = new ImagePanel(BACKGROUND_IMAGE);
		mainPanel.setLayout(null);

		// Initialize every panel
		playerPanel = new PlayerPanel();
		enemyPanel = new EnemyPanel();
		buttonsPanel = new ButtonsPanel();
		
		attackerSelectorPanel = new ImagePanel(BASE_URL + "selector_attacker.png");
		attackerSelectorPanel.setMaximumSize(new Dimension(SELECTOR_PANEL_SIZE, SELECTOR_PANEL_SIZE));
		attackerSelectorPanel.setLocation(RESET_POSITION[0], RESET_POSITION[1]);
		
		defenderSelectorPanel = new ImagePanel(BASE_URL + "selector_defender.png");
		defenderSelectorPanel.setMaximumSize(new Dimension(SELECTOR_PANEL_SIZE, SELECTOR_PANEL_SIZE));
		defenderSelectorPanel.setLocation(RESET_POSITION[0], RESET_POSITION[1]);
		
		bulletPanel = new ImagePanel(BASE_URL + "bullet_0_player.png");
		bulletPanel.setMaximumSize(new Dimension(BULLET_PANEL_SIZE, BULLET_PANEL_SIZE));
		bulletPanel.setLocation(RESET_POSITION[0], RESET_POSITION[1]);
		
		// Load animation spritesheet and split it on different images
		try {
			BufferedImage img = ImageIO.read(new File(BASE_URL + "SpriteExplosion.png"));
			explosionSprites = new ArrayList<BufferedImage>();
			for (int j = 0; j < EXPLOSION_SPRITESHEET_ROWS; j++) {
				for (int i = 0; i < EXPLOSION_SPRITESHEET_COLS; i++) {
					explosionSprites.add(img.getSubimage(EXPLOSION_IMAGE_WIDTH * i, j * EXPLOSION_IMAGE_HEIGHT, EXPLOSION_IMAGE_WIDTH, EXPLOSION_IMAGE_HEIGHT));
				}
			}
		} catch (IOException e) {
			System.out.println("problema cargando la imagen");
			e.printStackTrace();
		}
		
		explosionPanel = new ImagePanel(explosionSprites.get(0));
		explosionPanel.setMaximumSize(new Dimension(EXPLOSION_PANEL_SIZE, EXPLOSION_PANEL_SIZE));
		explosionPanel.setLocation(RESET_POSITION[0], RESET_POSITION[1]);
		
		// Adding panels to MainPanel
		mainPanel.add(explosionPanel);
		mainPanel.add(defenderSelectorPanel);
		mainPanel.add(attackerSelectorPanel);
		mainPanel.add(buttonsPanel);
		mainPanel.add(playerPanel);
		mainPanel.add(enemyPanel);
		mainPanel.add(bulletPanel);

		this.add(mainPanel);
	}
	
	public void approachEnemy() {
		
		// Task for approach enemyPanel to its position on X (FRAME_WIDTH / 2)
		Timer timerApproachEnemy = new Timer();
		TimerTask taskApproachEnemy = new TimerTask() {
			public void run() {
			 enemyPanel.enemyComing();
			 if (enemyPanel.getPosX() <= (int)(FRAME_WIDTH / 2)) {
				 timerApproachEnemy.cancel();
				 startBattle();
			 }
			}
		};
		
		// Task for change timer text on Main Panel
		mainPanel.setFirstLine("Enemy Approaching");
		remainingSeconds = APPROACH_SECONDS;
		Timer timerTimeApproachEnemy = new Timer();
		TimerTask taskTimeApproachEnemy = new TimerTask() {
			public void run() {
				remainingSeconds--;
				
				if (remainingSeconds <= 0) {
					timerTimeApproachEnemy.cancel();
					return;
				}
				
				// Convert from seconds to MM:SS
				int minutes = (int)(remainingSeconds / 60);
				int seconds = remainingSeconds % 60;
				mainPanel.setSecondLine(String.format("%02d:%02d", minutes, seconds));
				mainPanel.repaint();
			}
		};
		
		// Start timers and create Enemy Army
		timerApproachEnemy.schedule(taskApproachEnemy, APPROACH_DELAY, (int)(APPROACH_TIME / APPROACH_STEPS));
		timerTimeApproachEnemy.schedule(taskTimeApproachEnemy, APPROACH_DELAY, EVERY_SEC);
		InterfaceController.instance.createEnemyArmy();
	}
	
	public void startBattle() {
		
		// Delayed task for start the battle
		Timer timerStartBattle = new Timer();
		TimerTask taskStartBattle = new TimerTask() {
			public void run() {
				timerStartBattle.cancel();
				InterfaceController.instance.startBattle();
			}
		};
		
		// Task for update Main Panel timer text
		mainPanel.setFirstLine("Battle Starts in");
		remainingSeconds = BATTLE_STARTS_IN;
		Timer timerTimeStartBattle = new Timer();
		TimerTask taskTimeStartBattle = new TimerTask() {
			public void run() {
				remainingSeconds--;
				
				if (remainingSeconds <= 0) {
					mainPanel.setFirstLine("");
					mainPanel.setSecondLine("");
					repaint();
					timerTimeStartBattle.cancel();
					return;
				}
				
				int minutes = (int)(remainingSeconds / 60);
				int seconds = remainingSeconds % 60;
				mainPanel.setSecondLine(String.format("%02d:%02d", minutes, seconds));
				mainPanel.repaint();
			}
		};
		
		timerStartBattle.schedule(taskStartBattle, BATTLE_STARTS_IN_MILISECS, EVERY_SEC);
		timerTimeStartBattle.schedule(taskTimeStartBattle, 0, EVERY_SEC);
	}
	
	public void showBattleWinner(String message) {
		
		// Task for show Battle Winner and start a new one
		mainPanel.setFirstLine(message);
		repaint();
		remainingSeconds = SHOW_WINNERS;
		Timer timerShowWinner = new Timer();
		TimerTask taskShowWinner = new TimerTask() {
			public void run() {
				remainingSeconds--;
				
				if (remainingSeconds <= 0) {
					mainPanel.setFirstLine("");
					repaint();
					timerShowWinner.cancel();
					
					enemyPanel.resetEnemy();
					buttonsPanel.showPanel();
					approachEnemy();
					return;
				}
			}
		};
		
		timerShowWinner.schedule(taskShowWinner, APPROACH_DELAY, EVERY_SEC);
	}
}
