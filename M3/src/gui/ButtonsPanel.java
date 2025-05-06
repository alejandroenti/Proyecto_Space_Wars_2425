package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import utils.VariablesWindow;

public class ButtonsPanel extends JPanel implements VariablesWindow{
	ImageButton buy, update, reports;
	
	ButtonsPanel(){
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setSize(200,80);
		setBackground(new Color(0,0,0,100));
		setLocation((int)(FRAME_WIDTH/2)-getWidth()/2, FRAME_HEIGHT-getHeight()-100);
		
		try {
			buy = new ImageButton(ImageIO.read(new File("./src/art/shoppingCart.png")));
			update = new ImageButton(ImageIO.read(new File("./src/art/arrowUp.png")));
			reports = new ImageButton(ImageIO.read(new File("./src/art/import.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		buy.setMaximumSize(new Dimension(50,50));
		update.setMaximumSize(new Dimension(50,50));
		reports.setMaximumSize(new Dimension(50,50));
		
		add(Box.createHorizontalStrut(15));
		add(buy);
		add(Box.createHorizontalStrut(10));
		add(update);
		add(Box.createHorizontalStrut(10));
		add(reports);
	}
}
