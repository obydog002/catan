package catan.gui;

import catan.game.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.JPanel;
import javax.imageio.ImageIO;

import java.util.Random;

import java.io.*;

/*
class to draw the board on a JPanel
*/
public class BoardPanel extends JPanel
{
	Catan catan;
	
	// logical side length of each hex
	// then subject to scaling
	public static final int HEX_SIDE_LENGTH = 40;
	
	// margin for width of panel
	public static final int BOARD_WIDTH_MARGIN = 100;
	
	// margin for top of panel
	// 10 + 10 because 10 is getting eaten up somewhere
	public static final int BOARD_HEIGHT_MARGIN_TOP = 100;
	
	// margin for bottom
	public static final int BOARD_HEIGHT_MARGIN_BOTTOM = 200;
	
	// initial width of panel
	public static final int BOARD_WIDTH = 500;
	
	// initial height
	public static final int BOARD_HEIGHT = 500;
	
	static Random rng = new Random();
	
	// rotate the board by 90 degrees
	private boolean rotate = false;
	
	// handles mouse clicks
	private static InputHandler input;
	
	// static colors we use
	private static final Color SEA_BLUE = new Color(220,220,255);
	private static final Color LIGHT_RED = new Color(255,220,220);
	
	private static final Color WOOD_TILE_COL = new Color(155,62,0);
	private static final Color BRICK_TILE_COL = new Color(255,206,208);
	private static final Color SHEEP_TILE_COL = new Color(0,255,33);
	private static final Color WHEAT_TILE_COL = new Color(255,233,137);
	private static final Color ORE_TILE_COL = new Color(192,192,192);
	private static final Color DESERT_TILE_COL = new Color(255,239,216);
	
	// path to catan-master
	private static final File PATH = new File(BoardPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	
	// res/tile path
	private static final String TILE = File.separator + "catan" + File.separator + "res" + File.separator + "tile" + File.separator;
	
	// bufferedimages for tiles
	private static final BufferedImage WOOD_TILE = load_resource(TILE + "wood.png");
	private static final BufferedImage BRICK_TILE = load_resource(TILE + "brick.png");
	private static final BufferedImage SHEEP_TILE = load_resource(TILE + "sheep.png");
	private static final BufferedImage WHEAT_TILE = load_resource(TILE + "wheat.png");
	private static final BufferedImage ORE_TILE = load_resource(TILE + "ore.png");
	private static final BufferedImage DESERT_TILE = load_resource(TILE + "desert.png");
	
	// load a png file, from PATH/name
	// name - location of file relative to PATH
	public static BufferedImage load_resource(String name)
	{
		BufferedImage result = null;
		try
		{
			File f = new File(PATH + name);
			
			result = ImageIO.read(f);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(PATH + name + " could not be read.");
			
			return null;
		}
		
		if (result != null)
			System.out.println(PATH + name + " read successfully.");
		else
			System.out.println(PATH + name + " could not be read. result null.");
	
		return result;
	}
	
	public BoardPanel()
	{
		Dimension pref = new Dimension(BOARD_WIDTH + 2*BOARD_WIDTH_MARGIN, BOARD_HEIGHT + BOARD_HEIGHT_MARGIN_TOP + BOARD_HEIGHT_MARGIN_BOTTOM);
		this.setPreferredSize(pref);
		
		catan = new Catan();
		
		int length = 3;
		int type = 0;
		catan.setup(length, type);
		
		input = new InputHandler(this);
	}
	
	public void toggleRotate()
	{
		rotate = !rotate;
		
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D)g;
		
		drawBoard(g2D);
	}
	
