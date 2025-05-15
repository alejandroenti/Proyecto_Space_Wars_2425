package gui;

import java.awt.Color;
import java.awt.Dimension;
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
import utils.VariablesWindow;

public class ReportsWindow extends JFrame implements VariablesWindow{
	private JPanel mainPanel, dataPanel, buttonsPanel;
	private BufferedImage appLogo;
	private JButton nextBtn, previousBtn;
	
	
	public ReportsWindow() {
		super();
		
		setupFrame();
		initMainPanel();
		
		this.setVisible(true);
	}

	private void setupFrame() {
		
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
		
		DatabaseController.instance.getEveryPlanetIdNamesBattles();
		
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.BLACK); 
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		int num_pages = 0;
		ArrayList<Integer> planetIds = DatabaseController.instance.getPlanet_ids();
		int page = 0;
		int planetPerPage = 10;
		//float data_division = DatabaseController.instance.getPlanet_ids().size() / 10;
		
		
//		if (data_division == (int) (data_division)) {
//			num_pages = (int) (data_division);
//		}else {
//			num_pages = (int) (data_division) + 1;
//		}
		
		//JPanel[] arrayDataPanels = new JPanel[num_pages];
		dataPanel = new JPanel();
		dataPanel.setBackground(Color.YELLOW);
		dataPanel.setMaximumSize(new Dimension(getWidth(), getHeight() - 20));
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		dataPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		
		
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		previousBtn = new JButton("previous");
		buttonsPanel.add(previousBtn);			
		if (page != 0) {
		}
		
		nextBtn = new JButton("next");
		buttonsPanel.add(nextBtn);			
		if (planetIds.size() > planetPerPage * page + planetPerPage) {
		}
		
		
		
//		for (int i = 0; i < arrayDataPanels.length; i++) {
//			arrayDataPanels[i] = new JPanel();
//			nextBtn = new JButton("next");
//			previousBtn = new JButton("previous");
//			
//			if (i == 0) {
//				arrayDataPanels[i].add(nextBtn);
//				
//			}else if (i == arrayDataPanels.length - 1) {
//				arrayDataPanels[i].add(previousBtn);
//				
//			}else {
//				arrayDataPanels[i].add(nextBtn);
//				arrayDataPanels[i].add(previousBtn);
//				
//			}
//		}
		
		mainPanel.add(dataPanel);
		mainPanel.add(buttonsPanel);
		
		this.add(mainPanel);
	}
}
