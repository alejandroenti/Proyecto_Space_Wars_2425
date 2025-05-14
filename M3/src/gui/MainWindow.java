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

import controllers.InterfaceController;
import utils.VariablesWindow;

public class MainWindow extends JFrame implements VariablesWindow {
	
	private ImagePanel mainPanel, attackerSelectorPanel, defenderSelectorPanel, bulletPanel, explosionPanel;
	private PlayerPanel playerPanel;
	private EnemyPanel enemyPanel;
	private ButtonsPanel buttonsPanel;
	private BufferedImage appLogo;
	private ArrayList<BufferedImage> explosionSprites;
	private int remainingSeconds;
	
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
		
		bulletPanel = new ImagePanel(BASE_URL + "selector_attacker.png");
		bulletPanel.setMaximumSize(new Dimension(24, 24));
		bulletPanel.setLocation(-1000, 0);
		
		try {
			BufferedImage img = ImageIO.read(new File(BASE_URL + "SpriteExplosion.png"));
			explosionSprites = new ArrayList<BufferedImage>();
			//BufferedImage sprite = img.getSubimage(x, y, width, height);
			// cargamos las 48 imágenes de la hoja de sprites en un arrayList
			for (int j = 0; j<6; j++) {
				for (int i = 0; i < 8; i++) {
					explosionSprites.add(img.getSubimage(256*i, j*248, 256, 248 ));
				}
			}
		} catch (IOException e) {
			System.out.println("problema cargando la imagen");
			e.printStackTrace();
		}
		
		explosionPanel = new ImagePanel(explosionSprites.get(0));
		explosionPanel.setMaximumSize(new Dimension(48, 48));
		explosionPanel.setLocation(-1000, 0);
		
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
		
		mainPanel.setFirstLine("Enemy Approaching");
		remainingSeconds = 18;
		Timer timerTimeApproachEnemy = new Timer();
		TimerTask taskTimeApproachEnemy = new TimerTask() {
			public void run() {
				remainingSeconds--;
				
				if (remainingSeconds <= 0) {
					timerTimeApproachEnemy.cancel();
					return;
				}
				
				int minutes = (int)(remainingSeconds / 60);
				int seconds = remainingSeconds % 60;
				mainPanel.setSecondLine(String.format("%d:%2d", minutes, seconds));
				mainPanel.repaint();
			}
		};
		
		timerApproachEnemy.schedule(taskApproachEnemy, APPROACH_DELAY, (int)(APPROACH_TIME / APPROACH_STEPS));
		timerTimeApproachEnemy.schedule(taskTimeApproachEnemy, APPROACH_DELAY, 1000);
		InterfaceController.instance.createEnemyArmy();
	}
	
	public void startBattle() {
		Timer timerStartBattle = new Timer();
		TimerTask taskStartBattle = new TimerTask() {
			public void run() {
				timerStartBattle.cancel();
				InterfaceController.instance.startBattle();
			}
		};
		
		mainPanel.setFirstLine("Battle Starts in");
		remainingSeconds = 6;
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
				mainPanel.setSecondLine(String.format("%d:%2d", minutes, seconds));
				mainPanel.repaint();
			}
		};
		
		timerStartBattle.schedule(taskStartBattle, 6000, 1000);
		timerTimeStartBattle.schedule(taskTimeStartBattle, 0, 1000);
	}
	
	public void showBattleWinner(String message) {
		
		mainPanel.setFirstLine(message);
		remainingSeconds = 5;
		Timer timerShowWinner = new Timer();
		TimerTask taskShowWinner = new TimerTask() {
			public void run() {
				remainingSeconds--;
				
				if (remainingSeconds <= 0) {
					mainPanel.setFirstLine("");
					repaint();
					timerShowWinner.cancel();
					return;
				}
			}
		};
		
		timerShowWinner.schedule(taskShowWinner, 0, 1000);
	}
}
