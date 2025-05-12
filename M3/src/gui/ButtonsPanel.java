package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import events.MouseButtonsListener;
import utils.VariablesWindow;

public class ButtonsPanel extends ImagePanel implements VariablesWindow{
	
	private ImageButton btnBuy, btnUpdate, btnReports;
	private BufferedImage backgroundImage;
	
	public ButtonsPanel() {
		super(BASE_URL + "background_buttons_panel.png");
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setSize(230,80);
		setBackground(new Color(0,0,0,100));
		setLocation((int)(FRAME_WIDTH/2)-getWidth()/2, FRAME_HEIGHT-getHeight()-100);
		
		try {
			backgroundImage = ImageIO.read(new File(BASE_URL + "buttonPanelBackground.png"));
			btnBuy = new ImageButton(ImageIO.read(new File(BASE_URL + "shoppingCart.png")), backgroundImage);
			btnUpdate = new ImageButton(ImageIO.read(new File(BASE_URL + "arrowUp.png")), backgroundImage);
			btnReports = new ImageButton(ImageIO.read(new File(BASE_URL + "import.png")), backgroundImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		btnBuy.setMaximumSize(new Dimension(50,50));
		btnUpdate.setMaximumSize(new Dimension(50,50));
		btnReports.setMaximumSize(new Dimension(50,50));
		
		btnBuy.addMouseListener(new MouseButtonsListener() {
			public void mouseClicked(MouseEvent e) {
				new BuyWindow();
			}
		});
		
		btnUpdate.addMouseListener(new MouseButtonsListener() {
			public void mouseClicked(MouseEvent e) {
				new UpdateWindow();
			}
		});
		
		btnReports.addMouseListener(new MouseButtonsListener() {
			public void mouseClicked(MouseEvent e) {
				new ReportsWindow();
			}
		});
		
		add(Box.createHorizontalStrut(30));
		add(btnBuy);
		add(Box.createHorizontalStrut(10));
		add(btnUpdate);
		add(Box.createHorizontalStrut(10));
		add(btnReports);
		add(Box.createHorizontalStrut(30));
	}
	
	public void hidePanel() {
		this.setVisible(false);
	}
	
	public void showPanel() {
		this.setVisible(true);
	}
}
