package gui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;

import events.MouseButtonsListener;
import utils.VariablesWindow;

public class ButtonsPanel extends ImagePanel implements VariablesWindow{
	
	private ImageButton btnBuy, btnUpdate, btnReports;
	private BufferedImage backgroundImage;
	
	public ButtonsPanel() {
		super(BASE_URL + "background_buttons_panel.png");
		
		// Setup panel
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setSize(BUTTONS_PANEL_SIZE[0], BUTTONS_PANEL_SIZE[1]);
		setLocation((int)(FRAME_WIDTH/2) - getWidth()/2, FRAME_HEIGHT - getHeight() - BUTTONS_PANEL_Y_PADDING);
		
		// Initialize buttons with its images
		try {
			backgroundImage = ImageIO.read(new File(BASE_URL + "buttonPanelBackground.png"));
			btnBuy = new ImageButton(ImageIO.read(new File(BASE_URL + "shoppingCart.png")), backgroundImage);
			btnUpdate = new ImageButton(ImageIO.read(new File(BASE_URL + "arrowUp.png")), backgroundImage);
			btnReports = new ImageButton(ImageIO.read(new File(BASE_URL + "import.png")), backgroundImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Setup buttons sizes
		btnBuy.setMaximumSize(new Dimension(BUTTONS_PANEL_BUTTONS_SIZE, BUTTONS_PANEL_BUTTONS_SIZE));
		btnUpdate.setMaximumSize(new Dimension(BUTTONS_PANEL_BUTTONS_SIZE, BUTTONS_PANEL_BUTTONS_SIZE));
		btnReports.setMaximumSize(new Dimension(BUTTONS_PANEL_BUTTONS_SIZE, BUTTONS_PANEL_BUTTONS_SIZE));
		
		// Add buttons listeners to open its respective window
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
		
		// Add buttons with spaces to the panel
		add(Box.createHorizontalStrut(BUTTONS_PANEL_STRUT_LARGE));
		add(btnBuy);
		add(Box.createHorizontalStrut(BUTTONS_PANEL_STRUT_SMALL));
		add(btnUpdate);
		add(Box.createHorizontalStrut(BUTTONS_PANEL_STRUT_SMALL));
		add(btnReports);
		add(Box.createHorizontalStrut(BUTTONS_PANEL_STRUT_LARGE));
	}
	
	public void hidePanel() {
		this.setVisible(false);
	}
	
	public void showPanel() {
		this.setVisible(true);
	}
}
