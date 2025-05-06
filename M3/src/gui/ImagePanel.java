package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private BufferedImage image;
	
	public ImagePanel(String urlImage) {
		super();
		setOpaque(false);
		loadImage(urlImage);
	}
	
	private void loadImage(String urlImage) {
		
		try {
            image = ImageIO.read(new File(urlImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (image != null) {
			g.drawImage(image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), 0, 0, this);
		}
	}
}
