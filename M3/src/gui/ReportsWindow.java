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

import controllers.DatabaseController;
import events.MouseButtonsListener;
import utils.VariablesWindow;

public class ReportsWindow extends JFrame implements VariablesWindow {
	
	private final int REPORTS_PER_PAGE = 10;
	
	private JPanel mainPanel, dataPanel, buttonsPanel;
	private BufferedImage appLogo;
	private JButton nextBtn, previousBtn;
	private ReportPanel[] reportPanels;
	
	private int page;
	private ArrayList<Integer> planetIds;
	private ArrayList<String> planetNames;
	private ArrayList<ArrayList<Integer>> numBattle;
	private ArrayList<Report> reports;
	
	public ReportsWindow() {
		super();
		
		setupFrame();
		initMainPanel();
		
		this.setVisible(true);
	}

	private void setupFrame() {
		
		// Setup Window
		this.setTitle("Choose Report");
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
		
		// Update local reports from database
		DatabaseController.instance.getEveryPlanetIdNamesBattles();
		
		// Initialize Main Panel
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.BLACK); 
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		/*
		 * Initialize reports variables
		 * 	1. Stores panels
		 * 	2. Stores all reports
		 */
		this.reportPanels = new ReportPanel[REPORTS_PER_PAGE];
		this.reports = new ArrayList<Report>();
		
		this.planetIds = DatabaseController.instance.getPlanet_ids();
		this.planetNames = DatabaseController.instance.getPlanet_names();
		this.numBattle = DatabaseController.instance.getNum_battles();
		this.page = 0;
		
		for (int i = 0; i < numBattle.size(); i++) {
			for (int j = 0; j < numBattle.get(i).size(); j++) {
				reports.add(new Report(planetIds.get(i), planetNames.get(i), numBattle.get(i).get(j)));
			}
		}
		
		// Initialize Data Panel
		dataPanel = new JPanel();
		dataPanel.setBackground(Color.BLACK);
		dataPanel.setMaximumSize(new Dimension(getWidth(), getHeight() - 20));
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		dataPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		// Initialize Button Panel
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		previousBtn = new JButton("previous");
		buttonsPanel.add(previousBtn);			
		nextBtn = new JButton("next");
		buttonsPanel.add(nextBtn);
		
		// Add listeners to panels
		previousBtn.addMouseListener(new MouseButtonsListener() {
			public void mouseEntered(MouseEvent e) {
			}
			
			public void mouseExited(MouseEvent e) {
			}
			
			public void mouseClicked(MouseEvent e) {
				if (previousBtn.isEnabled()) {
					page--;
					loadReports();
					UpdateButtons();
				}
			}
		});
		
		nextBtn.addMouseListener(new MouseButtonsListener() {
			public void mouseEntered(MouseEvent e) {
			}
			
			public void mouseExited(MouseEvent e) {
			}
			
			public void mouseClicked(MouseEvent e) {
				if (nextBtn.isEnabled()) {
					page++;
					loadReports();
					UpdateButtons();
				}
			}
		});
		
		// Load reports on the first page and configure buttons
		loadReports();
		UpdateButtons();
		
		mainPanel.add(dataPanel);
		mainPanel.add(buttonsPanel);
		
		this.add(mainPanel);
	}

	private void UpdateButtons() {
		
		/*
		 * Check if we are not on first or last page.
		 * 		IF: disable the button
		 */
		
		if (page == 0) {
			previousBtn.setEnabled(false);
		}
		else {
			previousBtn.setEnabled(true);
		}
		
		if (reports.size() < REPORTS_PER_PAGE * page + REPORTS_PER_PAGE) {
			nextBtn.setEnabled(false);
		}
		else {
			nextBtn.setEnabled(true);
		}
	}
	
	private void cleanPanels() {
		
		// Delete current showed panels
		for (int i = 0; i < REPORTS_PER_PAGE; i++) {
			if (reportPanels[i] != null) {
				dataPanel.remove(reportPanels[i]);
				reportPanels[i] = null;
			}
		}
	}

	private void loadReports() {
		
		// Clean older panels
		cleanPanels();
		
		// Calculate new number of panels
		int panelsToShow = REPORTS_PER_PAGE * page + REPORTS_PER_PAGE;
		if (REPORTS_PER_PAGE * page + REPORTS_PER_PAGE > reports.size()) {
			panelsToShow = reports.size();
		}
		
		// Add the new panels to show
		for (int i = REPORTS_PER_PAGE * page; i < panelsToShow; i++) {
			ReportPanel panel = new ReportPanel(reports.get(i).getId(), reports.get(i).getName(), reports.get(i).getNum());
			reportPanels[i % REPORTS_PER_PAGE] = panel;
			dataPanel.add(panel);
		}	
		
		// Necessary to recalculate elements Layout
		dataPanel.revalidate();
		dataPanel.repaint();
	}
	
	class Report {
		private int id;
		private String name;
		private int num;
		
		public Report(int id, String name, int num) {
			super();
		
			this.id = id;
			this.name = name;
			this.num = num;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public int getNum() {
			return num;
		}
	}
}
