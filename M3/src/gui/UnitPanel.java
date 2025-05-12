package gui;

import java.awt.Color;
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
import utils.VariablesWindow;

public class UnitPanel extends JPanel implements Variables, VariablesWindow {
	
	private ImagePanel unitPanel;
	private ImageButton btnAddUnit, btnRemoveUnit;
	private BufferedImage backgroundImageBtn;
	private JPanel resourcesCostPanel;
	
	private int unitType;
	private int actualUnits;
	private int buyUnits;

	public UnitPanel(int unitType) {
		super();
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBackground(Color.GREEN);
		this.setMaximumSize(new Dimension((int)(FRAME_WIDTH / 2) - 32, FRAME_HEIGHT));
		
		this.unitType = unitType;
		this.actualUnits = InterfaceController.instance.getPlayerUnitNumber(unitType);
		this.buyUnits = 0;
		
		this.unitPanel = new ImagePanel(BASE_URL + "ships_" + unitType + "_0.png");
		try {
			backgroundImageBtn = ImageIO.read(new File(BASE_URL + "buttonPanelBackground.png"));
			this.btnAddUnit = new ImageButton(ImageIO.read(new File(BASE_URL + "plus.png")), backgroundImageBtn);
			this.btnRemoveUnit = new ImageButton(ImageIO.read(new File(BASE_URL + "minus.png")), backgroundImageBtn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.resourcesCostPanel = new JPanel();
		resourcesCostPanel.setLayout(new BoxLayout(resourcesCostPanel, BoxLayout.Y_AXIS));
		resourcesCostPanel.setOpaque(false);
		
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
		
		this.unitPanel.setMaximumSize(new Dimension(48, 48));
		this.btnAddUnit.setMaximumSize(new Dimension(32, 32));
		this.btnRemoveUnit.setMaximumSize(new Dimension(32, 32));
		this.resourcesCostPanel.setMaximumSize(new Dimension(28, 52));
		
		this.resourcesCostPanel.add(Box.createVerticalStrut(2));
		this.resourcesCostPanel.add(new ImagePanel(BASE_URL + "metal.png"));
		this.resourcesCostPanel.add(Box.createVerticalStrut(2));
		this.resourcesCostPanel.add(new ImagePanel(BASE_URL + "deuterium.png"));
		
		this.add(Box.createHorizontalStrut(256));
		this.add(btnRemoveUnit);
		this.add(Box.createHorizontalStrut(8));
		this.add(unitPanel);
		this.add(Box.createHorizontalStrut(8));
		this.add(btnAddUnit);
		this.add(Box.createHorizontalStrut(16));
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
		
		g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 12));
		g2d.drawString("Actual " + MILITARY_UNIT_NAMES[unitType] + ": " + actualUnits, 16, (int)(getHeight() / 2) + 6);
		g2d.drawString("Buy: " + buyUnits, 500, (int)(getHeight() / 2) + 6);		
		g2d.drawString("" + METAL_COST_UNITS[unitType], 440, (int)(getHeight() / 2) - 6);
		g2d.drawString("" + DEUTERIUM_COST_UNITS[unitType], 440, (int)(getHeight() / 2) + 18);
	}

}
