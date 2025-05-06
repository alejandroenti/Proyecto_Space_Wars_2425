package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class MainWindow extends JFrame{
	
	private final int[] INIT_POS = { -10, 0 };
	
	public MainWindow() {
		
		Toolkit pantalla = Toolkit.getDefaultToolkit();
		Dimension size = pantalla.getScreenSize();
		
		this.setTitle("Space Wars");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setBounds(INIT_POS[0], INIT_POS[1], (int)(size.getWidth() * 10), (int)(size.getHeight() * 10));
		
		this.setVisible(true);
	}
	
	
}
