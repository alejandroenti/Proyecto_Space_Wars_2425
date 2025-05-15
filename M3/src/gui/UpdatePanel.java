package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controllers.InterfaceController;
import events.MouseButtonsListener;
import exceptions.ResourceException;
import utils.VariablesWindow;

public class UpdatePanel extends JPanel implements VariablesWindow{
	private JPanel mainPanel;
	private StatsPanel statsPanel;
	private JButton btnUpdate;
	
	private int attackLevel;
	private int defenseLevel;
	
	
	UpdatePanel(String type){
		this.setLayout(new BorderLayout());
		
		this.attackLevel = InterfaceController.instance.getPlanetAttackTechnology();
		this.defenseLevel = InterfaceController.instance.getPlanetDefenseTechnology();
		
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BorderLayout());
		
		this.btnUpdate = new JButton("Update");
		
		this.btnUpdate.addMouseListener(new MouseButtonsListener() {
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
						JOptionPane.showMessageDialog(null, "[!] Not enough resources to update", "Update alert", JOptionPane.INFORMATION_MESSAGE);
					}
					
				}else {
					try {
						InterfaceController.instance.upgradePlanetDefenseTechnology();
						defenseLevel ++;
						repaint();
					}catch(ResourceException exc) {
						JOptionPane.showMessageDialog(null, "[!] Not enough resources to update", "Update alert", JOptionPane.INFORMATION_MESSAGE);
					}
					
				}
				
			}
		});
		
		try {
			
			statsPanel = new StatsPanel(type,ImageIO.read(new File(BASE_URL + "universe_background.jpg")));
			this.mainPanel.add(statsPanel,BorderLayout.CENTER);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		this.mainPanel.add(btnUpdate,BorderLayout.SOUTH);	
		this.add(mainPanel);
		
	}
	

	class StatsPanel extends JPanel{
		private String type;
		private BufferedImage image;
		
		StatsPanel(String type, BufferedImage image){
			this.type = type;
			this.image = image;
		}
		
		protected void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			
			g.drawImage(image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), 0, 0, this);
			
			Graphics2D g2d = (Graphics2D) g;
			
			g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 14));
			g2d.setColor(Color.WHITE);
			
			if (type.contentEquals("attack")) {
				g2d.drawString("ATTACK TECHNOLOGY: ", (int)(getWidth() / 4), (int)(getHeight() / 4) + 6);
				g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 100));
				g2d.drawString(""+attackLevel, (int)(getWidth()*2/5), (int)(getHeight() / 2) + 50);
			}else {
				g2d.drawString("DEFENSE TECHNOLOGY: ", (int)(getWidth() / 4), (int)(getHeight() / 4) + 6);
				g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 100));
				g2d.drawString(""+defenseLevel, (int)(getWidth()*2/5), (int)(getHeight() / 2) + 50);
			}
			
		}
	}
	
}
