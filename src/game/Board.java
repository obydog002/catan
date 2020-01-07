package src.game;

import java.util.Random;

public class Board
{	
	// head of nodeHex data structure, 
	// Will be pointed to tiles[0][0]
	// the references of the NodeHex nodes will determine what is geometrically and logically next
	// to it
	// all nodes will point to the same objects made 
	// by the arrays, so that changes are reflected in both.
	private NodeHex hex_head;
	
	// individual Tile hexes
	// first arg corresponds to row
	// second to collumn
	private Tile tiles[][];
	
	// rows of vertices
	// vertices[0] refers to the first row of the tip top of the hexes, angled so the hexagon vertex
	// is pointing directly north
	private Vertex vertices[][];
	
	// rows of edges
	// first edge is the one left of the first vertex of every row in vertices
	private Edge edges[][];
	
	// what the board type is
	// currently only reguler hexagon and extension board supported
	// 0 - reguler, 1 - extension
	private int type = -1;
	
	// determines the dimensions of the board
	private int length = -1;
	
	static Random rng = new Random();
	
	public Board(Tile tiles[][], Vertex vertices[][], Edge edges[][], int type, int length)
	{
		this.tiles = tiles;
		this.vertices = vertices;
		this.edges = edges;
		this.type = type;
		this.length = length;
	}
	
	public Board()
	{
		hex_head = null;
		
		this.tiles = null;
		this.vertices = null;
		this.edges = null;
	}
	
