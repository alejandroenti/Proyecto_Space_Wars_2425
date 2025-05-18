package gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controllers.InterfaceController;
import events.MouseButtonsListener;
import utils.VariablesWindow;

public class UpdateWindow extends JFrame implements VariablesWindow{
	private JPanel mainPanel;
	private BufferedImage appLogo;
	private UpdatePanel attackUpdatePanel, defenseUpdatePanel;
	
	public UpdateWindow() {
		super();
		
		setupFrame();
		initMainPanel();
		
		this.setVisible(true);
	}

	// Setting up the JFrame
	private void setupFrame() {
		
		this.setTitle("Upgrade Technology");
		this.setSize(FRAME_WIDTH / 2, FRAME_HEIGHT / 2);
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
	
	/*
	 * The window will have a black JPanel as background
	 * The JPanel's layout is BoxLayout (X_AXIS, to position the attack and defense update panels side by side)
	 */
	private void initMainPanel() {

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.BLACK);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		attackUpdatePanel = new UpdatePanel("attack");
		
		mainPanel.add(attackUpdatePanel);
		mainPanel.add(Box.createHorizontalStrut(10));
		
		defenseUpdatePanel = new UpdatePanel("defense");
		
		mainPanel.add(Box.createHorizontalStrut(10));
		mainPanel.add(defenseUpdatePanel);
		
		
		this.add(mainPanel);
	}
	
}
