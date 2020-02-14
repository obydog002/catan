package src.gui.engine;

import javax.swing.JPanel;

import java.awt.*;

import src.gui.BoardPanel;

// a JPanel for drawing of the piece itself
public class BuyPiecePanel extends JPanel
{
	// so when amount_res changes we can update all other panels
	private BuyDialog parent;
	
	// what piece this panel draws
	// 0 - road, 1 - house, 2 - city, 3 - dev back
	private int type;
	
	// color to draw pieces in
	private Color player_col;
	
	// costs with building this type
	private int[] costs;
	
	// resources we have
	private int[] amount_res;
	
	// whats been bought
	private int[] bought;
	
	// if we are currently hovering over the draw piece part
	private boolean hover = false;
	
	private static final int ALP_MASK1 = 0x77FFFFFF;
	private static final int ALP_MASK2 = 0xEEFFFFFF;
	
	private static final Color WHITE = new Color(0xFFFFFFFF);
	
	private static final Color LIGHT_GRAY = new Color(0xFFCCCCCC);
	// color indicating we dont have enough to buy this item
	private static final Color TRANS_DISABLED = new Color(LIGHT_GRAY.getRGB() & ALP_MASK2, true);
	
	// we hovering over it
	private static final Color TRANS_HOVER = new Color(Color.BLACK.getRGB() & ALP_MASK1, true);
	
	public BuyPiecePanel(BuyDialog parent, int type, Color player_col, int[] costs, int[] amount_res, int[] bought)
	{
		this.parent = parent;
		
		this.type = type;
		
		this.player_col = player_col;
		
		this.costs = costs;
		
		this.amount_res = amount_res;
		
		this.bought = bought;
		
		new BuyMouseHandler(this);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D)g;
		
		Dimension size = this.getSize();
		
		int width = (int)size.getWidth();
		int height = (int)size.getHeight();
		
		// need to leave a little bit of space at the bottom so we can draw costs
		int ad_height = 8*height/9;
		
		boolean compared = compare_costs();
		
		// can buy
		if (compared)
		{
			g2D.setColor(LIGHT_GRAY);
		}
		else
		{
			g2D.setColor(WHITE);
		}
		
		// draw background
		g2D.fillRect(0,0,width,ad_height);
		
		if (type == 0) // road
		{
			int e_width = ad_height/14;
			draw_road(g2D, width/2, ad_height/4, width/2, 3*ad_height/4, e_width);
		}
		else if (type == 1) // house
		{
			int h_width = ad_height/11;
			draw_house(g2D, width/2, ad_height/2, h_width, h_width);
		}
		else if (type == 2) // city
		{
			int c_width = ad_height/11;
			draw_city(g2D, width/2, ad_height/2, c_width, c_width);
		}
		else if (type == 3) // dev card
		{
			int x_margin = width/11;
			int y_margin = ad_height/11;
			draw_dev(g2D, x_margin, y_margin, 9*width/11, 9*ad_height/11);
		}
		
		// draw different transparency effects to how if we hovering/able to buy this item
		
		// can buy this item
		if (compared)
		{
			// we are hovering over it
			if (hover)
			{
				g2D.setColor(TRANS_HOVER);
			
				g2D.fillRect(0, 0, width, ad_height);
			}
		}
		else // cannot buy this item
		{
			g2D.setColor(TRANS_DISABLED);
			
			g2D.fillRect(0, 0, width, ad_height);
		}
		
