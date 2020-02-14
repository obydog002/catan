package src.gui.engine;

import javax.swing.*;

import java.awt.*;

import java.awt.image.BufferedImage;

import src.gui.BoardPanel;

// a panal to display resources at the bottom of buy dialog
public class DisplayResourcesPanel extends JPanel
{
	private int[] amount_res;
	
	private static final BufferedImage IMAGE[] = new BufferedImage[5];
	
	static
	{
		IMAGE[0] = BoardPanel.WOOD_DECAL;
		IMAGE[1] = BoardPanel.BRICK_DECAL;
		IMAGE[2] = BoardPanel.SHEEP_DECAL;
		IMAGE[3] = BoardPanel.WHEAT_DECAL;
		IMAGE[4] = BoardPanel.ORE_DECAL;
	}
	
	public DisplayResourcesPanel(int[] amount_res)
	{
		this.amount_res = amount_res;
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D)g;
		
		Dimension size = this.getSize();
		
		int width = (int)size.getWidth();
		int height = (int)size.getHeight();
		
		// each 'box' should fit a fifth of the total width
		int box_width = width/5;
		
		g2D.setColor(Color.BLACK);
		
		int font_size = height + 4;
		g2D.setFont(new Font("Comic Sans MS", Font.BOLD, height));
		
		int x = 0;
		for (int i = 0; i < 5; i++)
		{
			g2D.drawString(amount_res[i] + "", x, height);
				
			x += box_width/2;
			g2D.drawImage(IMAGE[i], x, 0, box_width/2, height, null);
				
			x += box_width/2;
		}
	}
}