package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class MainWindow extends JFrame{
	
	MainWindow(){
		Toolkit pantalla = Toolkit.getDefaultToolkit();
		Dimension size = pantalla.getScreenSize();
		
		setTitle("Space Wars");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(size.getWidth(), size.getHeight());
		setResizable(false);
		setLocationRelativeTo(null);
		
		setVisible(true);
	}
	
	
}