		// draw costs
		draw_costs(g2D, costs, ad_height, width/6, height/9);
	}
	
	public boolean compare_costs()
	{
		for (int i = 0; i < 5; i++)
		{
			if (amount_res[i] < costs[i]) // cant build this item
				return false;
		}
		
		return true;
	}
	
	public void draw_costs(Graphics2D g, int[] costs, int y, int width, int height)
	{
		int x = 0;
	
		int index = 0;
		int count = 0;
		while (index < 5)
		{
			if (costs[index] > count)
			{
				count++;
				
				Image img;
				if (index == 0)
				{
					img = BoardPanel.WOOD_DECAL;
				}
				else if (index == 1)
				{
					img = BoardPanel.BRICK_DECAL;
				}
				else if (index == 2)
				{
					img = BoardPanel.SHEEP_DECAL;
				}
				else if (index == 3)
				{
					img = BoardPanel.WHEAT_DECAL;
				}
				else
				{
					img = BoardPanel.ORE_DECAL;
				}
				
				g.drawImage(img, x, y, width, height, null);
				
				x += width;
			}
			else
			{
				index++;
				count = 0;
			}
		}
	}
	
	public void draw_road(Graphics2D g, int x0, int y0, int x1, int y1, int edge_width)
	{
		g.setColor(player_col);
			
		g.setStroke(new BasicStroke(edge_width));
		
		g.drawLine(x0,y0,x1,y1);
	}
	
	public void draw_house(Graphics2D g, int x0, int y0, int width, int height)
	{
		g.setColor(player_col);
		
		g.fillRect(x0 - width, y0 - height, 2*width, 2*height);
		
		int[] tri_x = {x0 - width, x0, x0 + width};
		int[] tri_y = {y0 - height, y0 - 2*height, y0 - height};
		
		g.fillPolygon(tri_x, tri_y, 3);
		
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2*width/5));
		
		g.drawLine(x0 - width, y0 - height, x0, y0 - 2*width);
		g.drawLine(x0, y0 - 2*height, x0 + width, y0 - height);
		g.drawLine(x0 + width, y0 - height, x0 + width, y0 + height);
		g.drawLine(x0 + width, y0 + height, x0 - width, y0 + height);
		g.drawLine(x0 - width, y0 + height, x0 - width, y0 - height);
	}
	
	public void draw_city(Graphics2D g, int x0, int y0, int width, int height)
	{
		g.setColor(player_col);
		
		g.fillRect(x0 - 2*width, y0 - height, 3*width, 2*height);
		g.fillRect(x0 - 2*width, y0 - 2*height, 2*width, height);
		
		int[] tri_x = {x0 - 2*width, x0 - width, x0};
		int[] tri_y = {y0 - 2*height, y0 - 3*height, y0 - 2*height};
		
		g.fillPolygon(tri_x, tri_y, 3);
		
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2*width/5));
		
		g.drawLine(x0 - 2*width, y0 - 2*height, x0 - width, y0 - 3*height);
		g.drawLine(x0 - width, y0 - 3*height, x0, y0 - 2*height);
		g.drawLine(x0, y0 - 2*height, x0, y0 - height);
		g.drawLine(x0, y0 - height, x0 + width, y0 - height);
		g.drawLine(x0 + width, y0 - height, x0 + width, y0 + height);
		g.drawLine(x0 + width, y0 + height, x0 - 2*width, y0 + height);
		g.drawLine(x0 - 2*width, y0 + height, x0 - 2*width, y0 - 2*height);
	}
	
	public void draw_dev(Graphics2D g, int x0, int y0, int width, int height)
	{
		g.drawImage(BoardPanel.DEV_BACK, x0, y0, width, height, null);
	}
	
	// method for handling when the mouse has been clicked
	public void mouse_clicked(int x, int y)
	{
		// got enough resources to buy the item, and hovering over
		if (hover && compare_costs())
		{
			for (int i = 0; i < 5; i++)
			{
				amount_res[i] -= costs[i];
			}
			
			bought[type]++;
		}
		
		parent.update();
	}
	
	// when the mouse is hoevered at a certain x,y value
	public void mouse_move(int x, int y)
	{
		Dimension size = this.getSize();
		
		int width = (int)size.getWidth();
		int height = (int)(8*size.getHeight()/9); // adjusting
		
		if (x > 0 && y > 0 && x < width && y < height)
		{
			hover = true;
		}
		else
		{
			hover = false;
		}
		
		repaint();
	}
}