	// currently only supports reguler and extension boards
	// of any length
	// custom drawing must be done for other board types
	public void drawBoard(Graphics2D g)
	{
		Board board = catan.getBoard();
		
		if (board == null) 
			return;
		
		Dimension current_dim = this.getSize();
		
		// logical width and height
		int width = (int)current_dim.getWidth() - 2*BOARD_WIDTH_MARGIN;
		int height = (int)current_dim.getHeight() - BOARD_HEIGHT_MARGIN_TOP - BOARD_HEIGHT_MARGIN_BOTTOM;
		
		// dont draw board if its too small	
		if (width < 1 || height < 1) 
		{
			clear(g, LIGHT_RED);
			return;
		}
		else
			clear(g, SEA_BLUE);
		
		int len = board.getLength();
		
		int x_half_length;
		int y_half_length;
		
		if (rotate)
		{
			if (board.getType() == 1)
			{
				x_half_length = (int)(width/(3*len - 1));
				y_half_length = (int)(height/(3*len));
			}
			else
			{
				x_half_length = (int)(width/(2*len + 2));
				y_half_length = (int)(height/(4*len - 2));
			}
		}
		else
		{
			if (board.getType() == 1)
			{
				x_half_length = (int)(width/(3*len));
				y_half_length = (int)(height/(3*len - 1));
			}
			else
			{
				x_half_length = (int)(width/(4*len - 2));
				y_half_length = (int)(height/(2*len + 2));
			}
		}
			
		// margins from edge
		int x_margin = BOARD_WIDTH_MARGIN;
		int y_margin = BOARD_HEIGHT_MARGIN_TOP;
			
		Tile[][] tiles = board.getTiles();
		Edge[][] edges = board.getEdges();
		Vertex[][] vertices = board.getVertices();
		
		// to control where edges and vertices go
		Point[][] bounds = new Point[vertices.length][];
		for (int i = 0; i < bounds.length; i++)
		{
			bounds[i] = new Point[vertices[i].length];
			
			for (int j = 0; j < bounds[i].length; j++)
			{
				bounds[i][j] = new Point();
			}
		}
		
		// control drawing in correct position
		boolean reverse = false;
		int r = len - 1;
		
		if (rotate)
		{
			// tiles
			for (int i = 0; i < tiles.length; i++)
			{
				for (int j = 0; j < tiles[i].length; j++)
				{
					// distances to center hex
					int x_dist = x_margin + (int)(x_half_length*(1 + 1.5*(tiles.length - 1 - i)));
					int y_dist = y_margin + y_half_length*(r + 1 + 2*j);
					
					drawHex(g, x_dist, y_dist, x_half_length, y_half_length, true, tiles[i][j]);
					
					// bounds 
					int top_y_index = 2*i;
					
					if (reverse)
						bounds[top_y_index + 1][j].move(x_dist + x_half_length, y_dist);
					else
						bounds[top_y_index][j].move(x_dist + x_half_length, y_dist);
					
					bounds[top_y_index + 1][j].move(x_dist + x_half_length/2, y_dist - y_half_length);
					bounds[top_y_index + 1][j + 1].move(x_dist + x_half_length/2, y_dist + y_half_length);
					bounds[top_y_index + 2][j].move(x_dist - x_half_length/2, y_dist - y_half_length);
					bounds[top_y_index + 2][j + 1].move(x_dist - x_half_length/2, y_dist + y_half_length);
					
					if (reverse)
						bounds[top_y_index + 3][j].move(x_dist - x_half_length, y_dist);
					else
						bounds[top_y_index + 3][j + 1].move(x_dist - x_half_length, y_dist);
				}
				
				if (reverse)
					r++;
				else
					r--;
				
				if (r == 0)
					reverse = true;
			}
			
			// edges
			
			int mid = edges.length/2;
			for (int j = 0; j < edges[mid].length; j++)
			{
				drawEdge(g, bounds[mid][j].x, bounds[mid][j].y, bounds[mid + 1][j].x, bounds[mid + 1][j].y, edges[mid][j]);
			}
			
			for (int j = 0; j < edges[mid - 1].length/2; j++)
			{
				drawEdge(g, bounds[mid][j].x, bounds[mid][j].y, bounds[mid - 1][j].x, bounds[mid - 1][j].y, edges[mid - 1][2*j]);
				drawEdge(g, bounds[mid - 1][j].x, bounds[mid - 1][j].y, bounds[mid][j + 1].x, bounds[mid][j + 1].y, edges[mid - 1][2*j + 1]);
				
				drawEdge(g, bounds[mid + 1][j].x, bounds[mid + 1][j].y, bounds[mid + 2][j].x, bounds[mid + 2][j].y, edges[mid + 1][2*j]);
				drawEdge(g, bounds[mid + 2][j].x, bounds[mid + 2][j].y, bounds[mid + 1][j + 1].x, bounds[mid + 1][j + 1].y, edges[mid + 1][2*j + 1]);
			}
			
			for (int i = 1; i < len; i++)
			{
				for (int j = 0; j < edges[mid - 2*i].length; j++)
				{
					drawEdge(g, bounds[mid - 2*i][j].x, bounds[mid - 2*i][j].y, bounds[mid - 2*i + 1][j].x, bounds[mid - 2*i + 1][j].y, edges[mid - 2*i][j]);
					drawEdge(g, bounds[mid + 2*i][j].x, bounds[mid + 2*i][j].y, bounds[mid + 2*i + 1][j].x, bounds[mid + 2*i + 1][j].y, edges[mid + 2*i][j]);
				}
				
				for (int j = 0; j < edges[mid - 2*i - 1].length/2; j++)
				{
					drawEdge(g, bounds[mid - 2*i][j].x, bounds[mid - 2*i][j].y, bounds[mid - 2*i - 1][j].x, bounds[mid - 2*i - 1][j].y, edges[mid - 2*i - 1][2*j]);
					drawEdge(g, bounds[mid - 2*i - 1][j].x, bounds[mid - 2*i - 1][j].y, bounds[mid - 2*i][j + 1].x, bounds[mid - 2*i][j + 1].y, edges[mid - 2*i - 1][2*j + 1]);
					
					drawEdge(g, bounds[mid + 2*i + 1][j].x, bounds[mid + 2*i + 1][j].y, bounds[mid + 2*i + 2][j].x, bounds[mid + 2*i + 2][j].y, edges[mid + 2*i - 1][2*j]);
					drawEdge(g, bounds[mid + 2*i + 2][j].x, bounds[mid + 2*i + 2][j].y, bounds[mid + 2*i + 1][j + 1].x, bounds[mid + 2*i + 1][j + 1].y, edges[mid + 2*i - 1][2*j + 1]);
					
				}
			}
			
			// vertices
			for (int i = 0; i < vertices.length; i++)
			{
				for (int j = 0; j < vertices[i].length; j++)
				{
					drawVertex(g, bounds[i][j].x, bounds[i][j].y, vertices[i][j]);
				}
			}
		}
		else
		{
			// tiles
			for (int i = 0; i < tiles.length; i++)
			{
				for (int j = 0; j < tiles[i].length; j++)
				{
					// distances to center of hex
					int x_dist = x_margin + x_half_length*(r + 1 + 2*j);
					int y_dist = y_margin + (int)(y_half_length*(1 + 1.5*i));
					
					drawHex(g, x_dist, y_dist, x_half_length, y_half_length, false, tiles[i][j]);
					
					// set bounds for tiles
					int top_y_index = 2*i;
					
					if (reverse)
						bounds[top_y_index + 1][j].move(x_dist, y_dist - y_half_length);
					else
						bounds[top_y_index][j].move(x_dist, y_dist - y_half_length);
					
					bounds[top_y_index + 1][j].move(x_dist - x_half_length, y_dist - y_half_length/2);
					bounds[top_y_index + 1][j + 1].move(x_dist + x_half_length, y_dist - y_half_length/2);
					bounds[top_y_index + 2][j].move(x_dist - x_half_length, y_dist + y_half_length/2);
					bounds[top_y_index + 2][j + 1].move(x_dist + x_half_length, y_dist + y_half_length/2);
					
					if (reverse)
						bounds[top_y_index + 3][j].move(x_dist, y_dist + y_half_length);
					else
						bounds[top_y_index + 3][j + 1].move(x_dist, y_dist + y_half_length);
				}
				
				if (reverse)
					r++;
				else
					r--;
				
				if (r == 0)
					reverse = true;
			}
			
			// edges
			
			int mid = edges.length/2;
			for (int j = 0; j < edges[mid].length; j++)
			{
				drawEdge(g, bounds[mid][j].x, bounds[mid][j].y, bounds[mid + 1][j].x, bounds[mid + 1][j].y, edges[mid][j]);
			}
			
			for (int j = 0; j < edges[mid - 1].length/2; j++)
			{
				drawEdge(g, bounds[mid][j].x, bounds[mid][j].y, bounds[mid - 1][j].x, bounds[mid - 1][j].y, edges[mid - 1][2*j]);
				drawEdge(g, bounds[mid - 1][j].x, bounds[mid - 1][j].y, bounds[mid][j + 1].x, bounds[mid][j + 1].y, edges[mid - 1][2*j + 1]);
				
				drawEdge(g, bounds[mid + 1][j].x, bounds[mid + 1][j].y, bounds[mid + 2][j].x, bounds[mid + 2][j].y, edges[mid + 1][2*j]);
				drawEdge(g, bounds[mid + 2][j].x, bounds[mid + 2][j].y, bounds[mid + 1][j + 1].x, bounds[mid + 1][j + 1].y, edges[mid + 1][2*j + 1]);
			}
			
			for (int i = 1; i < len; i++)
			{
				for (int j = 0; j < edges[mid - 2*i].length; j++)
				{
					drawEdge(g, bounds[mid - 2*i][j].x, bounds[mid - 2*i][j].y, bounds[mid - 2*i + 1][j].x, bounds[mid - 2*i + 1][j].y, edges[mid - 2*i][j]);
					drawEdge(g, bounds[mid + 2*i][j].x, bounds[mid + 2*i][j].y, bounds[mid + 2*i + 1][j].x, bounds[mid + 2*i + 1][j].y, edges[mid + 2*i][j]);
				}
				
				for (int j = 0; j < edges[mid - 2*i - 1].length/2; j++)
				{
					drawEdge(g, bounds[mid - 2*i][j].x, bounds[mid - 2*i][j].y, bounds[mid - 2*i - 1][j].x, bounds[mid - 2*i - 1][j].y, edges[mid - 2*i - 1][2*j]);
					drawEdge(g, bounds[mid - 2*i - 1][j].x, bounds[mid - 2*i - 1][j].y, bounds[mid - 2*i][j + 1].x, bounds[mid - 2*i][j + 1].y, edges[mid - 2*i - 1][2*j + 1]);
					
					drawEdge(g, bounds[mid + 2*i + 1][j].x, bounds[mid + 2*i + 1][j].y, bounds[mid + 2*i + 2][j].x, bounds[mid + 2*i + 2][j].y, edges[mid + 2*i - 1][2*j]);
					drawEdge(g, bounds[mid + 2*i + 2][j].x, bounds[mid + 2*i + 2][j].y, bounds[mid + 2*i + 1][j + 1].x, bounds[mid + 2*i + 1][j + 1].y, edges[mid + 2*i - 1][2*j + 1]);
					
				}
			}
			
			// vertices
			for (int i = 0; i < vertices.length; i++)
			{
				for (int j = 0; j < vertices[i].length; j++)
				{
					drawVertex(g, bounds[i][j].x, bounds[i][j].y, vertices[i][j]);
				}
			}
		}
	}
	
