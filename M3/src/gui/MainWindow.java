package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class MainWindow extends JFrame{
	
	private final int WIDTH = 1200, HEIGHT = 675;
	
	public MainWindow() {
		
		this.setTitle("Space Wars");
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		
		this.setVisible(true);
	}
	
	
}
