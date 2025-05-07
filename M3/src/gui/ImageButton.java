package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

public class ImageButton extends JButton {
	
	private BufferedImage iconImage;
	private BufferedImage backgroungImage;
	
	public ImageButton(BufferedImage iconImage, BufferedImage backgroungImage) {
		super();
		
		this.iconImage = iconImage;
		this.backgroungImage = backgroungImage;
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroungImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), 0, 0, this);
        g.drawImage(iconImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), 0, 0, this);
	}
}
