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

import controller.InterfaceController;
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

	private void setupFrame() {
		
		this.setTitle(FRAME_TITLE);
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
	
	private void initMainPanel() {

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.ORANGE);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		attackUpdatePanel = new UpdatePanel("attack");
		mainPanel.add(attackUpdatePanel);
		attackUpdatePanel.setLocation(0, 0);
		attackUpdatePanel.setBackground(Color.BLUE);
		
		defenseUpdatePanel = new UpdatePanel("defense");	
		mainPanel.add(defenseUpdatePanel);
		defenseUpdatePanel.setLocation(getWidth()/2, 0);
		defenseUpdatePanel.setBackground(Color.YELLOW);
		
		
		this.add(mainPanel);
	}
}
