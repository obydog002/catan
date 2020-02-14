package src.gui.engine;

import java.awt.event.*;

// a handler class to help with mouse detection in BuyPiecePanel
public class BuyMouseHandler extends MouseAdapter
{
	private BuyPiecePanel panel;
	
	public BuyMouseHandler(BuyPiecePanel panel)
	{
		this.panel = panel;
		
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
	}
	
	// when the mouse is clicked
	public void mouseClicked(MouseEvent	e)
	{
		panel.mouse_clicked(e.getX(), e.getY());
	}
	
	// mouse moved but no button held
	public void mouseMoved(MouseEvent e)
	{
		panel.mouse_move(e.getX(), e.getY());
	}
	
	public void mouseExited(MouseEvent e)
	{
		// special value that means the mouse is out of bounds
		panel.mouse_move(-1,-1);
	}
}