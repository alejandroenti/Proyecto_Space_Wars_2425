package gui;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import controllers.InterfaceController;
import events.MouseButtonsListener;
import utils.Variables;

public class UnitPanel extends ImagePanel implements Variables{
	
	private ImagePanel unitPanel;
	private ImageButton btnAddUnit, btnRemoveUnit;
	private BufferedImage backgroundImageBtn;
	private JPanel resourcesCostPanel;
	
	private int unitType;
	private int actualUnits;
	private int buyUnits;

	public UnitPanel(int unitType) {
		super(BASE_URL + "background_unit_panel.png");
		
		// Setup panel
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setMaximumSize(new Dimension((int)(FRAME_WIDTH / 2) - UNIT_PANEL_X_PADDING, FRAME_HEIGHT));
		
		// Initialize counting variables
		this.unitType = unitType;
		this.actualUnits = InterfaceController.instance.getPlayerUnitNumber(unitType);
		this.buyUnits = 0;
		
		// Initialize panel for unit image and add and remove unit buttons
		this.unitPanel = new ImagePanel(BASE_URL + "ships_" + unitType + "_0.png");
		try {
			backgroundImageBtn = ImageIO.read(new File(BASE_URL + "buttonPanelBackground.png"));
			this.btnAddUnit = new ImageButton(ImageIO.read(new File(BASE_URL + "plus.png")), backgroundImageBtn);
			this.btnRemoveUnit = new ImageButton(ImageIO.read(new File(BASE_URL + "minus.png")), backgroundImageBtn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Initialize resources panel
		this.resourcesCostPanel = new JPanel();
		resourcesCostPanel.setLayout(new BoxLayout(resourcesCostPanel, BoxLayout.Y_AXIS));
		resourcesCostPanel.setOpaque(false);
		
		// Add listener to buttons
		this.btnAddUnit.addMouseListener(new MouseButtonsListener() {
			public void mouseClicked(MouseEvent e) {
				buyUnits++;
				repaint();
			}
		});
		
		this.btnRemoveUnit.addMouseListener(new MouseButtonsListener() {
			public void mouseClicked(MouseEvent e) {
				if (buyUnits == 0) return;
				buyUnits--;
				repaint();
			}
		});
		
		// Size Panels and buttons
		this.unitPanel.setMaximumSize(new Dimension(UNIT_PANEL_UNIT_PANEL_SIZE, UNIT_PANEL_UNIT_PANEL_SIZE));
		this.btnAddUnit.setMaximumSize(new Dimension(UNIT_PANEL_BUTTON_SIZE, UNIT_PANEL_BUTTON_SIZE));
		this.btnRemoveUnit.setMaximumSize(new Dimension(UNIT_PANEL_BUTTON_SIZE, UNIT_PANEL_BUTTON_SIZE));
		this.resourcesCostPanel.setMaximumSize(new Dimension(UNIT_PANEL_RESOURCES_COST_PANEL_SIZE[0], UNIT_PANEL_RESOURCES_COST_PANEL_SIZE[1]));
		
		// Construct resources panel
		this.resourcesCostPanel.add(Box.createVerticalStrut(UNIT_PANEL_RESOURCES_COST_PANEL_STRUT));
		this.resourcesCostPanel.add(new ImagePanel(BASE_URL + "metal.png"));
		this.resourcesCostPanel.add(Box.createVerticalStrut(UNIT_PANEL_RESOURCES_COST_PANEL_STRUT));
		this.resourcesCostPanel.add(new ImagePanel(BASE_URL + "deuterium.png"));
		
		// Add panels
		this.add(Box.createHorizontalStrut(UNIT_PANEL_STRUT_LARGE));
		this.add(btnRemoveUnit);
		this.add(Box.createHorizontalStrut(UNIT_PANEL_STRUT_TINY));
		this.add(unitPanel);
		this.add(Box.createHorizontalStrut(UNIT_PANEL_STRUT_TINY));
		this.add(btnAddUnit);
		this.add(Box.createHorizontalStrut(UNIT_PANEL_STRUT_SMALL));
		this.add(resourcesCostPanel);
	}
	
	public int getBuyUnits() {
		return buyUnits;
	}
	
	public int getUnitType() {
		return unitType;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, UNIT_PANEL_FONT_SIZE));
		g2d.drawString("Actual " + MILITARY_UNIT_NAMES[unitType] + ": " + actualUnits, UNIT_PANEL_TEXT_POSITION_X[0], (int)(getHeight() / 2) + (int)(UNIT_PANEL_FONT_SIZE / 2));
		g2d.drawString("Buy: " + buyUnits, UNIT_PANEL_TEXT_POSITION_X[1], (int)(getHeight() / 2) + (int)(UNIT_PANEL_FONT_SIZE / 2));		
		g2d.drawString("" + METAL_COST_UNITS[unitType], UNIT_PANEL_TEXT_POSITION_X[2], (int)(getHeight() / 2) - (int)(UNIT_PANEL_FONT_SIZE / 2));
		g2d.drawString("" + DEUTERIUM_COST_UNITS[unitType], UNIT_PANEL_TEXT_POSITION_X[3], (int)(getHeight() / 2) + (int)(3 * UNIT_PANEL_FONT_SIZE / 2));
	}

}
