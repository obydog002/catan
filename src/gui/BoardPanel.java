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
	public static final int BOARD_WIDTH_MARGIN = 80;
	
	// margin for top of panel
	// 10 + 10 because 10 is getting eaten up somewhere
	public static final int BOARD_HEIGHT_MARGIN_TOP = 50;
	
	// margin for bottom
	public static final int BOARD_HEIGHT_MARGIN_BOTTOM = 200;
	
	// initial width of panel
	public static final int BOARD_WIDTH = 500;
	
	// initial height
	public static final int BOARD_HEIGHT = 500;
	
	private Random rng;
	
	private GameData game_data;
	
	// states of the game
	// 0 - board setup
	// 1 - choose player order
	// 2 - game playing
	private int state;
	
	// instance variables for drawing board setup stuff
	Tile hexes_display[];
	int tokens_display[]; // same as arrays in Config
	int ports_display[];
	
	// count for how many hexes are left - this is already implicit for tokens_display and ports_display
	int hexes_count[];
	
	// locations 
	Point hexes_points[];
	Point token_points[];
	Point port_points[];
	
	// rotate the board by 90 degrees
	private boolean rotate = false;
	
	// handles mouse clicks
	private static InputHandler input;
	
	private static Color player_col[];
	
	// refers to which hex, vertex or edge the mouse closest to 
	// for mouse selection only
	// hex coordinates
	private int hex_selected_i, hex_selected_j;

	// vertex coords
	private int vertex_selected_i, vertex_selected_j;
	
	// edge coords
	private int edge_selected_i, edge_selected_j;
	
	// mouse coordinates
	private int mouse_x, mouse_y;
	
	private int player_selected;

	// hexes half lengths
	private int x_half_length;
	private int y_half_length;
	
	// boundry for calculating how the mouse click will interact with
	// hexes, vertices, edges
	private Point vertex_bounds[][];
	
	// hex center
	private Point hex_center[][];
	
	// alpha mask
	private static final int ALP_MASK = 0x77FFFFFF;
	
	// static colors we use
	private static final Color SEA_BLUE = new Color(220,220,255);
	private static final Color LIGHT_RED = new Color(255,220,220);
	private static final Color BEIGE = new Color(255, 253, 208);
	private static final Color DARK_GREY = new Color(25,25,25);
	
	private static final Color TRANS_RED = new Color(Color.RED.getRGB() & ALP_MASK, true);
	
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
	
	// rotates the board
	public void toggle_rotate()
	{
		rotate = !rotate;
		
		repaint();
	}
	
	// initializes the data we need for showing it all
	public void init_board_setup()
	{
		hexes_display = new Tile[6];
		tokens_display = new int[11];
		ports_display = new int[6];
		
		hexes_points = new Point[6];
		token_points = new Point[11];
		port_points = new Point[6];
		
		for (int i = 0; i < 6; i++)
		{
			// -2,-2 so it doesnt get detected by the index code stuff
			hexes_display[i] = new Tile(-2,-2);
			hexes_display[i].set(i,-1,false);
			
			hexes_points[i] = new Point();
			port_points[i] = new Point();
		}
		
		for (int i = 0; i < 11; i++)
		{
			token_points[i] = new Point();
		}
	}
	
	public BoardPanel(GameData game_data, Random rng)
	{
		this.rng = rng;
		this.game_data = game_data;
		
		state = 0;

		init_board_setup();
		
		hex_selected_i = -1;
		hex_selected_j = -1;
		
		vertex_selected_i = -1;
		vertex_selected_j = -1;
		
		edge_selected_i = -1;
		edge_selected_j = -1;
		
		player_selected = 0;

		Dimension pref = new Dimension(BOARD_WIDTH + 2*BOARD_WIDTH_MARGIN, BOARD_HEIGHT + BOARD_HEIGHT_MARGIN_TOP + BOARD_HEIGHT_MARGIN_BOTTOM);
		this.setPreferredSize(pref);
		
		catan = new CatanEngine(game_data, rng);
		
		player_col = catan.get_player_colors();
		
		x_half_length = 0;
		y_half_length = 0;
		
		Board board = catan.get_board();
		
		Vertex[][] vertices = board.get_vertices();
		
		vertex_bounds = new Point[vertices.length][];
		for (int i = 0; i < vertices.length; i++)
		{
			vertex_bounds[i] = new Point[vertices[i].length];
			for (int j = 0; j < vertices[i].length; j++)
			{
				vertex_bounds[i][j] = new Point();
			}
		}
		
		Tile[][] tiles = board.get_tiles();
		
		hex_center = new Point[tiles.length][];
		for (int i = 0; i < tiles.length; i++)
		{
			hex_center[i] = new Point[tiles[i].length];
			for (int j = 0; j < tiles[i].length; j++)
			{
				hex_center[i][j] = new Point();
			}
		} 
		
		input = new InputHandler(this);
	}
	
	// a method that will generate a full board based on the setup data passed to it
	public void generate_board(BoardSetupData setup)
	{
		catan.generate_board(setup);
		
		repaint();
	}
	
	// process mouse pressed down
	public void mouse_held(int x, int y)
	{
		
	}
	
	// process mouse released
	public void mouse_released(int x, int y)
	{
		
	}
	
	// input handler will call this to update it
	public void mouse_move(int x, int y)
	{
		this.mouse_x = x;
		this.mouse_y = y;
		
		
		repaint();
	}

	// will try to find the nearest hex to the coordinates supplied on the board
	// x,y - point coordinates
	// sets the hex_selected fields to nearest
	// or sets to -1,-1 if none found
	public void find_board_hex(int x, int y)
	{
		// if either are 0 dont proceed
		if (x_half_length == 0 || y_half_length == 0)
			return;
		
		Board board = catan.get_board();
		Vertex[][] vertices = board.get_vertices();
		
		Dimension dim = this.getSize();
		
		int log_width = (int)dim.getWidth() - 2*BOARD_WIDTH_MARGIN;
		int log_height = (int)dim.getHeight() - (BOARD_HEIGHT_MARGIN_BOTTOM + BOARD_HEIGHT_MARGIN_TOP);
		
		// out of x bounds
		if (x <= BOARD_WIDTH_MARGIN || x >= log_width + BOARD_WIDTH_MARGIN)
		{
			hex_selected_i = -1;
			hex_selected_j = -1;
			return;
		}
		
		// similarily y bounds
		if (y <= BOARD_HEIGHT_MARGIN_TOP || y >= log_height + BOARD_HEIGHT_MARGIN_TOP)
		{
			hex_selected_i = -1;
			hex_selected_j = -1;
			return;
		}
		
		int vert_length = vertex_bounds.length;
		
		if (rotate)
		{
			// indexes start from the rightside of rotated board
			int cor_x = vertex_bounds[0][0].x - x;
			
			int base_i = cor_x / (3*x_half_length/2);
			base_i *= 2;
			
			if (cor_x % (3*x_half_length/2) >= x_half_length/2)
			{
				base_i++;
			}
			
			if (base_i >= vert_length - 1)
			{
				hex_selected_i = -1;
				hex_selected_j = -1;
				return;
			}
			
			int cor_y = y;
			
			if (base_i % 2 == 0) // hex triangles
			{
				int first_i = base_i + 1;
				int second_i = base_i;
				boolean right = true; // triangles start by pointing right
				
				if (base_i >= vert_length/2) // left-side of board
				{
					first_i = base_i;
					second_i = base_i + 1;
					right = false;
				}
				
				int y_top = vertex_bounds[first_i][0].y;
				int y_length = vertex_bounds[first_i].length;
				int y_bot = vertex_bounds[first_i][y_length - 1].y;
				
				if (cor_y <= y_top || cor_y >= y_bot)
				{
					hex_selected_i = -1;
					hex_selected_j = -1;
					return;
				}
				
				cor_y -= y_top;
				
				int base_j = cor_y / (2*y_half_length);
				
				// main triangle coordinates
				int top_x = vertex_bounds[first_i][base_j].x;
				int top_y = vertex_bounds[first_i][base_j].y;
				int mid_x = vertex_bounds[second_i][base_j].x;
				int mid_y = vertex_bounds[second_i][base_j].y;
				int bot_x = vertex_bounds[first_i][base_j + 1].x;
				int bot_y = vertex_bounds[first_i][base_j + 1].y;
				
				boolean found = in_tri_bary(x, y, top_x, top_y, mid_x, mid_y, bot_x, bot_y);
				
				if (found)
				{
					if (right)
					{
						Vertex v = vertices[second_i][base_j];
						Tile t = v.node_vertex.hexes[2].tile;
						int index[] = t.get_index();
						hex_selected_i = index[0];
						hex_selected_j = index[1];
						return;
					}
					else
					{
						Vertex v = vertices[second_i][base_j];
						Tile t = v.node_vertex.hexes[1].tile;
						int index[] = t.get_index();
						hex_selected_i = index[0];
						hex_selected_j = index[1];
						return;
					}
				}
				
				// first row or last we don't need to check auxilary triangles
				if (base_i > 0 && base_i < vert_length - 2)
				{
					// first column no need to check top triangle
					if (base_j > 0)
					{
						if (right)
						{
							int top_right_x = mid_x;
							int top_right_y = top_y;
							
							found = in_tri_bary(x, y, top_x, top_y, top_right_x, top_right_y, mid_x, mid_y);
							if (found)
							{
								Vertex v = vertices[first_i][base_j];
								Tile t = v.node_vertex.hexes[1].tile;
								int index[] = t.get_index();
								hex_selected_i = index[0];
								hex_selected_j = index[1];
								return;
							}
						}
						else
						{
							int top_left_x = mid_x;
							int top_left_y = top_y;
							
							found = in_tri_bary(x, y, top_left_x, top_left_y, top_x, top_y, mid_x, mid_y);
							if (found)
							{
								Vertex v = vertices[first_i][base_j];
								Tile t = v.node_vertex.hexes[2].tile;
								int index[] = t.get_index();
								hex_selected_i = index[0];
								hex_selected_j = index[1];
								return;
							}
						}
					}
					
					// last column dont check bot triangle
					if (base_j < y_length - 2)
					{
						if (right)
						{
							int bot_right_x = mid_x;
							int bot_right_y = bot_y;
							
							found = in_tri_bary(x, y, bot_x, bot_y, mid_x, mid_y, bot_right_x, bot_right_y);
							if (found)
							{
								Vertex v = vertices[first_i][base_j + 1];
								Tile t = v.node_vertex.hexes[1].tile;
								int index[] = t.get_index();
								hex_selected_i = index[0];
								hex_selected_j = index[1];
								return;
							}
						}
						else
						{
							int bot_left_x = mid_x;
							int bot_left_y = bot_y;
							
							found = in_tri_bary(x, y, mid_x, mid_y, bot_x, bot_y, bot_left_x, bot_left_y);
							if (found)
							{
								Vertex v = vertices[first_i][base_j + 1];
								Tile t = v.node_vertex.hexes[2].tile;
								int index[] = t.get_index();
								hex_selected_i = index[0];
								hex_selected_j = index[1];
								return;
							}
						}
					}
				}
			}
			else // rectangle parts
			{
				int y_top = vertex_bounds[base_i][0].y;
				int y_length = vertex_bounds[base_i].length;
				int y_bot = vertex_bounds[base_i][y_length - 1].y;
				
				if (cor_y <= y_top || cor_y >= y_bot)
				{
					hex_selected_i = -1;
					hex_selected_j = -1;
					return;
				}
				
				cor_y -= y_top;
				
				int base_j = cor_y / (2*y_half_length);
				
				Vertex v = vertices[base_i][base_j];
				Tile t = v.node_vertex.hexes[2].tile;
				int index[] = t.get_index();
				hex_selected_i = index[0];
				hex_selected_j = index[1];
				return;
			}
		}
		else
		{
			int cor_y = y - BOARD_HEIGHT_MARGIN_TOP;
			
			int height_part = 3*y_half_length/2;
			
			int base_i = cor_y / height_part;
			base_i *= 2;
			
			// the index is within the rectangle part of the hex
			if (cor_y % height_part >= y_half_length/2)
			{
				base_i++;
			}
		
			// stop from trying to collision check non existent rectangle at bottom
			if (base_i >= vert_length - 1)
			{
				hex_selected_i = -1;
				hex_selected_j = -1;
				return;
			}
			
			int cor_x = x;
			
			// even means its the hex tops/bottoms
			if (base_i % 2 == 0)
			{
				if (base_i < vert_length/2) // top of board means triangles start by pointing upwards
				{
					int x_left = vertex_bounds[base_i + 1][0].x;
					int horz_length = vertex_bounds[base_i + 1].length;
					int x_right = vertex_bounds[base_i + 1][horz_length - 1].x;
					
					// outside the boundries so no point checking
					if (cor_x <= x_left || cor_x >= x_right)
					{
						hex_selected_i = -1;
						hex_selected_j = -1;
						return;
					}
					
					cor_x -= x_left;
					
					int base_j = cor_x / (2*x_half_length);
					int left_x = vertex_bounds[base_i + 1][base_j].x;
					int left_y = vertex_bounds[base_i + 1][base_j].y;
					int mid_x = vertex_bounds[base_i][base_j].x;
					int mid_y = vertex_bounds[base_i][base_j].y;
					int right_x = vertex_bounds[base_i + 1][base_j + 1].x;
					int right_y = vertex_bounds[base_i + 1][base_j + 1].y;
					
					boolean found = in_tri_bary(x, y, left_x, left_y, mid_x, mid_y, right_x, right_y);
					
					if (found) // set the hex selected
					{
						Vertex v = vertices[base_i][base_j];
						Tile t = v.node_vertex.hexes[2].tile;
						int[] index = t.get_index();
						
						hex_selected_i = index[0];
						hex_selected_j = index[1];
						return;
					}
					
					// if its the first row we shouldnt check outside tiangles as they aren't apart
					// of the board
					if (base_i > 0)
					{
						// if its in the first hex or last hex we don't need to check an outside triangle
						if (base_j > 0)
						{
							int top_left_x = vertex_bounds[base_i + 1][base_j].x;
							int top_left_y = vertex_bounds[base_i + 1][base_j].y - y_half_length/2;
							
							found = in_tri_bary(x, y, top_left_x, top_left_y, mid_x, mid_y, left_x, left_y);
							
							if (found)
							{
								Vertex v = vertices[base_i][base_j];
								Tile t = v.node_vertex.hexes[0].tile;
								int[] index = t.get_index();
								
								hex_selected_i = index[0];
								hex_selected_j = index[1];
								return;
							}
						}
						
						if (base_j < horz_length - 2)
						{
							int top_right_x = vertex_bounds[base_i + 1][base_j + 1].x;
							int top_right_y = vertex_bounds[base_i + 1][base_j + 1].y - y_half_length/2;
							
							found = in_tri_bary(x, y, mid_x, mid_y, top_right_x, top_right_y, right_x, right_y);
							
							if (found)
							{
								Vertex v = vertices[base_i][base_j];
								Tile t = v.node_vertex.hexes[1].tile;
								int[] index = t.get_index();
								
								hex_selected_i = index[0];
								hex_selected_j = index[1];
								return;
							}
						}
					}
				}
				else // otherwise they start by pointing donwards
				{
					int x_left = vertex_bounds[base_i][0].x;
					int horz_length = vertex_bounds[base_i].length;
					int x_right = vertex_bounds[base_i][horz_length - 1].x;
					
					// outside the boundries so no point checking
					if (cor_x <= x_left || cor_x >= x_right)
					{
						hex_selected_i = -1;
						hex_selected_j = -1;
						return;
					}
					
					cor_x -= x_left;
					
					int base_j = cor_x / (2*x_half_length);
					double left_x = vertex_bounds[base_i][base_j].x;
					double left_y = vertex_bounds[base_i][base_j].y;
					double mid_x = vertex_bounds[base_i + 1][base_j].x;
					double mid_y = vertex_bounds[base_i + 1][base_j].y;
					double right_x = vertex_bounds[base_i][base_j + 1].x;
					double right_y = vertex_bounds[base_i][base_j + 1].y;
					
					boolean found = in_tri_bary(x, y, left_x, left_y, mid_x, mid_y, right_x, right_y);
					
					if (found) // set the hex selected
					{
						Vertex v = vertices[base_i + 1][base_j];
						Tile t = v.node_vertex.hexes[1].tile;
						int[] index = t.get_index();
						
						hex_selected_i = index[0];
						hex_selected_j = index[1];
						return;
					}
					
					// last-most row we shouldnt check outside tiangles as they aren't apart
					// of the board
					if (base_i < vert_length - 2)
					{
						// if its in the first hex or last hex we don't need to check an outside triangle
						if (base_j > 0)
						{
							int bot_left_x = vertex_bounds[base_i][base_j].x;
							int bot_left_y = vertex_bounds[base_i][base_j].y + y_half_length/2;
							
							found = in_tri_bary(x, y, bot_left_x, bot_left_y, left_x, left_y, mid_x, mid_y);
							
							if (found) // set the hex selected
							{
								Vertex v = vertices[base_i + 1][base_j];
								Tile t = v.node_vertex.hexes[0].tile;
								int[] index = t.get_index();
								
								hex_selected_i = index[0];
								hex_selected_j = index[1];			
								return;
							}
						}
						
						if (base_j < horz_length - 2)
						{
							int bot_right_x = vertex_bounds[base_i][base_j + 1].x;
							int bot_right_y = vertex_bounds[base_i][base_j + 1].y + y_half_length/2;
							
							found = in_tri_bary(x, y, mid_x, mid_y, right_x, right_y, bot_right_x, bot_right_y);
							
							if (found) // set the hex selected
							{
								Vertex v = vertices[base_i + 1][base_j];
								Tile t = v.node_vertex.hexes[2].tile;
								int[] index = t.get_index();
								
								hex_selected_i = index[0];
								hex_selected_j = index[1];
								return;
							}
						}
					}
				}
			} 
			else // rectangle part of hex
			{
				int x_left = vertex_bounds[base_i][0].x;
				int horz_length = vertex_bounds[base_i].length;
				int x_right = vertex_bounds[base_i][horz_length - 1].x;
				
				// bounds checking again
				if (cor_x <= x_left || cor_x >= x_right)
				{
					hex_selected_i = -1;
					hex_selected_j = -1;
					return;
				}
				
				cor_x -= x_left;
				
				int base_j = cor_x / (2*x_half_length);
				
				// belong to rect base_i, base_j of the vertex bounds
				Vertex v = vertices[base_i][base_j];
				Tile t = v.node_vertex.hexes[2].tile;
				int[] index = t.get_index();
				
				hex_selected_i = index[0];
				hex_selected_j = index[1];		
				return;	
			}
		}
		
		hex_selected_i = -1;
		hex_selected_j = -1;
		return;
	}
	
	// tries to find nearest vertex using circle collision
	// x,y - point coordinates
	// sets selected fields to nearest index
	// or sets to -1,-1 if none
	public void find_board_vertex(int x, int y)
	{
		if (x_half_length == 0 || y_half_length == 0)
		{
			return;
		}
		
		int vert_length = vertex_bounds.length;
		
		int r0;
		int r1;
		int r;
		
		if (rotate)
		{
			r0 = x_half_length/2;
			
			r1 = (int)Math.sqrt(x_half_length*x_half_length/16 + y_half_length*y_half_length/4);
			
			r = (r0 > r1) ? r0 : r1;
			
			// x bounds checking
			if (x <= vertex_bounds[vert_length - 1][0].x - x_half_length/2 || x >= vertex_bounds[0][0].x + x_half_length/2)
			{
				vertex_selected_i = -1;
				vertex_selected_j = -1;
				return;
			}
			
			int cor_x = (vertex_bounds[0][0].x + x_half_length/2) - x;
			
			int base_i = cor_x / (3*x_half_length/2);
			base_i *= 2;
			
			if (base_i >= vert_length)
			{
				vertex_selected_i = -1;
				vertex_selected_j = -1;
				return;
			}
			
			int first_i = base_i + 1;
			int second_i = base_i;
			
			// left-side of board
			if (base_i >= vert_length/2)
			{
				first_i = base_i;
				second_i = base_i + 1;
			}
			
			int top_y = vertex_bounds[first_i][0].y - y_half_length/2;
			int y_length = vertex_bounds[first_i].length;
			int bot_y = vertex_bounds[first_i][y_length - 1].y + y_half_length/2;
			
			if (y <= top_y || y >= bot_y)
			{
				vertex_selected_i = -1;
				vertex_selected_j = -1;
				return;
			}
			
			int cor_y = y - top_y;
			
			int base_j = cor_y / (2*y_half_length);
			
			boolean found = in_circle(x, y, vertex_bounds[first_i][base_j].x, vertex_bounds[first_i][base_j].y, r);
			if (found)
			{
				vertex_selected_i = first_i;
				vertex_selected_j = base_j;
				return;
			}
				
			// if its the last vertex we don't check the other one since it doesn't exist!
			if (base_j < y_length - 1)
			{
				found = in_circle(x, y, vertex_bounds[second_i][base_j].x, vertex_bounds[second_i][base_j].y, r);
				if (found)
				{
					vertex_selected_i = second_i;
					vertex_selected_j = base_j;
					return;
				}
			}
		}
		else
		{
			// radiuses
			r0 = y_half_length/2; // half of vertical edge length
		
			r1 = (int)Math.sqrt(x_half_length*x_half_length/4 + y_half_length*y_half_length/16); // half of angled edge
		
			r = (r0 > r1) ? r0 : r1; // max of radius
		
			// bounds checking for y
			if (y <= vertex_bounds[0][0].y - y_half_length/2 || y >= vertex_bounds[vert_length - 1][0].y + y_half_length/2)
			{
				vertex_selected_i = -1;
				vertex_selected_j = -1;
				return;
			}
			
			int cor_y = y - (vertex_bounds[0][0].y - y_half_length/2);
			
			int base_i = cor_y / (3*y_half_length/2);
			base_i *= 2;
			
			if (base_i >= vert_length)
			{
				vertex_selected_i = -1;
				vertex_selected_j = -1;
				return;
			}
			
		
			int first_i = base_i + 1;
			int second_i = base_i;
			
			// bottom half of board
			if (base_i >= vert_length/2)
			{
				first_i = base_i;
				second_i = base_i + 1;
			}

			int x_left = vertex_bounds[first_i][0].x - x_half_length/2;
			int horz_length = vertex_bounds[first_i].length;
			int x_right = vertex_bounds[first_i][horz_length - 1].x + x_half_length/2;
				
			// x bounds checking
			if (x <= x_left || x >= x_right)
			{
				vertex_selected_i = -1;
				vertex_selected_j = -1;
				return;
			}
				
			int cor_x = x - x_left;
				
			int base_j = cor_x / (2*x_half_length);
				
			boolean found = in_circle(x, y, vertex_bounds[first_i][base_j].x, vertex_bounds[first_i][base_j].y, r);
			if (found)
			{
				vertex_selected_i = first_i;
				vertex_selected_j = base_j;
				return;
			}
				
			// if its the last vertex we don't check the other one since it doesn't exist!
			if (base_j < horz_length - 1)
			{
				found = in_circle(x, y, vertex_bounds[second_i][base_j].x, vertex_bounds[second_i][base_j].y, r);
				if (found)
				{
					vertex_selected_i = second_i;
					vertex_selected_j = base_j;
					return;
				}
			}
		}
		
		vertex_selected_i = -1;
		vertex_selected_j = -1;
		return;
	}
	
	// tries to find nearest edge using rect collision
	// x,y - point coordinates
	// sets selected fields to nearest index
	// or sets to -1,-1 if none
	public void find_board_edge(int x, int y)
	{
		if (x_half_length == 0 || y_half_length == 0)
		{
			return;
		}
		
		int vert_length = vertex_bounds.length;
		
		Board board = catan.get_board();
		Vertex vertices[][] = board.get_vertices();
		
		if (rotate)
		{
			if (x <= vertex_bounds[vert_length - 1][0].x || x >= vertex_bounds[0][0].x)
			{
				edge_selected_i = -1;
				edge_selected_j = -1;
				return;
			}
			
			int cor_x = vertex_bounds[0][0].x - x;
			
			int base_i = cor_x / (3*x_half_length/2);
			base_i *= 2;
			
			// horizontal edges
			if (cor_x % (3*x_half_length/2) >= x_half_length/2)
			{
				base_i++;
			}
			
			if (base_i >= vert_length - 1)
			{
				edge_selected_i = -1;
				edge_selected_j = -1;
				return;
			}
			
			// angled edges
			if (base_i % 2 == 0)
			{
				int first_i = base_i + 1;
				int second_i = base_i;
				boolean right = true; // edges start by 'pointing' right
				
				if (base_i >= vert_length/2) // left half of board
				{
					first_i = base_i;
					second_i = base_i + 1;
					right = false;
				}
				
				int y_top = vertex_bounds[first_i][0].y;
				int y_length = vertex_bounds[first_i].length;
				int y_bot = vertex_bounds[first_i][y_length - 1].y;
				
				if (y <= y_top || y >= y_bot)
				{
					edge_selected_i = -1;
					edge_selected_j = -1;
					return;
				}
				
				int cor_y = y - y_top;
				
				int base_j = cor_y / (2*y_half_length);
				
				Vertex v;
				Edge e;
				
				// second rectangle
				if (cor_y % (2*y_half_length) >= y_half_length)
				{
					if (right)
					{
						v = vertices[base_i][base_j];
						e = v.node_vertex.edges[1].edge;
					}
					else
					{
						v = vertices[base_i + 1][base_j];
						e = v.node_vertex.edges[1].edge;
					}
				}
				else // first rectangle
				{
					if (right)
					{
						v = vertices[base_i + 1][base_j];
						e = v.node_vertex.edges[1].edge;
					}
					else
					{
						v = vertices[base_i][base_j];
						e = v.node_vertex.edges[1].edge;
					}
				}
				
				int index[] = e.get_index();
				edge_selected_i = index[0];
				edge_selected_j = index[1];
				return;
			}
			else
			{
				int y_top = vertex_bounds[base_i][0].y - y_half_length;
				int y_length = vertex_bounds[base_i].length;
				int y_bot = vertex_bounds[base_i][y_length - 1].y + y_half_length;
				
				// chicken
				if (y <= y_top || y >= y_bot)
				{
					edge_selected_i = -1;
					edge_selected_j = -1;
					return;
				}
				
				int cor_y = y - y_top;
				
				int base_j = cor_y / (2*y_half_length);
				
				// czechoslovakia
				if (base_j >= y_length)
				{
					edge_selected_i = -1;
					edge_selected_j = -1;
					return;
				}
				
				Vertex v = vertices[base_i][base_j];
				Edge e = v.node_vertex.edges[2].edge;
				int[] index = e.get_index();
				edge_selected_i = index[0];
				edge_selected_j = index[1];
				return;
			}
		}
		else
		{
			// standard bounds checkin
			if (y <= vertex_bounds[0][0].y || y >= vertex_bounds[vert_length - 1][0].y)
			{
				edge_selected_i = -1;
				edge_selected_j = -1;
				return;
			}
			
			int cor_y = y - vertex_bounds[0][0].y;
			
			int base_i = cor_y / (3*y_half_length/2);
			base_i *= 2;
			
			// vertical edges section
			if (cor_y % (3*y_half_length/2) >= y_half_length/2)
			{
				base_i++;
			}
			
			if (base_i >= vert_length - 1)
			{
				edge_selected_i = -1;
				edge_selected_j = -1;
				return;
			}
			
			// angled edges
			if (base_i % 2 == 0)
			{
				int first_i = base_i + 1;
				int second_i = base_i;
				boolean up = true; // edges start by 'pointing' up
				
				if (base_i >= vert_length/2) // bottom half of board
				{
					first_i = base_i;
					second_i = base_i + 1;
					up = false;
				}
				
				int x_left = vertex_bounds[first_i][0].x;
				int horz_length = vertex_bounds[first_i].length;
				int x_right = vertex_bounds[first_i][horz_length - 1].x;
				
				if (x <= x_left || x >= x_right)
				{
					edge_selected_i = -1;
					edge_selected_j = -1;
					return;
				}
				
				int cor_x = x - x_left;
				
				int base_j = cor_x / (2*x_half_length);
				
				Vertex v;
				Edge e;
				
				// second rectangle
				if (cor_x % (2*x_half_length) >= x_half_length)
				{
					if (up)
					{
						v = vertices[base_i][base_j];
						e = v.node_vertex.edges[1].edge;
					}
					else
					{
						v = vertices[base_i + 1][base_j];
						e = v.node_vertex.edges[1].edge;
					}
				}
				else // first rectangle
				{
					if (up)
					{
						v = vertices[base_i + 1][base_j];
						e = v.node_vertex.edges[1].edge;
					}
					else
					{
						v = vertices[base_i][base_j];
						e = v.node_vertex.edges[1].edge;
					}
				}
				
				int index[] = e.get_index();
				edge_selected_i = index[0];
				edge_selected_j = index[1];
				return;
			}
			else // vertical edges
			{
				int x_left = vertex_bounds[base_i][0].x - x_half_length;
				int horz_length = vertex_bounds[base_i].length;
				int x_right = vertex_bounds[base_i][horz_length - 1].x + x_half_length;
				
				if (x <= x_left || x >= x_right)
				{
					edge_selected_i = -1;
					edge_selected_j = -1;
					return;
				}
				
				int cor_x = x - x_left;
				
				int base_j = cor_x / (2*x_half_length);
				
				// can never be too careful with bounds checking
				if (base_j >= horz_length)
				{
					edge_selected_i = -1;
					edge_selected_j = -1;
					return;
				}
				
				// set selected to the edge in question
				Vertex v = vertices[base_i][base_j];
				Edge e = v.node_vertex.edges[2].edge;
				int[] index = e.get_index();
				edge_selected_i = index[0];
				edge_selected_j = index[1];
				
				return;
			}
		}
	}
	
	// tests if given point is in circle
	// x,y - point coordinates
	// x0,y0 - circle center
	// r - radius of circle
	public boolean in_circle(double x, double y, double x0, double y0, double r)
	{
		return (x0 - x)*(x0 - x) + (y0 - y)*(y0 - y) < r*r;
	}
	
	// tests if given point is within the hex 
	// x,y - point coordinates
	// mid_x,mid_y - middle of hex
	// half_width - distance from mid to left side
	// half_height - distance from mid to top side
	// rotate - if the hex is rotated
	public boolean in_hex(double x, double y, double mid_x, double mid_y, double half_width, double half_height, boolean rotate)
	{
		double point_x[] = new double[6];
		double point_y[] = new double[6];
		
		if (rotate)
		{
			point_x[0] = mid_x - half_width/2;
			point_x[1] = mid_x + half_width/2;
			point_x[2] = mid_x + half_width;
			point_x[3] = mid_x + half_width/2;
			point_x[4] = mid_x - half_width/2;
			point_x[5] = mid_x - half_width;
			
			point_y[0] = mid_y - half_height;
			point_y[1] = mid_y - half_height;
			point_y[2] = mid_y;
			point_y[3] = mid_y + half_height;
			point_y[4] = mid_y + half_height;
			point_y[5] = mid_y;
		}
		else
		{
			point_x[0] = mid_x - half_width;
			point_x[1] = mid_x;
			point_x[2] = mid_x + half_width;
			point_x[3] = mid_x + half_width;
			point_x[4] = mid_x;
			point_x[5] = mid_x - half_width;
			
			point_y[0] = mid_y - half_height/2;
			point_y[1] = mid_y - half_height;
			point_y[2] = mid_y - half_height/2;
			point_y[3] = mid_y + half_height/2;
			point_y[4] = mid_y + half_height;
			point_y[5] = mid_y + half_height/2;
		}
		
		for (int i = 0; i < 6; i++)
		{
			if (in_tri_bary(x, y, point_x[i], point_y[i], point_x[(i + 1)%6], point_y[(i + 1)%6], mid_x, mid_y))
			{
				return true;
			}
		}
		
		return false;
	}
	
	// tests if the given point is within the triangle based on barycentric method
	// x,y - point coordinates
	// x0,y0 - first vertex of triangle
	// x1,y1 - second vertex of triangle
	// x2,y2 - third vertex of triangle
	public boolean in_tri_bary(double x, double y, double x0, double y0, double x1, double y1, double x2, double y2)
	{
		double denom = (y1 - y2)*(x0 - x2) + (x2 - x1)*(y0 - y2);
		double a = ((y1 - y2)*(x - x2) + (x2 - x1)*(y - y2)) / denom;
		double b = ((y2 - y0)*(x - x2) + (x0 - x2)*(y - y2)) / denom;
		double c = 1 - a - b;
		
		return a >= 0 && a <= 1 && b >= 0 && b <= 1 && c >= 0 && c <= 1;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D)g;
		
		draw_board(g2D);
		
		if (state == 0)
		{
			draw_board_setup(g2D);
		}
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
			house_width = 2;
		
		// set token radius
		token_radius = (x_half_length < y_half_length) ? x_half_length : y_half_length;
		token_radius  = 2*token_radius/5;
		if (token_radius < 1)
			token_radius = 1;
		
		// margins from edge
		int x_margin = BOARD_WIDTH_MARGIN;
		int y_margin = BOARD_HEIGHT_MARGIN_TOP;
			
		Tile[][] tiles = board.get_tiles();
		Edge[][] edges = board.get_edges();
		Vertex[][] vertices = board.get_vertices();
		
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
					
					hex_center[i][j].move(x_dist, y_dist);
					// bounds 
					int top_y_index = 2*i;
					
					if (reverse)
						vertex_bounds[top_y_index + 1][j].move(x_dist + x_half_length, y_dist);
					else
						vertex_bounds[top_y_index][j].move(x_dist + x_half_length, y_dist);
					
					vertex_bounds[top_y_index + 1][j].move(x_dist + x_half_length/2, y_dist - y_half_length);
					vertex_bounds[top_y_index + 1][j + 1].move(x_dist + x_half_length/2, y_dist + y_half_length);
					vertex_bounds[top_y_index + 2][j].move(x_dist - x_half_length/2, y_dist - y_half_length);
					vertex_bounds[top_y_index + 2][j + 1].move(x_dist - x_half_length/2, y_dist + y_half_length);
					
					if (reverse)
						vertex_bounds[top_y_index + 3][j].move(x_dist - x_half_length, y_dist);
					else
						vertex_bounds[top_y_index + 3][j + 1].move(x_dist - x_half_length, y_dist);
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
							
							drawEdge(g, vertex_bounds[i + 1][l_j].x, vertex_bounds[i + 1][l_j].y, vertex_bounds[i][r_j].x, vertex_bounds[i][r_j].y, edges[i][j]);
							dir = 0;
						}
						else
						{
							drawEdge(g, vertex_bounds[i][l_j].x, vertex_bounds[i][l_j].y, vertex_bounds[i + 1][r_j].x, vertex_bounds[i + 1][r_j].y, edges[i][j]);
							dir = 1;
						}
					}
				}
				else
				{
					for (int j = 0; j < edges[i].length; j++)
					{
						drawEdge(g,vertex_bounds[i][j].x, vertex_bounds[i][j].y, vertex_bounds[i + 1][j].x, vertex_bounds[i + 1][j].y, edges[i][j]);
					}
				}
			}
			
			// vertices
			for (int i = 0; i < vertices.length; i++)
			{
				for (int j = 0; j < vertices[i].length; j++)
				{
					drawVertex(g, vertex_bounds[i][j].x, vertex_bounds[i][j].y, vertices[i][j]);
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
						vertex_bounds[top_y_index + 1][j].move(x_dist, y_dist - y_half_length);
					else
						vertex_bounds[top_y_index][j].move(x_dist, y_dist - y_half_length);
					
					vertex_bounds[top_y_index + 1][j].move(x_dist - x_half_length, y_dist - y_half_length/2);
					vertex_bounds[top_y_index + 1][j + 1].move(x_dist + x_half_length, y_dist - y_half_length/2);
					vertex_bounds[top_y_index + 2][j].move(x_dist - x_half_length, y_dist + y_half_length/2);
					vertex_bounds[top_y_index + 2][j + 1].move(x_dist + x_half_length, y_dist + y_half_length/2);
					
					if (reverse)
						vertex_bounds[top_y_index + 3][j].move(x_dist, y_dist + y_half_length);
					else
						vertex_bounds[top_y_index + 3][j + 1].move(x_dist, y_dist + y_half_length);
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
							
							drawEdge(g, vertex_bounds[i + 1][l_j].x, vertex_bounds[i + 1][l_j].y, vertex_bounds[i][r_j].x, vertex_bounds[i][r_j].y, edges[i][j]);
							dir = 0;
						}
						else
						{
							drawEdge(g, vertex_bounds[i][l_j].x, vertex_bounds[i][l_j].y, vertex_bounds[i + 1][r_j].x, vertex_bounds[i + 1][r_j].y, edges[i][j]);
							dir = 1;
						}
					}
				}
				else
				{
					for (int j = 0; j < edges[i].length; j++)
					{
						drawEdge(g,vertex_bounds[i][j].x, vertex_bounds[i][j].y, vertex_bounds[i + 1][j].x, vertex_bounds[i + 1][j].y, edges[i][j]);
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
					drawVertex(g, vertex_bounds[i][j].x, vertex_bounds[i][j].y, vertices[i][j]);
				}
			}
		}
	}
	
	// draws the support stuff needed to make the board
	// such as tiles, tokens, ports
	public void draw_board_setup(Graphics2D g)
	{
		Dimension current_dim = this.getSize();
		
		int width = (int)current_dim.getWidth();
		int height = (int)current_dim.getHeight();
		
		// if dimensions are too small just return
		if (width <= 2*BOARD_WIDTH_MARGIN || height <= BOARD_HEIGHT_MARGIN_BOTTOM + BOARD_HEIGHT_MARGIN_TOP)
		{
			return;
		}
		
		// set hex points
		// and draw the hexes
		int x = 2 + x_half_length;
		int y = height - 2 - y_half_length;
		for (int i = 0; i < 6; i++)
		{
			hexes_points[i].move(x,y);
			drawHex(g, x, y, x_half_length, y_half_length, rotate, hexes_display[i]);
			
			x += 2*x_half_length + 2;
		}
		
		// set token points
		// draw them as well
		x = 2 + x_half_length;
		y = height - 4 - 2*y_half_length - 2*token_radius;
		int low = 0; // index of 2
		int high = 10; // index of 12
		for (int i = 0; i < 5; i++)
		{
			token_points[high].move(x,y);
			drawToken(g, x, y, token_radius, 5, high + 2, false);
			
			int y_low = y - 10 - 2*token_radius;
			token_points[low].move(x,y_low);
			drawToken(g, x, y_low, token_radius, 5, low + 2, false);
			
			x += 10 + 2*token_radius;
			
			low++;
			high--;
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
					drawVertex(g, bounds[i][0], bounds[i][1], node.vertices[i].vertex);
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
	private int edge_width = 1;
	
	// how wide houses/cities are
	private int house_width = 1;
	
	// radius of token
	private int token_radius = 1;
	
	// draw a singular tile
	// g - graphics object to draw on
	// x0 - x coordinate of middle of the hex
	// y0 - y coordinate of middle
	// x_half_length - dist from center of hex to farthest point x-axis direction
	// y_half_length - dist from center to farthest point y-axis direction
	// rotate - false means vertex points north, true means edge perp is north
	// tile - tile to draw. null value draws a black empty outline
	public void drawHex(Graphics2D g, int x0, int y0, int x_half_length, int y_half_length, boolean rotate, Tile tile)
	{
		// do nothing if tile is null ofcourse
		if (tile == null)
		{
			return;
		}
		
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
		
		// tri0 is first triangle, tri1 second
		int[] tri0_x = new int[3];
		int[] tri0_y = new int[3];
		int[] tri1_x = new int[3];
		int[] tri1_y = new int[3];
		
		if (rotate)
		{
			tri0_x[0] = x0 - x_half_length/2; tri0_x[1] = x0 - x_half_length; tri0_x[2] = x0 - x_half_length/2;
			tri0_y[0] = y0 + y_half_length; tri0_y[1] = y0; tri0_y[2] = y0 - y_half_length;
			
			g.fillPolygon(tri0_x, tri0_y, 3);
			
			g.fillRect(x0 - x_half_length/2, y0 - y_half_length, x_half_length, 2*y_half_length);
			
			if (im != null)
				g.drawImage(im, x0 - x_half_length/2, y0 - y_half_length, x_half_length, 2*y_half_length, null);
			
			tri1_x[0] = x0 + x_half_length/2; tri1_x[1] = x0 + x_half_length; tri1_x[2] = x0 + x_half_length/2;
			tri1_y = tri0_y;
			
			g.fillPolygon(tri1_x, tri1_y, 3);
		}
		else
		{
			tri0_x[0] = x0 - x_half_length; tri0_x[1] = x0; tri0_x[2] = x0 + x_half_length;
			tri0_y[0] = y0 - y_half_length/2; tri0_y[1] = y0 - y_half_length; tri0_y[2] = y0 - y_half_length/2;
			
			g.fillPolygon(tri0_x, tri0_y, 3);
			
			g.fillRect(x0 - x_half_length, y0 - y_half_length/2, 2*x_half_length, y_half_length);
			
			if (im != null)
				g.drawImage(im, x0 - x_half_length, y0 - y_half_length/2, 2*x_half_length, y_half_length, null);
			
			tri1_x = tri0_x;
			tri1_y[0] = y0 + y_half_length/2; tri1_y[1] = y0 + y_half_length; tri1_y[2] = y0 + y_half_length/2;
	
			g.fillPolygon(tri1_x, tri1_y, 3);
		}
		
		int number = tile.get_number();
		
		// if the number has been set properly we draw a token
		// or if it has the robber we denote this
		if (number > 1 && number < 13 || tile.get_robber())
		{
			drawToken(g, x0, y0, token_radius, 5, number, tile.get_robber());
		}
		
		// if this is the selected tile draw transparent red over it
		int index[] = tile.get_index();
		if (hex_selected_i == index[0] && hex_selected_j == index[1])
		{
			g.setColor(TRANS_RED);
			
			g.fillPolygon(tri0_x, tri0_y, 3);
			
			if (rotate)
			{
				g.fillRect(x0 - x_half_length/2, y0 - y_half_length, x_half_length, 2*y_half_length);
			}
			else
			{
				g.fillRect(x0 - x_half_length, y0 - y_half_length/2, 2*x_half_length, y_half_length);
			}
	
			g.fillPolygon(tri1_x, tri1_y, 3);
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
		
		// draw the road if present
		if (player > -1 && type > -1)
		{
			g.setColor(player_col[player]);
			
			// make it a little thicker
			g.setStroke(new BasicStroke(edge_width + 2));
		}
		else // otherwise just draw border
		{
			g.setColor(Color.BLACK);
			
			g.setStroke(new BasicStroke(edge_width));
		}
		
		g.drawLine(x0,y0,x1,y1);
		
		int index[] = edge.get_index();
		if (edge_selected_i == index[0] && edge_selected_j == index[1])
		{
			g.setColor(TRANS_RED);
			g.setStroke(new BasicStroke(edge_width + 2));
			
			g.drawLine(x0,y0,x1,y1);
		}
	}
	
	// drawing houses/cities
	// g - graphics to draw on
	// x0 - x coord of vertex
	// y0 - y coord of vertex
	// vertex - house, city, port info
	public void drawVertex(Graphics2D g, int x0, int y0, Vertex vertex)
	{	
		int player = vertex.get_player();
		int type = vertex.get_type();
		int port = vertex.get_port();
		
		if (player > -1)
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
		else // draw little black circle to mask road endings
		{
			g.setColor(Color.BLACK);
			g.fillOval(x0 - house_width, y0 - house_width, 2*house_width, 2*house_width);
		}
		
		int index[] = vertex.get_index();
		
		// if this is currently selected draw transparent red over it
		if (vertex_selected_i == index[0] && vertex_selected_j == index[1])
		{
			g.setColor(TRANS_RED);
			
			g.fillOval(x0 - house_width, y0 - house_width, 2*house_width, 2*house_width);
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