package src.gui.engine;

import javax.swing.*;

import java.awt.*;

import java.awt.image.BufferedImage;

import src.gui.BoardPanel;

// panel for drawing each port trade type
public class PortTradePanel extends JPanel
{
	// 0 - wood, 1 - brick, 2 - sheep, 3 - wheat, 4 - ore, 5 - any(3:1)
	int type;
	
	public PortTradePanel(int type)
	{
		this.type = type;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Dimension size = this.getSize();
		
		int width = (int)size.getWidth();
		int height = (int)size.getHeight();
		
		Graphics2D g2D = (Graphics2D)g;
		
		draw_trade(g2D, width, height);
	}
	
	// draw the decal for type and the ratio
	public void draw_trade(Graphics2D g, int width, int height)
	{
		int dec_height = 3*height/4;
		int numb_height = height/4;
		
		int in = 2; // how much to put in
		int out = 1; // how much we get out
		
		BufferedImage img;
		if (type == 0) // wood
		{
			img = BoardPanel.WOOD_DECAL;
		}
		else if (type == 1) // brick
		{
			img = BoardPanel.BRICK_DECAL;
		}
		else if (type == 2) // sheep
		{
			img = BoardPanel.SHEEP_DECAL;
		}
		else if (type == 3) // wheat
		{
			img = BoardPanel.WHEAT_DECAL;
		}
		else if (type == 4) // ore
		{
			img = BoardPanel.ORE_DECAL;
		}
		else if (type == 5) // any 3:1
		{
			img = BoardPanel.FONT_BLUE_QUESTION;
			in = 3;
		}
		else // any 4:1
		{
			img = BoardPanel.FONT_BLUE_QUESTION;
			in = 4;
		}
		
		g.drawImage(img, 0, 0, width, dec_height, null);
		
		// draw numbers
		
		int numb_width = width/5;
		g.drawImage(BoardPanel.FONT[in][0], numb_width, dec_height, numb_width, numb_height, null);
		g.drawImage(BoardPanel.FONT_BLACK_COLON, 2*numb_width, dec_height, numb_width, numb_height, null);
		g.drawImage(BoardPanel.FONT[out][0], 3*numb_width, dec_height, numb_width, numb_height, null);
	}
}