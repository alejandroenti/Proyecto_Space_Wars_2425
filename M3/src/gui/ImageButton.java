package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

public class ImageButton extends JButton {
	
	private BufferedImage iconImage;
	private BufferedImage backgroundImage;
	private float alpha;
	
	public ImageButton(BufferedImage iconImage, BufferedImage backgroundImage) {
		super();
		
		super.setOpaque(false);
		super.setContentAreaFilled(false);
		super.setBorderPainted(false);
		
		this.iconImage = iconImage;
		this.backgroundImage = backgroundImage;
		this.alpha = 0.85f;
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
		
		repaint();
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        if (backgroundImage != null) {
        	g.drawImage(backgroundImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), 0, 0, this);
        }
        g.drawImage(iconImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), 0, 0, this);
	}
}
