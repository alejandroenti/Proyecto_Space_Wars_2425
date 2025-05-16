package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;

import events.MouseButtonsListener;

public class ReportPanel extends ImagePanel {
	
	private int planetId;
	private String planetName;
	private int battleNum;

	public ReportPanel(int planetId, String planetName, int battleNum) {
		super(BASE_URL + "background_unit_panel.png");
		
		this.setBackground(Color.RED);
		this.setMaximumSize(new Dimension(500, 200));
	
		this.planetId = planetId;
		this.planetName = planetName;
		this.battleNum = battleNum;
		
		this.addMouseListener(new MouseButtonsListener() {
			public void mouseEntered(MouseEvent e) {
			}
			
			public void mouseExited(MouseEvent e) {
			}
			
			
			public void mouseClicked(MouseEvent e) {
				new ReportWindow(planetId, battleNum);
			}
		});
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, UNIT_PANEL_FONT_SIZE));
		g2d.drawString("Planet " + planetId + ": " + planetName, 50, (int)(getHeight() / 2) + (int)(UNIT_PANEL_FONT_SIZE / 2));
		g2d.drawString("Battle Number " + battleNum, 300, (int)(getHeight() / 2) + (int)(UNIT_PANEL_FONT_SIZE / 2));
	}

}
