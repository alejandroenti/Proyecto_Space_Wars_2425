package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
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
	
	public void rotateImage(double theta) {
		
		if (image == null) return;
		
		// https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
		double rads = Math.toRadians(theta);
		
		BufferedImage rotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = rotatedImage.createGraphics();
		AffineTransform at = new AffineTransform();
		at.rotate(rads, image.getWidth() / 2, image.getHeight() / 2);
		g2d.setTransform(at);
		g2d.drawImage(image, 0, 0, this);
		g2d.dispose();
		
		image = rotatedImage;
	}
	
	public void changeImage(String urlImage) {
		try {
            image = ImageIO.read(new File(urlImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		repaint();
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
