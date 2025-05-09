package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import utils.Variables;
import utils.VariablesWindow;

public class BuyWindow extends JFrame implements Variables, VariablesWindow {
	
	private JPanel mainPanel;
	private BufferedImage appLogo;
	private JButton btnBuy;
	
	public BuyWindow() {
		super();
		
		setupFrame();
		initMainPanel();
		
		this.setVisible(true);
	}

	private void setupFrame() {
		
		this.setTitle(FRAME_TITLE);
		this.setSize(FRAME_WIDTH / 2, FRAME_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
		mainPanel.setBackground(Color.RED);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		mainPanel.add(Box.createVerticalStrut(16));
		
		for (int i = 0; i < MILITARY_UNIT_NAMES.length; i++) {
			UnitPanel unitPanel = new UnitPanel(i);
			unitPanel.setAlignmentX(CENTER_ALIGNMENT);
		
			System.out.println("Adding Panel " + MILITARY_UNIT_NAMES[i]);
			mainPanel.add(unitPanel);
			mainPanel.add(Box.createVerticalStrut(16));
		}
		
		btnBuy = new JButton("Buy Units");
		btnBuy.setAlignmentX(CENTER_ALIGNMENT);
		
		mainPanel.add(btnBuy);
		mainPanel.add(Box.createVerticalStrut(16));
		
		this.add(mainPanel);
	}
	
	private void paintUnits() {
		
		mainPanel.add(Box.createVerticalStrut(16));
		
		for (int i = 0; i < MILITARY_UNIT_NAMES.length; i++) {
			UnitPanel unitPanel = new UnitPanel(i);
		
			System.out.println("Adding Panel " + MILITARY_UNIT_NAMES[i]);
			mainPanel.add(unitPanel);
			mainPanel.add(Box.createVerticalStrut(16));
		}
		
	}
}