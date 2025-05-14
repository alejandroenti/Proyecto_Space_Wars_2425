package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import utils.VariablesWindow;

public class ImagePanel extends JPanel implements VariablesWindow {

	private BufferedImage image;
	private double currentRotation = 0;
	private String firstLine = "", secondLine = "";
	
	public ImagePanel(String urlImage) {
		super();
		setOpaque(false);
		loadImage(urlImage);
	}
	
	public ImagePanel(BufferedImage image) {
		super();
		
		setOpaque(false);
		this.image = image;
	}
	
	public double getCurrentRotation() {
		return currentRotation;
	}
	
	public void setFirstLine(String firstLine) {
		this.firstLine = firstLine;
	}
	
	public void setSecondLine(String secondLine) {
		this.secondLine = secondLine;
	}
	
	public void rotateImage(double theta) {
		
		if (image == null) return;
		
		// https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
		double rads = Math.toRadians(theta);
		currentRotation += rads;
		
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
	
	public void changeImage(BufferedImage image) {

		this.image = image;
		
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
		
		if (!firstLine.isBlank() || !secondLine.isBlank()) {
			Graphics2D g2d = (Graphics2D) g;
			
			g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, MAIN_PANEL_FONT_SIZE_SMALL));
			g2d.setColor(Color.WHITE);
			g2d.drawString(firstLine, MAIN_PANEL_STRING_POSITION[0][0], MAIN_PANEL_STRING_POSITION[0][1]);
			g2d.setFont(new Font("DejaVu Sans Mono", Font.BOLD, MAIN_PANEL_FONT_SIZE_LARGE));
			g2d.drawString(secondLine, MAIN_PANEL_STRING_POSITION[1][0], MAIN_PANEL_STRING_POSITION[1][1]);
		}
	}
}