	// draw a singular tile
	// g - graphics object to draw on
	// x0 - x coordinate of middle of the hex
	// y0 - y coordinate of middle
	// x_half_length - dist from center of hex to farthest point x-axis direction
	// y_half_length - dist from center to farthest point y-axis direction
	// orient - false means vertex points north, true means edge perp is north
	// tile - tile to draw. null value draws a black empty outline
	public void drawHex(Graphics2D g, int x0, int y0, int x_half_length, int y_half_length, boolean orient, Tile tile)
	{
		g.setColor(Color.BLACK);
		
		BufferedImage im = null;
			
		int res = tile.getResource();
		if (res == 0) 
		{
			g.setColor(WOOD_TILE_COL);
			im = WOOD_TILE;
		}
		else if (res == 1) 
		{
			g.setColor(BRICK_TILE_COL);
			im = BRICK_TILE;
		}
		else if (res == 2) 
		{
			g.setColor(SHEEP_TILE_COL);
			im = SHEEP_TILE;
		}
		else if (res == 3) 
		{
			g.setColor(WHEAT_TILE_COL);
			im = WHEAT_TILE;
		}
		else if (res == 4) 
		{
			g.setColor(ORE_TILE_COL);
			im = ORE_TILE;
		}
		else if (res == 5) 
		{
			g.setColor(DESERT_TILE_COL);
			im = DESERT_TILE;
		}
		else
		{
			g.setColor(Color.RED);
		}
			
		if (orient)
		{
			int[] tri_x = {x0 - x_half_length/2, x0 - x_half_length, x0 - x_half_length/2};
			int[] tri_y = {y0 + y_half_length, y0, y0 - y_half_length};
			
			g.fillPolygon(tri_x, tri_y, 3);
			
			g.fillRect(x0 - x_half_length/2, y0 - y_half_length, x_half_length, 2*y_half_length);
			
			if (im != null)
				g.drawImage(im, x0 - x_half_length/2, y0 - y_half_length, x_half_length, 2*y_half_length, null);
			
			tri_x[0] = x0 + x_half_length/2; tri_x[1] = x0 + x_half_length; tri_x[2] = x0 + x_half_length/2;
			
			g.fillPolygon(tri_x, tri_y, 3);
		}
		else
		{
			int[] tri_x = {x0 - x_half_length, x0, x0 + x_half_length};
			int[] tri_y = {y0 - y_half_length/2, y0 - y_half_length, y0 - y_half_length/2};
			
			g.fillPolygon(tri_x, tri_y, 3);
			
			g.fillRect(x0 - x_half_length, y0 - y_half_length/2, 2*x_half_length, y_half_length);
			
			if (im != null)
				g.drawImage(im, x0 - x_half_length, y0 - y_half_length/2, 2*x_half_length, y_half_length, null);
			
			tri_y[0] = y0 + y_half_length/2; tri_y[1] = y0 + y_half_length; tri_y[2] = y0 + y_half_length/2;
			
			g.fillPolygon(tri_x, tri_y, 3);
		}
	}
	
