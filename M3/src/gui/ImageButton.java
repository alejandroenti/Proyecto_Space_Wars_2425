package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

public class ImageButton extends JButton{
	private BufferedImage image;
	
	public ImageButton(BufferedImage image) {
		super();
		this.image = image;
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), 0, 0, this);
	
	}
}