	// set to a reguler hexagon with length of each side
	public void set_reg_hex(int length)
	{
		this.type = 0;
		this.length = length;
		
		int x = length*2 - 1;
		
		tiles = new Tile[x][];
		
		tiles[length - 1] = new Tile[x];
		
		for (int i = 1; i < length; i++)
		{
			tiles[length - 1 - i] = new Tile[x - i];
			tiles[length - 1 + i] = new Tile[x - i];
		}
		
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				tiles[i][j] = new Tile(i,j);
			}
		}
		
		vertices = new Vertex[4*length][];
		
		vertices[2*length - 1] = new Vertex[2*length];
		vertices[2*length - 2] = new Vertex[2*length - 1];
		
		vertices[2*length] = new Vertex[2*length];
		vertices[2*length + 1] = new Vertex[2*length - 1];
		
		int r = 2*length - 2;
		for (int i = 1; i < length; i++)
		{
			vertices[2*length - 2*i - 1] = new Vertex[r + 1];
			vertices[2*length - 2*i - 2] = new Vertex[r];
			
			vertices[2*length + 2*i] = new Vertex[r + 1];
			vertices[2*length + 2*i + 1] = new Vertex[r];
			
			r--;
		}
		
		for (int i = 0; i < vertices.length; i++)
		{
			for (int j = 0; j < vertices[i].length; j++)
			{
				vertices[i][j] = new Vertex(i,j);
			}
		}
		
		edges = new Edge[4*length - 1][];
		
		edges[2*length - 1] = new Edge[2*length]; // middle row of edges
		
		edges[2*length - 2] = new Edge[4*length - 2]; // row above
		edges[2*length] = new Edge[4*length - 2]; // row below
		
		r = 2*length - 2;
		for (int i = 1; i < length; i++)
		{
			edges[2*length - 2*i - 1] = new Edge[r + 1];
			edges[2*length - 2*i - 2] = new Edge[2*r];
			
			edges[2*length + 2*i - 1] = new Edge[r + 1];
			edges[2*length + 2*i] = new Edge[2*r];
			
			r--;
		}
		
		for (int i = 0; i < edges.length; i++)
		{
			for (int j = 0; j < edges[i].length; j++)
			{
				edges[i][j] = new Edge(i,j);
			}
		}
	}

	// set the board to be a hexagon with 
	// length - 1, length, length, length - 1, length, length dimensions
	public void set_ext_hex(int length)
	{
		this.type = 1;
		this.length = length;
		
		int adjust = length - 1;
		
		tiles = new Tile[2*adjust + 1][];
		
		tiles[adjust] = new Tile[2*adjust];
		for (int i = 1; i <= adjust; i++)
		{
			tiles[adjust - i] = new Tile[2*adjust - i];
			tiles[adjust + i] = new Tile[2*adjust - i];
		}
		
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				tiles[i][j] = new Tile(i,j);
			}
		}
		
		vertices = new Vertex[4*(adjust + 1)][];
		
		int x = 2*(adjust + 1);
		for (int i = 0; i <= adjust; i++)
		{
			vertices[x - 2 - 2*i] = new Vertex[x - 2 - i];
			vertices[x - 1 - 2*i] = new Vertex[x - 1 - i];
			vertices[x + 2*i] = new Vertex[x - 1 - i];
			vertices[x + 1 + 2*i] = new Vertex[x - 2 - i];
		}
		
		for (int i = 0; i < vertices.length; i++)
		{
			for (int j = 0; j < vertices[i].length; j++)
			{
				vertices[i][j] = new Vertex(i,j);
			}
		}
		
		edges = new Edge[4*(adjust + 1) - 1][];
		
		edges[x - 2] = new Edge[4*adjust];
		edges[x - 1] = new Edge[2*adjust + 1];
		edges[x] = new Edge[4*adjust];
		for (int i = 1; i <= adjust; i++)
		{
			edges[x - 2 - 2*i] = new Edge[4*adjust - 2*i];
			edges[x - 1 - 2*i] = new Edge[2*adjust + 1 - i];
			edges[x - 1 + 2*i] = new Edge[2*adjust + 1 - i];
			edges[x + 2*i] = new Edge[4*adjust - 2*i];
		}
		
		for (int i = 0; i < edges.length; i++)
		{
			for (int j = 0; j < edges[i].length; j++)
			{
				edges[i][j] = new Edge(i,j);
			}
		}
	}
	
	// sets the node data structure to reflect the array
	// only call after ext,reg methods are called
	public void initialize_nodes_normal()
	{
		int vert = tiles.length;
		int half_v = vert/2;
		
		hex_head = new NodeHex(tiles[0][0]);
		
		NodeHex base = hex_head;
		NodeHex node;
		NodeHex temp;
		
		// Initializing nodehexes
		for (int i = 0; i < vert; i++)
		{
			node = base;
			
			for (int j = 0; j < tiles[i].length - 1; j++)
			{
				node.hexes[2] = new NodeHex(tiles[i][j + 1]);
				temp = node;
				node = node.hexes[2];
				node.hexes[5] = temp; 
			}
			
			// if its the last row we dont initialize the next row of hexes
			if (i < vert - 1)
			{
				int index = 3; // go right 
				if (i < half_v) // otherwise goes left
					index = 4;
				
				base.hexes[index] = new NodeHex(tiles[i + 1][0]);
				temp = base;
				base = base.hexes[index];
				
				// set the backwards link as well
				if (index == 4)
					base.hexes[1] = temp;
				else
					base.hexes[0] = temp;
			}
		}
		
		// assigning nodehex hexes array correctly
		base = hex_head;
		NodeHex prev_left = null;
		NodeHex prev_right = null;
		
		NodeHex next_left = null;
		NodeHex next_right = null;
		
		while (base != null)
		{
			node = base;
			
			if (base.hexes[4] == null)
			{
				next_left = null;
				next_right = base.hexes[3];
			}
			else
			{
				next_left = base.hexes[4];
				next_right = next_left.hexes[2];
			}
			
			if (base.hexes[0] == null)
			{
				prev_left = null;
				prev_right = base.hexes[1];
			}
			else
			{
				prev_left = base.hexes[0];
				prev_right = prev_left.hexes[2];
			}
			
			while (node != null)
			{
				node.hexes[0] = prev_left;
				node.hexes[1] = prev_right;
				
				node.hexes[4] = next_left;
				node.hexes[3] = next_right;
				
				
				node = node.hexes[2];
				
				prev_left = prev_right;
				if (prev_right != null)
					prev_right = prev_right.hexes[2];
				
				next_left = next_right;
				if (next_right != null)
					next_right = next_right.hexes[2];
			}
			
			
			if (base.hexes[4] == null)
				base = base.hexes[3];
			else
				base = base.hexes[4];
		}
		
		// temporary array so we can build the node vertexes
		NodeVertex temp_vertices[][] = new NodeVertex[vertices.length][];
		
		for (int i = 0; i < vertices.length; i++)
		{
			temp_vertices[i] = new NodeVertex[vertices[i].length];
			for (int j = 0; j < vertices[i].length; j++)
			{
				temp_vertices[i][j] = new NodeVertex(vertices[i][j]);
			}
		}
		
		int i = 0, j = 0;
		base = hex_head;
		while (base != null)
		{
			j = 0;
			node = base;
			
			while (node != null)
			{
				// vertices
				int top_index = 2*i;
				
				int l_j = j;
				int r_j = j + 1;
				int t_j = j;
				int b_j = j + 1;
				
				if (i > half_v)
				{
					t_j = j + 1;
					b_j = j;
				}
				else if (i == half_v)
				{
					b_j = j;
				}
				
				// set hex vertices
				node.vertices[0] = temp_vertices[top_index + 1][l_j];
				node.vertices[1] = temp_vertices[top_index][t_j];
				node.vertices[2] = temp_vertices[top_index + 1][r_j];
				node.vertices[3] = temp_vertices[top_index + 2][r_j];
				node.vertices[4] = temp_vertices[top_index + 3][b_j];
				node.vertices[5] = temp_vertices[top_index + 2][l_j];
				
				// connect hex vertices with parent hex
				node.vertices[0].hexes[2] = node;
				node.vertices[1].hexes[2] = node;
				node.vertices[2].hexes[0] = node;
				node.vertices[3].hexes[0] = node;
				node.vertices[4].hexes[1] = node;
				node.vertices[5].hexes[1] = node;
				
				// connect vertices to each other
				node.vertices[0].vertices[1] = node.vertices[1];
				node.vertices[1].vertices[2] = node.vertices[0];
				
				node.vertices[1].vertices[1] = node.vertices[2];
				node.vertices[2].vertices[0] = node.vertices[1];
				
				node.vertices[2].vertices[2] = node.vertices[3];
				node.vertices[3].vertices[0] = node.vertices[2];
				
				node.vertices[3].vertices[2] = node.vertices[4];
				node.vertices[4].vertices[1] = node.vertices[3];
				
				node.vertices[4].vertices[0] = node.vertices[5];
				node.vertices[5].vertices[1] = node.vertices[4];
				
				node.vertices[5].vertices[0] = node.vertices[0];
				node.vertices[0].vertices[2] = node.vertices[5];
				
				// edges
				t_j = 2*j;
				b_j = 2*j + 1;
				
				if (i > half_v)
				{
					t_j = 2*j + 1;
					b_j = 2*j;
				}
				else if (i == half_v)
				{
					b_j = 2*j;
				}
				
				node.vertices[0].edges[1] = edges[top_index][t_j];
				node.vertices[1].edges[2] = edges[top_index][t_j];
				
				node.vertices[1].edges[1] = edges[top_index][t_j + 1];
				node.vertices[2].edges[0] = edges[top_index][t_j + 1];
				
				node.vertices[2].edges[2] = edges[top_index + 1][j + 1];
				node.vertices[3].edges[0] = edges[top_index + 1][j + 1];
				
				node.vertices[3].edges[2] = edges[top_index + 2][b_j + 1];
				node.vertices[4].edges[1] = edges[top_index + 2][b_j + 1];
				
				node.vertices[4].edges[0] = edges[top_index + 2][b_j];
				node.vertices[5].edges[1] = edges[top_index + 2][b_j];
				
				node.vertices[5].edges[0] = edges[top_index + 1][j];
				node.vertices[0].edges[2] = edges[top_index + 1][j];
				
				node = node.hexes[2];
				j++;
			}
			
			if (base.hexes[4] == null)
				base = base.hexes[3];
			else
				base = base.hexes[4];
			
			i++;
		}	
	}
	
	// debug method for printing nodes
	// to see their values
	public void test_print_nodes()
	{
		NodeHex node = hex_head;
		int dir = 0; // 0 right, then right-bot or left-bot
		// 1 left, then left-bot or right bot
		
		while (node != null)
		{
			System.out.println("hex: " + node + "\n");
			
			for (int i = 0; i < 6; i++)
			{
				if (node.vertices[i] == null)
				{
					System.out.println(i + " null\n");
				}
				else
					System.out.println("vertex " + i + ": " + node.vertices[i] + "\n");
			}
			
			if (dir == 0)
			{
				if (node.hexes[2] != null)
					node = node.hexes[2];
				else if (node.hexes[3] != null)
				{
					node = node.hexes[3];
					dir = 1;
				}
				else if (node.hexes[4] != null)
				{
					node = node.hexes[4];
					dir = 1;
				}
				else
				{
					node = null;
				}
			}
			else 
			{
				if (node.hexes[5] != null)
					node = node.hexes[5];
				else if (node.hexes[4] != null)
				{
					node = node.hexes[4];
					dir = 0;
				}
				else if (node.hexes[3] != null)
				{
					node = node.hexes[3];
					dir = 0;
				}
				else
				{
					node = null;
				}
			}
		}
	}
	
	// a method to set tiles with random hexes and tokens
	// board must be initialized first with 3-reg or 4-ext modes
	// new methods needed for other boards as this assumes symmetric board types!!
	// tile_setup - an array representing tile counts summing up # tiles
	// tile_setup[0] wood, 1 brick, 2 sheep, 3 wheat, 4 ore, 5 desert
	// token_setup - array representng tokens for tiles, sum to # tiles - #desert tiles
	// 0 #2s, 1 #3s, .... 
	// correct - if we need to remove instances of too op tiles, aka 6 or 8 next to each other.
	public void set_board(int[] tile_setup, int[] token_setup, boolean correct)
	{
		int[] tile_a = new int[tile_setup.length];
		int[] token_a = new int[token_setup.length];
		
		for (int i = 0; i < tile_a.length; i++)
		{
			tile_a[i] = tile_setup[i];
		}
		
		for (int i = 0; i < token_a.length; i++)
		{
			token_a[i] = token_setup[i];
		}
		
		int number_tiles = 0;
		int desert = tile_setup[5];
		
		for (int i = 0; i < tiles.length; i++)
		{
			number_tiles += tiles[i].length;
		}
		
		int[] hexes = new int[number_tiles];
		int r = 0;
		for (int i = 0; i < hexes.length; i++)
		{
			while (tile_a[r] == 0)
				r++;
			
			hexes[i] = r;
			tile_a[r]--;
		}
	
		int[] tokens = new int[number_tiles - desert]; // don't set the desert tile
		r = 0;
		for (int i = 0; i < tokens.length; i++)
		{
			while (token_a[r] == 0)
				r++;
			
			token_a[r]--;
			// normalize the tokens.
			if (r < 5)
			{
				tokens[i] = r + 2; 
			}
			else
			{
				tokens[i] = r + 3;
			}
		}
		
		shuffle(hexes);
		
		// if correct is true this will loop until it finds a configuration that doesn't violate the 6_8 rule
		do
		{
			shuffle(tokens);
			
			r = 0;
			int ad = 0; // to adjust for the desert tiles
			for (int i = 0; i < tiles.length; i++)
			{
				for (int k = 0; k < tiles[i].length; k++)
				{
					// desert tile
					if (hexes[r] == 5)
					{
						tiles[i][k].set(hexes[r], -1, false);
						ad++;
					}
					else
					{
						tiles[i][k].set(hexes[r], tokens[r - ad], false);
					}
						
					r++;
				}
			}
			
			// if we need to check the op tiles
			if (correct)
			{
				boolean ok = true;
				for (int i = 0; i < tiles.length; i++)
				{
					for (int j = 0; j < tiles[i].length; j++)
					{
						// if the number isn't 6 or 8 no point
						if (tiles[i][j].get_number() == 6 || tiles[i][j].get_number() == 8)
						{
							int l_j = j - 1; // left j
							int r_j = j + 1; // right j
							int t_i = i - 1; // top i
							int b_i = i + 1; // bot i
							
							// indexes of the hexes 
							// order- left-top most hex, clockwise 
							
							// top 0 is to the right
							// bot 0 is on the left
							// handled for tiles.length/2 < 2
							int[] i_index = {t_i, t_i, i, b_i, b_i, i};
							int[] j_index = {l_j, j, r_j, r_j, j, l_j};
							
							if (i == tiles.length/2) 
							{	
								// middle row
								// both 0s are to the right
								j_index[0] = l_j; j_index[1] = j; j_index[2] = r_j; j_index[3] = j; j_index[4] = l_j; j_index[5] = l_j;
							}
							else if (i > tiles.length/2)
							{
								// bottom row
								// top 0 is on the left
								// bot 0 is on the right
								j_index[0] = j; j_index[1] = r_j; j_index[2] = r_j; j_index[3] = j; j_index[4] = l_j; j_index[5] = l_j;
							}
							
							for (int k = 0; k < 6; k++)
							{
								if (i_index[k] >= 0 && i_index[k] < tiles.length && j_index[k] >= 0 && j_index[k] < tiles[i_index[k]].length)
									ok &= !(tiles[i_index[k]][j_index[k]].get_number() == 6 || tiles[i_index[k]][j_index[k]].get_number() == 8);
							}
						}
					}
				}
				
				if (ok)
				{
					// no need to correct anymore
					correct = false;
				}
			}
		} while (correct);
		
		// place the robber at first available desert tile
		// or if none does nothing
		boolean set = false;
		for (int i = 0; i < tiles.length && !set; i++)
		{
			for (int j = 0; j < tiles[i].length && !set; j++)
			{
				if (tiles[i][j].get_resource() == 5) // desert
				{
					tiles[i][j].set_robber(true);
					set = true;
				}
			}
		}
	}
	
	// shuffles array in random order. 
	// I believe this is fischer-yates 
	public void shuffle(int[] a)
	{
		for (int j = 0; j < a.length - 1; j++)
		{
			int r = rng.nextInt(a.length - j) + j;
			int temp = a[j];
			a[j] = a[r];
			a[r] = temp;
		}
	}
	
	public void test_print()
	{
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				System.out.print(tiles[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < vertices.length; i++)
		{
			for (int j = 0; j < vertices[i].length; j++)
			{
				System.out.print(vertices[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < edges.length; i++)
		{
			for (int j = 0; j < edges[i].length; j++)
			{
				System.out.print(edges[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	// test method for seeing all types of tiles types, numbers, robber etc, houses to debug
	// shouldn't be used to create a board 
	// must be called after a board is initialized with the set_ext_hex or set reg methods
	public void test_randomize_all()
	{
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				int res = rng.nextInt(6);
				int numb = rng.nextInt(11) + 2;
				boolean robber = rng.nextInt(10) == 0;
				
				tiles[i][j].set(res, numb, robber);
			}
		}
		
		for (int i = 0; i < edges.length; i++)
		{
			for (int j = 0; j < edges[i].length; j++)
			{
				int player = rng.nextInt(4);
				int road = 1;
				
				edges[i][j].set(player, road);
			}
		}
		
		for (int i = 0; i < vertices.length; i++)
		{
			for (int j = 0; j < vertices[i].length; j++)
			{
				int player = rng.nextInt(8) - 4;
				int type = -1;
				if (player > -1)
					type = rng.nextInt(2);
				else
					player = -1;
				
				int port = rng.nextInt(10) == 0 ? 0 : -1;
				
				vertices[i][j].set(player, type, port);
			}
		}
	}
	
	public Tile[][] get_tiles()
	{
		return tiles;
	}
	
	public Vertex[][] get_vertices()
	{
		return vertices;
	}
	
	public Edge[][] get_edges()
	{
		return edges;
	}
	
	public int get_type()
	{
		return type;
	}
	
	public int get_length()
	{
		return length;
	}

	// returns head, which should point to the left-top most hex 
	// of the board
	public NodeHex get_head()
	{
		return hex_head;
	}
	
	public int has_settlement(int i, int j)
	{
		return -1;// return player number;
	}
}