	// for drawing the edges (roads)
	// g - graphics to draw on
	// x0 - x coord of start of line
	// y0 - y coord of start of line
	// x1 - x coord of end
	// y1 - y coord of end
	// edge - object containing road info
	public void drawEdge(Graphics2D g, int x0, int y0, int x1, int y1, Edge edge)
	{
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(5));
		
		g.drawLine(x0,y0,x1,y1);
		
		/*int r = rng.nextInt(4);
		if (r == 0) g.drawLine(x0, y0, x0 + 2, y0 - 10);
		else if (r == 1) g.drawLine(x0, y0, x0 + 10, y0 - 2);
		else if (r == 2) g.drawLine(x0, y0, x0 - 2, y0 + 10);
		else if (r == 3) g.drawLine(x0, y0, x0 - 10, y0 + 2);*/
	}
	
	// drawing houses
	// g - graphics to draw on
	// x0 - x coord of vertex
	// y0 - y coord of vertex
	// vertex - house, city, port info
	public void drawVertex(Graphics2D g, int x0, int y0, Vertex vertex)
	{
		g.setColor(Color.BLACK);
		g.fillRect(x0 - 5, y0 - 5, 10, 10);
		
		// what to do here
	}
	
	// clears entire screen to specific color
	// g - graphics
	// c - color to clear to
	public void clear(Graphics2D g, Color c)
	{
		g.setColor(c);
		
		Dimension dim = this.getSize();
		
		g.fillRect(0,0, (int)dim.getWidth(), (int)dim.getHeight());
	}
}