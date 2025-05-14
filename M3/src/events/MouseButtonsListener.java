package events;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gui.ImageButton;

public class MouseButtonsListener extends MouseAdapter {
	
	private int id;
	
	public int getId() {
		return id;
	}
	
	public MouseButtonsListener() {
		super();
		
		this.id = -1;
	}
	
	public MouseButtonsListener(int id) {
		super();
		
		this.id = id;
	}
	
	public void mouseExited(MouseEvent e) {
		((ImageButton)e.getComponent()).setAlpha(0.85f);
	}
	
	public void mouseEntered(MouseEvent e) {
		((ImageButton)e.getComponent()).setAlpha(1f);
	}
	
	public void mouseClicked(MouseEvent e) {}
}
