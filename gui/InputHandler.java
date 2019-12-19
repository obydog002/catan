package catan.gui;

import java.awt.event.*;

public class InputHandler implements MouseListener
{
	BoardPanel panel;
	
	public InputHandler(BoardPanel panel)
	{
		this.panel = panel;
		panel.addMouseListener(this);
	}
	
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
}