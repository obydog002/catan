package src.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.JPanel;
import javax.imageio.ImageIO;

import java.util.Random;

import java.io.*;

import src.game.*;

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
	
	private static Color player_col[];
	
	private int cursor_i, cursor_j;

	private int player_selected;

	// static colors we use
	private static final Color SEA_BLUE = new Color(220,220,255);
	private static final Color LIGHT_RED = new Color(255,220,220);
	private static final Color BEIGE = new Color(255, 253, 208);
	private static final Color DARK_GREY = new Color(25,25,25);
	
	private static final Color WOOD_TILE_COL = new Color(155,62,0);
	private static final Color BRICK_TILE_COL = new Color(255,206,208);
	private static final Color SHEEP_TILE_COL = new Color(0,255,33);
	private static final Color WHEAT_TILE_COL = new Color(255,233,137);
	private static final Color ORE_TILE_COL = new Color(192,192,192);
	private static final Color DESERT_TILE_COL = new Color(255,239,216);
	
	// path to catan-master
	private static final File PATH = new File(BoardPanel.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	
	// res/tile path
	private static final String TILE_PATH = File.separator + "res" + File.separator + "tile" + File.separator;
	
	// res/font path
	private static final String FONT_PATH = File.separator + "res" + File.separator + "font" + File.separator;
	
	// bufferedimages for fonts
	// first index is value of number (0 - 0, 1 - 1,..., 9 - 9)
	// second index is color (0 - black, 1 - red)
	private static final BufferedImage FONT[][] = new BufferedImage[10][2];
	
	// load the fonts
	static
	{
		for (int i = 0; i < 10; i++)
		{
			FONT[i][0] = load_resource(FONT_PATH + "b" + i + ".png");
			FONT[i][1] = load_resource(FONT_PATH + "r" + i + ".png");
		}
	}
	
	// bufferedimages for tiles
	private static final BufferedImage WOOD_TILE = load_resource(TILE_PATH + "wood.png");
	private static final BufferedImage BRICK_TILE = load_resource(TILE_PATH + "brick.png");
	private static final BufferedImage SHEEP_TILE = load_resource(TILE_PATH + "sheep.png");
	private static final BufferedImage WHEAT_TILE = load_resource(TILE_PATH + "wheat.png");
	private static final BufferedImage ORE_TILE = load_resource(TILE_PATH + "ore.png");
	private static final BufferedImage DESERT_TILE = load_resource(TILE_PATH + "desert.png");
	
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
	
	public BoardPanel(GameData game_data)
	{
		cursor_i = cursor_j = 0;
		player_selected = 0;

		Dimension pref = new Dimension(BOARD_WIDTH + 2*BOARD_WIDTH_MARGIN, BOARD_HEIGHT + BOARD_HEIGHT_MARGIN_TOP + BOARD_HEIGHT_MARGIN_BOTTOM);
		this.setPreferredSize(pref);
		
		catan = new CatanEngine(game_data);
		
		player_col = catan.get_player_colors();
	}
	
	public void move_cursor(int d_i, int d_j)
	{
		int i = cursor_i + d_i;
		int j = cursor_j + d_j;

		Board board = catan.get_board();
		Vertex vertices[][] = board.get_vertices();

		if (i < 0)
			i = 0;
		else if (i >= vertices.length)
			i = vertices.length - 1;
		
		if (j < 0)
			j = 0;
		else if (j >= vertices[i].length)
			j = vertices[i].length - 1;

		cursor_i = i;
		cursor_j = j;
		
		repaint();
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
		
		if (!rotate)
			draw_board(g2D);
		else
			draw_board_nodes(g2D);
	}
	
	// currently only supports reguler and extension boards
	// of any length
	// custom drawing must be done for other board types
	public void draw_board(Graphics2D g)
	{
		Board board = catan.get_board();
		
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
		
		int len = board.get_length();
		
		int x_half_length;
		int y_half_length;
		
		int x_dist_edge = 0;
		
		if (rotate)
		{
			if (board.get_type() == 1)
			{
				x_half_length = (int)(width/(3*len - 1));
				y_half_length = (int)(height/(4*len - 4));
				
				x_dist_edge = (3*len - 2)*x_half_length;
			}
			else
			{
				x_half_length = (int)(width/(3*len - 1));
				y_half_length = (int)(height/(4*len - 2));
				
				x_dist_edge = (3*len - 2)*x_half_length;
			}
		}
		else
		{
			if (board.get_type() == 1)
			{
				x_half_length = (int)(width/(4*len - 4));
				y_half_length = (int)(height/(3*len - 1));
			}
			else
			{
				x_half_length = (int)(width/(4*len - 2));
				y_half_length = (int)(height/(3*len - 1));
			}
		}
	
		// set edge width
		edge_width = (x_half_length < y_half_length) ? x_half_length/7 : y_half_length/7;
		if (edge_width < 1) 
			edge_width = 1;
		
		// set house/city width
		house_width = (x_half_length < y_half_length) ? 2*x_half_length/11 : 2*y_half_length/11;
		if (house_width < 2)
			edge_width = 2;
		
		// margins from edge
		int x_margin = BOARD_WIDTH_MARGIN;
		int y_margin = BOARD_HEIGHT_MARGIN_TOP;
			
		Tile[][] tiles = board.get_tiles();
		Edge[][] edges = board.get_edges();
		Vertex[][] vertices = board.get_vertices();
		
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
					int x_dist = x_margin + x_dist_edge;
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
				
				x_dist_edge -= 3*x_half_length/2;
			}
			
			// edges
			int mid = edges.length/2;
			
			for (int i = 0; i < edges.length; i++)
			{
				int dir = 1; // 1 means up first, 0 means down first
				if (i > mid)
				{
					dir = 0;
				}
				
				if (i % 2 == 0)
				{
					for (int j = 0; j < edges[i].length; j++)
					{
						int l_j = j/2;
						int r_j = (j+1)/2;
							
						if (dir == 1)
						{
							
							drawEdge(g, bounds[i + 1][l_j].x, bounds[i + 1][l_j].y, bounds[i][r_j].x, bounds[i][r_j].y, edges[i][j]);
							dir = 0;
						}
						else
						{
							drawEdge(g, bounds[i][l_j].x, bounds[i][l_j].y, bounds[i + 1][r_j].x, bounds[i + 1][r_j].y, edges[i][j]);
							dir = 1;
						}
					}
				}
				else
				{
					for (int j = 0; j < edges[i].length; j++)
					{
						drawEdge(g,bounds[i][j].x, bounds[i][j].y, bounds[i + 1][j].x, bounds[i + 1][j].y, edges[i][j]);
					}
				}
			}
			
			// old edge code
			/*
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
			*/
			
			// vertices
			for (int i = 0; i < vertices.length; i++)
			{
				for (int j = 0; j < vertices[i].length; j++)
				{
					boolean temp_house = (i == cursor_i && j == cursor_j);
					drawVertex(g, bounds[i][j].x, bounds[i][j].y, vertices[i][j], temp_house);
				}
			}
		}
		else
		{
			// tiles
			int y_dist_edge = y_half_length;
			
			for (int i = 0; i < tiles.length; i++)
			{
				for (int j = 0; j < tiles[i].length; j++)
				{
					// distances to center of hex
					int x_dist = x_margin + x_half_length*(r + 1 + 2*j);
					int y_dist = y_margin + y_dist_edge;
					
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
				
				y_dist_edge += 3*y_half_length/2;
			}
			
			// edges
			int mid = edges.length/2;
			
			for (int i = 0; i < edges.length; i++)
			{
				int dir = 1; // 1 means up first, 0 means down first
				if (i > mid)
				{
					dir = 0;
				}
				
				if (i % 2 == 0)
				{
					for (int j = 0; j < edges[i].length; j++)
					{
						int l_j = j/2;
						int r_j = (j+1)/2;
							
						if (dir == 1)
						{
							
							drawEdge(g, bounds[i + 1][l_j].x, bounds[i + 1][l_j].y, bounds[i][r_j].x, bounds[i][r_j].y, edges[i][j]);
							dir = 0;
						}
						else
						{
							drawEdge(g, bounds[i][l_j].x, bounds[i][l_j].y, bounds[i + 1][r_j].x, bounds[i + 1][r_j].y, edges[i][j]);
							dir = 1;
						}
					}
				}
				else
				{
					for (int j = 0; j < edges[i].length; j++)
					{
						drawEdge(g,bounds[i][j].x, bounds[i][j].y, bounds[i + 1][j].x, bounds[i + 1][j].y, edges[i][j]);
					}
				}
			}
			
			// old edge drawing code
			/*
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
			}*/
			
			// vertices
			for (int i = 0; i < vertices.length; i++)
			{
				for (int j = 0; j < vertices[i].length; j++)
				{
					boolean temp_house = i == cursor_i && j == cursor_j;
					drawVertex(g, bounds[i][j].x, bounds[i][j].y, vertices[i][j], temp_house);
				}
			}
		}
	}
	
	// test method for drawing the board using the nodes instead of array
	// just to make sure the nodes are synced properly
	public void draw_board_nodes(Graphics2D g)
	{
		Board board = catan.get_board();
		
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
		
		int len = board.get_length();
		
		int x_half_length;
		int y_half_length;
		
		if (board.get_type() == 1)
		{
			x_half_length = (int)(width/(4*len - 4));
			y_half_length = (int)(height/(3*len - 1));
		}
		else
		{
			x_half_length = (int)(width/(4*len - 2));
			y_half_length = (int)(height/(3*len - 1));
		}
		
		// set edge width
		edge_width = (x_half_length < y_half_length) ? x_half_length/7 : y_half_length/7;
		if (edge_width < 1) 
			edge_width = 1;
		
		// set house/city width
		house_width = (x_half_length < y_half_length) ? 2*x_half_length/11 : 2*y_half_length/11;
		if (house_width < 2)
			edge_width = 2;
		
		// margins from edge
		int x_margin = BOARD_WIDTH_MARGIN;
		int y_margin = BOARD_HEIGHT_MARGIN_TOP;
		
		NodeHex base = board.get_head();
		NodeHex node = base;
		
		int x_dist_edge = len * x_half_length;
		int y_dist_edge = y_half_length;
		while (base != null)
		{
			node = base;
			
			int x_half_steps = 0;
			while (node != null)
			{
				int x_dist = x_margin + x_dist_edge + x_half_steps*x_half_length;
				int y_dist = y_margin + y_dist_edge;
				drawHex(g, x_dist, y_dist, x_half_length, y_half_length, false, node.tile);
				
				int[][] bounds = new int[6][2];
				bounds[0][0] = x_dist - x_half_length;
				bounds[0][1] = y_dist - y_half_length/2;
				bounds[1][0] = x_dist;
				bounds[1][1] = y_dist - y_half_length;
				bounds[2][0] = x_dist + x_half_length;
				bounds[2][1] = y_dist - y_half_length/2;
				bounds[3][0] = x_dist + x_half_length;
				bounds[3][1] = y_dist + y_half_length/2;
				bounds[4][0] = x_dist;
				bounds[4][1] = y_dist + y_half_length;
				bounds[5][0] = x_dist - x_half_length;
				bounds[5][1] = y_dist + y_half_length/2;
				
				for (int i = 0; i < 6; i++)
				{
					drawEdge(g, bounds[i][0], bounds[i][1], bounds[(i + 1)%6][0], bounds[(i + 1)%6][1], node.edges[i].edge);
				}
				
				for (int i = 0; i < 6; i++)
				{
					drawVertex(g, bounds[i][0], bounds[i][1], node.vertices[i].vertex, false);
				}
				
				node = node.hexes[2];
				x_half_steps += 2;
			}
			
			if (base.hexes[4] == null)
			{
				base = base.hexes[3];
				x_dist_edge += x_half_length;
			}
			else
			{
				base = base.hexes[4];
				x_dist_edge -= x_half_length;
			}
			
			y_dist_edge += 3*y_half_length/2;
		}
			
		
	}
	
	// how wide the edges/roads are
	private static int edge_width = 1;
	
	// how wide houses/cities are
	private static int house_width = 1;
	
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
		// do nothing if tile is null ofcourse
		if (tile == null) return;
		
		BufferedImage im = null;
			
		int res = tile.get_resource();
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
			// dont draw anything of the hex if the tile is not decided yet
			return; 
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
		
		int number = tile.get_number();
		
		int radius = (x_half_length < y_half_length) ? x_half_length : y_half_length;
		radius  = 2*radius/5;
		
		drawToken(g, x0, y0, radius, 5, number, tile.get_robber());
		
		// draw black border
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(edge_width));
		
		if (orient)
		{
			g.drawLine(x0 - x_half_length/2, y0 - y_half_length, x0 + x_half_length/2, y0 - y_half_length);
			g.drawLine(x0 + x_half_length/2, y0 - y_half_length, x0 + x_half_length, y0);
			g.drawLine(x0 + x_half_length, y0, x0 + x_half_length/2, y0 + y_half_length);
			g.drawLine(x0 + x_half_length/2, y0 + y_half_length, x0 - x_half_length/2, y0 + y_half_length);
			g.drawLine(x0 - x_half_length/2, y0 + y_half_length, x0 - x_half_length, y0);
			g.drawLine(x0 - x_half_length, y0, x0 - x_half_length/2, y0 - y_half_length);
		}
		else
		{
			g.drawLine(x0, y0 - y_half_length, x0 + x_half_length, y0 - y_half_length/2);
			g.drawLine(x0 + x_half_length, y0 - y_half_length/2, x0 + x_half_length, y0 + y_half_length/2);
			g.drawLine(x0 + x_half_length, y0 + y_half_length/2, x0, y0 + y_half_length);
			g.drawLine(x0, y0 + y_half_length, x0 - x_half_length, y0 + y_half_length/2);
			g.drawLine(x0 - x_half_length, y0 + y_half_length/2, x0 - x_half_length, y0 - y_half_length/2);
			g.drawLine(x0 - x_half_length, y0 - y_half_length/2, x0, y0 - y_half_length);
		}
		
	}
	
	// draws the token with number on it for tiles
	// will only draw the numbers 2-12.
	// g - graphics to draw on
	// x0 - center x coord
	// y0 - center y coord
	// r - radius 
	// t0 - thickness of the border
	// number - number to draw
	// robber - whether the robber is present on this tile
	public void drawToken(Graphics2D g, int x0, int y0, int r, int t0, int number, boolean robber)
	{
		if (robber)
			g.setColor(Color.GRAY);
		else
			g.setColor(BEIGE);
		
		g.fillOval(x0 - r, y0 - r, 2*r, 2*r);
		
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(t0));
		g.drawOval(x0 - r, y0 - r, 2*r, 2*r);
		
		// if the robber is present we don't need to draw anything
		if (!robber && number != -1)
		{
			int digit_width = (int)(Math.sqrt(2)*r/2);
			
			if (number > 9)
			{
				// 10s digit
				int d1 = number / 10;
				int d0 = number % 10;
				g.drawImage(FONT[d1][0], x0 - digit_width, y0 - digit_width, digit_width, 2*digit_width, null);
				g.drawImage(FONT[d0][0], x0, y0 - digit_width, digit_width, 2*digit_width, null);
			}
			else
			{
				// red numbers
				if (number == 6 || number == 8)
				{
					g.drawImage(FONT[number][1], x0 - digit_width, y0 - digit_width, 2*digit_width, 2*digit_width, null);
				}
				else
				{
					g.drawImage(FONT[number][0], x0 - digit_width, y0 - digit_width, 2*digit_width, 2*digit_width, null);
				}
			}
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
		int player = edge.get_player();
		int type = edge.get_type();
		
		// draw the road
		if (player > -1 && type > -1)
		{
			g.setColor(player_col[player]);
			
			// make it a little thicker
			g.setStroke(new BasicStroke(edge_width + 2));
			g.drawLine(x0,y0,x1,y1);
		}
		
	}
	
	// drawing houses/cities
	// g - graphics to draw on
	// x0 - x coord of vertex
	// y0 - y coord of vertex
	// vertex - house, city, port info
	// temp_house - if this is the cursor location
	public void drawVertex(Graphics2D g, int x0, int y0, Vertex vertex, boolean temp_house)
	{	
		int player = vertex.get_player();
		int type = vertex.get_type();
		int port = vertex.get_port();
		
		if (temp_house)
		{
			int alpha_mask = 0x77FFFFFF;
			int new_color = player_col[player_selected].getRGB() & alpha_mask;
			Color col = new Color(new_color, true);
			drawHouse(g, x0, y0, (int)(house_width*1.5), col);
		}
		else if (player > -1)
		{
			if (type == 0)
			{
				drawHouse(g, x0, y0, house_width, player_col[player]);
			}
			else if (type == 1)
			{
				drawCity(g, x0, y0, house_width, player_col[player]);
			}
		}
		
	}
	
	// draw a house with color c, centered at x0, y0, width w
	public void drawHouse(Graphics2D g, int x0, int y0, int width, Color c)
	{
		g.setColor(c);
		
		g.fillRect(x0 - width, y0 - width, 2*width, 2*width);
		
		int[] tri_x = {x0 - width, x0, x0 + width};
		int[] tri_y = {y0 - width, y0 - 2*width, y0 - width};
		
		g.fillPolygon(tri_x, tri_y, 3);
		
		g.setColor(DARK_GREY);
		g.setStroke(new BasicStroke(2*width/5));
		
		g.drawLine(x0 - width, y0 - width, x0, y0 - 2*width);
		g.drawLine(x0, y0 - 2*width, x0 + width, y0 - width);
		g.drawLine(x0 + width, y0 - width, x0 + width, y0 + width);
		g.drawLine(x0 + width, y0 + width, x0 - width, y0 + width);
		g.drawLine(x0 - width, y0 + width, x0 - width, y0 - width);
	}
	
	// draw city with color c, centered x0, y0, width w
	public void drawCity(Graphics2D g, int x0, int y0, int width, Color c)
	{
		g.setColor(c);
		
		x0 += 2*width/3;
		y0 -= 2*width/5;
		
		g.fillRect(x0 - 2*width, y0 - width, 3*width, 2*width);
		g.fillRect(x0 - 2*width, y0 - 2*width, 2*width, width);
		
		int[] tri_x = {x0 - 2*width, x0 - width, x0};
		int[] tri_y = {y0 - 2*width, y0 - 3*width, y0 - 2*width};
		
		g.fillPolygon(tri_x, tri_y, 3);
		
		g.setColor(DARK_GREY);
		g.setStroke(new BasicStroke(2*width/5));
		
		g.drawLine(x0 - 2*width, y0 - 2*width, x0 - width, y0 - 3*width);
		g.drawLine(x0 - width, y0 - 3*width, x0, y0 - 2*width);
		g.drawLine(x0, y0 - 2*width, x0, y0 - width);
		g.drawLine(x0, y0 - width, x0 + width, y0 - width);
		g.drawLine(x0 + width, y0 - width, x0 + width, y0 + width);
		g.drawLine(x0 + width, y0 + width, x0 - 2*width, y0 + width);
		g.drawLine(x0 - 2*width, y0 + width, x0 - 2*width, y0 - 2*width);
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