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
	
	public void mousePressed(MouseEvent e)
	{
		
	}
	
	public void mouseReleased(MouseEvent e)
	{
		
	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public void mouseClicked(MouseEvent e)
	{
		panel.toggleRotate();
	}

	// mouse motion listener
	// -----------------------------------------
	
	public void mouseDragged(MouseEvent e)
	{
		
	}
	
	public void mouseMoved(MouseEvent e)
	{
		panel.update_mouse_location(e.getX(), e.getY());
	}
	
	// action listener
	// -----------------------------------------
	
	public void actionPerformed(ActionEvent e)
	{
		
	}
}