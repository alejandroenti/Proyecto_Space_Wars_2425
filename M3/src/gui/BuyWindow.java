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
import utils.Variables;
import utils.VariablesWindow;

public class BuyWindow extends JFrame implements Variables, VariablesWindow {
	
	private JPanel mainPanel;
	private BufferedImage appLogo;
	private ArrayList<UnitPanel> unitPanels;
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
		mainPanel.setBackground(new Color(24, 21, 20, 225));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		mainPanel.add(Box.createVerticalStrut(16));
		
		this.unitPanels = new ArrayList<UnitPanel>();
		
		for (int i = 0; i < MILITARY_UNIT_NAMES.length; i++) {
			UnitPanel unitPanel = new UnitPanel(i);
			unitPanel.setAlignmentX(CENTER_ALIGNMENT);
		
			unitPanels.add(unitPanel);
			mainPanel.add(unitPanel);
			mainPanel.add(Box.createVerticalStrut(16));
		}
		
		btnBuy = new JButton("Buy");
		btnBuy.setAlignmentX(CENTER_ALIGNMENT);
		btnBuy.addMouseListener(new MouseButtonsListener() {
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			
			public void mouseClicked(MouseEvent e) {
				ArrayList<Integer> units = new ArrayList<Integer>();
				ArrayList<Integer> quantity = new ArrayList<Integer>();
				for (UnitPanel unit : unitPanels) {					
					if (unit.getBuyUnits() > 0) {
						units.add(unit.getUnitType());
						quantity.add(unit.getBuyUnits());
					}
				}
				InterfaceController.instance.buyMilitaryUnit(units, quantity);
				dispose();
			}
		});
		
		mainPanel.add(btnBuy);
		mainPanel.add(Box.createVerticalStrut(16));
		
		this.add(mainPanel);
	}
}