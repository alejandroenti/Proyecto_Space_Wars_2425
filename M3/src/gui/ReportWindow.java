package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controllers.DatabaseController;
import events.MouseButtonsListener;
import gui.ReportsWindow.Report;
import utils.Variables;
import utils.VariablesWindow;

public class ReportWindow extends JFrame implements Variables, VariablesWindow {

	private JPanel mainPanel;
	private BufferedImage appLogo;
	private JTextArea report;
	
	private int planetId;
	private int battleId;
	
	public ReportWindow(int planetId, int battleId) {
		super();
		
		this.planetId = planetId;
		this.battleId = battleId;
		
		setupFrame();
		initMainPanel();
		
		this.setVisible(true);
	}

	private void setupFrame() {
		
		this.setTitle("Report");
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
		mainPanel.setBackground(Color.BLACK); 
		
		this.report = new JTextArea();
		this.report.setText(DatabaseController.instance.getBattleSummary(planetId, battleId) + "\n\n" + DatabaseController.instance.getBattleLog(planetId, battleId));
		
		mainPanel.add(report);
		
		this.add(mainPanel);
	}
}
