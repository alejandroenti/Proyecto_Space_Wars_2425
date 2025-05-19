package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controllers.DatabaseController;
import utils.VariablesWindow;

public class ReportWindow extends JFrame implements VariablesWindow {

	private JPanel mainPanel;
	private BufferedImage appLogo;
	private JTextArea report;
	private JScrollPane scrollPane;
	
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
		
		// Setup Window
		this.setTitle("Report");
		this.setSize(FRAME_WIDTH - (int)(FRAME_WIDTH / 10), FRAME_HEIGHT / 2);
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
		
		// Initialize main Panel
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.BLACK); 
		mainPanel.setLayout(new BorderLayout());
		
		// Setup TextArea
		this.report = new JTextArea("");
		this.report.setEnabled(false);
		this.report.setFont(new Font("Monospaced", Font.BOLD, 16));
		this.report.setDisabledTextColor(Color.BLACK);
		this.report.setLineWrap(true);
		this.report.setWrapStyleWord(true);
		this.report.setText(DatabaseController.instance.getBattleSummary(planetId, battleId) + "\n\n" + DatabaseController.instance.getBattleLog(planetId, battleId));
		this.report.setCaretPosition(0);
		
		// Setup ScrollPane
		this.scrollPane = new JScrollPane(this.report);
		this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		mainPanel.add(this.scrollPane);
		
		this.add(mainPanel);
	}
}
