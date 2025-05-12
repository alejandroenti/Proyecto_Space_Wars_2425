package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.InterfaceController;
import events.MouseButtonsListener;
import exceptions.ResourceException;
import utils.VariablesWindow;

public class UpdatePanel extends JPanel implements VariablesWindow{
	private JPanel mainPanel;
	private JButton btnUpdate;
	
	private int attackLevel;
	private int defenseLevel;
	private String type;
	
	private boolean update;
	
	UpdatePanel(String type){
		this.update = true;
		
		this.type = type;
		
		this.attackLevel = InterfaceController.instance.getPlanetAttackTechnology();
		this.defenseLevel = InterfaceController.instance.getPlanetDefenseTechnology();
		
		mainPanel = new JPanel();
		
		btnUpdate = new JButton("Update");
		
		btnUpdate.addMouseListener(new MouseButtonsListener() {
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			
			public void mouseClicked(MouseEvent e) {
				
				if (type.contentEquals("attack")) {
					try {
						InterfaceController.instance.upgradePlanetAttackTechnology();
						attackLevel ++;
						repaint();
					}catch(ResourceException exc) {
						btnUpdate.setEnabled(false);
						update = false;
						repaint();
					}
					
				}else {
					try {
						InterfaceController.instance.upgradePlanetDefenseTechnology();
						defenseLevel ++;
						repaint();
					}catch(ResourceException exc) {
						btnUpdate.setEnabled(false);
						update = false;
						repaint();
					}
					
				}
				
			}
		});
		
		add(mainPanel);
		
		btnUpdate.setLocation(100,getHeight()-100);
		mainPanel.add(btnUpdate);	
	}
	
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 14));
		
		if (type.contentEquals("attack")) {
			g2d.drawString("ATTACK TECHNOLOGY: ", 60, (int)(getHeight() / 4) + 6);
			g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 100));
			g2d.drawString(""+attackLevel, 120, (int)(getHeight() / 2) + 50);
		}else {
			g2d.drawString("DEFENSE TECHNOLOGY: ", 60, (int)(getHeight() / 4) + 6);
			g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 100));
			g2d.drawString(""+defenseLevel, 120, (int)(getHeight() / 2) + 50);
		}
		
		if (!update) {
			g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 14));
			g2d.setColor(Color.RED);
			g2d.drawString("You don't have the resources needed ", 15, (int)(getHeight()*3 / 4) + 6);
		}
	}
}
