package catan.gui;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class InputHandler implements MouseListener, ActionListener
{
	BoardPanel panel;
	BoardOptionsPanel board_options_panel;

	public InputHandler(BoardPanel panel, BoardOptionsPanel board_options_panel)
	{
		this.panel = panel;
		this.panel.addMouseListener(this);
		this.board_options_panel = board_options_panel;
		this.board_options_panel.setListeners(this);
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

	public void actionPerformed(ActionEvent e)
	{
		int i = 0, j =0;
		String act = e.getActionCommand();
		if (act == "up")
		{
			i--;
		}
		else if (act == "down")
		{
			i++;
		}
		else if (act == "left")
		{
			j--;
		}
		else if (act == "right")
		{
			j++;
		}
		else if (act == "confirm")
		{

		}

		panel.move_cursor(i, j);
	}
}