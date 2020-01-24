package src.gui;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class InputHandler implements MouseListener, ActionListener, MouseMotionListener
{
	BoardPanel panel;

	public InputHandler(BoardPanel panel)
	{
		this.panel = panel;
		this.panel.addMouseListener(this);
		this.panel.addMouseMotionListener(this);
	}
	
	// mouse listener
	// -----------------------------------------
	
	// when mouse button held down
	public void mousePressed(MouseEvent e)
	{
		panel.mouse_held(e.getX(), e.getY());
	}
	
	// mouse button released
	public void mouseReleased(MouseEvent e)
	{
		panel.mouse_released(e.getX(), e.getY());
	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public void mouseClicked(MouseEvent e)
	{
		
	}

	// mouse motion listener
	// -----------------------------------------
	
	public void mouseDragged(MouseEvent e)
	{
		
	}
	
	public void mouseMoved(MouseEvent e)
	{
		panel.mouse_move(e.getX(), e.getY());
	}
	
	// action listener
	// -----------------------------------------
	
	public void actionPerformed(ActionEvent e)
	{
		
	}
}