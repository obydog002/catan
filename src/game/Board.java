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
	
	private Random rng;
	
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
		this.rng = null;
		
		hex_head = null;
		
		this.tiles = null;
		this.vertices = null;
		this.edges = null;
	}
	public Board(Random rng)
	{
		this.rng = rng;
		
		this.hex_head = null;
		
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
		
		NodeEdge temp_edges[][] = new NodeEdge[edges.length][];
		
		for (int i = 0; i < edges.length; i++)
		{
			temp_edges[i] = new NodeEdge[edges[i].length];
			for (int j = 0; j < edges[i].length; j++)
			{
				temp_edges[i][j] = new NodeEdge(edges[i][j]);
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
				
				// connect edges to hexes
				node.edges[0] = temp_edges[top_index][t_j];
				node.edges[1] = temp_edges[top_index][t_j + 1];
				node.edges[2] = temp_edges[top_index + 1][j + 1];
				node.edges[3] = temp_edges[top_index + 2][b_j + 1];
				node.edges[4] = temp_edges[top_index + 2][b_j];
				node.edges[5] = temp_edges[top_index + 1][j];
				
				// connect parent hex to edges
				node.edges[0].hexes[1] = node;
				node.edges[1].hexes[0] = node;
				node.edges[2].hexes[0] = node;
				node.edges[3].hexes[0] = node;
				node.edges[4].hexes[1] = node;
				node.edges[5].hexes[1] = node;
				
				// connect edges to vertices, connect vertices to edges
				node.edges[0].vertices[0] = node.vertices[1];
				node.edges[0].vertices[1] = node.vertices[0];
				node.vertices[0].edges[1] = node.edges[0];
				node.vertices[1].edges[2] = node.edges[0];
				
				node.edges[1].vertices[0] = node.vertices[1];
				node.edges[1].vertices[1] = node.vertices[2];
				node.vertices[1].edges[1] = node.edges[1];
				node.vertices[2].edges[0] = node.edges[1];
				
				node.edges[2].vertices[0] = node.vertices[2];
				node.edges[2].vertices[1] = node.vertices[3];
				node.vertices[2].edges[2] = node.edges[2];
				node.vertices[3].edges[0] = node.edges[2];
				
				node.edges[3].vertices[0] = node.vertices[3];
				node.edges[3].vertices[1] = node.vertices[4];
				node.vertices[3].edges[2] = node.edges[3];
				node.vertices[4].edges[1] = node.edges[3];
				
				node.edges[4].vertices[0] = node.vertices[5];
				node.edges[4].vertices[1] = node.vertices[4];
				node.vertices[4].edges[0] = node.edges[4];
				node.vertices[5].edges[1] = node.edges[4];
				
				node.edges[5].vertices[0] = node.vertices[0];
				node.edges[5].vertices[1] = node.vertices[5];
				node.vertices[5].edges[0] = node.edges[5];
				node.vertices[0].edges[2] = node.edges[5];
				
				
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
					System.out.println("vertex " + i + ": null\n");
				else
					System.out.println("vertex " + i + ": " + node.vertices[i] + "\n");
			}
			
			for (int i = 0; i < 6; i++)
			{
				if (node.edges[i] == null)
					System.out.println("edge " + i + ": null\n");
				else
					System.out.println("edge " + i + ": " + node.edges[i].edge + "\n");
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
	
	// perform a breadth-first or depth-first traversal over nodehexes
	// prints each 
	// breadth - true means BF, false is DF
	// verbose - true means prints neighbouring information as well, false just hex info
	public void traverse_hex_print(boolean breadth, boolean verbose)
	{
		int total = 0;
		boolean seen[][] = new boolean[tiles.length][];
		for (int i = 0; i < tiles.length; i++)
		{
			total += tiles[i].length;
			seen[i] = new boolean[tiles[i].length];
		}
		
		// function as a stack or a queue as needed
		// just using arrays for now, not the most
		// elegant solution but it will do for this.
		// In the future I will either use java's stack/queue
		// or create my own for AIs when traversing the board.
		NodeHex list[] = new NodeHex[total];
		
		System.out.println("hexes:");
		
		boolean empty = false;
		
		int head = -1;
		if (breadth)
			head = 0;
		
		int back = 1;
		list[0] = hex_head;
		NodeHex next = list[0];
		int index[] = next.tile.get_index();
		
		seen[index[0]][index[1]] = true;
		
		while (!empty)
		{
			System.out.println("Hex:" + next.tile);
			if (verbose)
			{
				System.out.println(next.neighbour_info());
			}
			
			for (int i = 0; i < 6; i++)
			{
				if (next.hexes[i] != null)
				{
					index = next.hexes[i].tile.get_index();
					if (!seen[index[0]][index[1]]) // not been seen yet
					{
						seen[index[0]][index[1]] = true;
						
						head++;
						list[head] = next.hexes[i];
					}
				}
			}
			
			// get next element
			if (breadth) // queue
			{
				if (back > head) // empty
					empty = true;
				else
				{
					next = list[back];
					back++;
				}
			}
			else // stack
			{
				if (head == -1) // empty
					empty = true;
				else
				{
					next = list[head];
					head--;
				}
			}	
		}
	}
	
	// traversal for node vertices
	// breadth - true bf, false df
	// verbose - neighbour info
	public void traverse_vertex_print(boolean breadth, boolean verbose)
	{
		int total = 0;
		boolean seen[][] = new boolean[vertices.length][];
		for (int i = 0; i < vertices.length; i++)
		{
			total += vertices.length;
			seen[i] = new boolean[vertices[i].length];
		}
		
		NodeVertex list[] = new NodeVertex[total];
		
		System.out.println("vertices:");
		
		boolean empty = false;
		
		int head = -1;
		if (breadth)
			head = 0;
		
		int back = 1;
		list[0] = hex_head.vertices[0];
		NodeVertex next = list[0];
		int index[] = next.vertex.get_index();
		
		seen[index[0]][index[1]] = true;
		
		while (!empty)
		{
			System.out.println("vertex: " + next.vertex);
			if (verbose)
			{
				System.out.println(next.neighbour_info());
			}
			
			for (int i = 0; i < 3; i++)
			{
				if (next.vertices[i] != null)
				{
					index = next.vertices[i].vertex.get_index();
					
					if (!seen[index[0]][index[1]])
					{
						seen[index[0]][index[1]] = true;
						
						head++;
						list[head] = next.vertices[i];
					}
				}
			}
			
			// get next element
			if (breadth) // queue
			{
				if (back > head) // empty
					empty = true;
				else
				{
					next = list[back];
					back++;
				}
			}
			else // stack
			{
				if (head == -1) // empty
					empty = true;
				else
				{
					next = list[head];
					head--;
				}
			}	
		}
	}
	
	// traversal for node edges
	// breadth - true bf, false df
	// verbose - neighbour info
	public void traverse_edge_print(boolean breadth, boolean verbose)
	{
		int total = 0;
		boolean seen[][] = new boolean[edges.length][];
		for (int i = 0; i < edges.length; i++)
		{
			total += edges.length;
			seen[i] = new boolean[edges[i].length];
		}
		
		NodeEdge list[] = new NodeEdge[total];
		
		System.out.println("edges:");
		
		boolean empty = false;
		
		int head = -1;
		if (breadth)
			head = 0;
		
		int back = 1;
		list[0] = hex_head.edges[0];
		NodeEdge next = list[0];
		int index[] = next.edge.get_index();
		
		seen[index[0]][index[1]] = true;
		
		while (!empty)
		{
			System.out.println("edge: " + next.edge);
			if (verbose)
			{
				System.out.println(next.neighbour_info());
			}
			
			for (int i = 0; i < 2; i++)
			{
				// both vertices should never be null
				NodeVertex v = next.vertices[i];
				
				for (int j = 0; j < 3; j++)
				{
					if (v.edges[j] != null)
					{
						index = v.edges[j].edge.get_index();
						if (!seen[index[0]][index[1]])
						{
							seen[index[0]][index[1]] = true;
							
							head++;
							list[head] = v.edges[j];
						}
					}
				}
			}
			
			// get next element
			if (breadth) // queue
			{
				if (back > head) // empty
					empty = true;
				else
				{
					next = list[back];
					back++;
				}
			}
			else // stack
			{
				if (head == -1) // empty
					empty = true;
				else
				{
					next = list[head];
					head--;
				}
			}	
		}
	}
	
	// iterates over vertices that have houses/cities, owned by cur_player, and aren't in v_tracker
	// then performs dfs on each neighbouring edge to determine connectivity
	// will find any edges/houses that are 'connected' given by the boolean tracker arrays
	// will then mark them in the supplied boolean arrays res as true (connected)
	public void integrity_check(int cur_player, int[][] v_tracker, boolean[][] e_tracker, boolean[][] v_res, boolean[][] e_res)
	{
		// to track visits
		boolean[][] visit = new boolean[edges.length][];
		
		for (int i = 0; i < edges.length; i++)
		{
			visit[i] = new boolean[edges[i].length];
		}

		for (int i = 0; i < vertices.length; i++)
		{
			for (int j = 0; j < vertices[i].length; j++)
			{
				Vertex v = vertices[i][j];
				
				// if its ours, if it has a house/city, and if its not a house we wish to track
				// v_tracker being 1 or 3 means a house was placed here this turn
				boolean tracked = (v_tracker[i][j] == 1 || v_tracker[i][j] == 3);
				if (v.get_type() > -1 && v.get_player() == cur_player && !tracked)
				{
					for (int k = 0; k < 3; k++)
					{
						if (v.node_vertex.edges[k] != null)
						{
							Edge e = v.node_vertex.edges[k].edge;
							if (e.get_player() == cur_player && e.get_type() > -1)
							{
								integ_dfs(e, cur_player, v_tracker, e_tracker, v_res, e_res, visit);
							}
						}
					}
				}
			}
		}
	}
	
	// the dfs helper method for integrity_check
	// performs a single dfs over the edge supplied, marking visit fields and other fields as appropirate
	public void integ_dfs(Edge edge, int player, int[][] v_tracker, boolean[][] e_tracker, boolean[][] v_res, boolean[][] e_res, boolean[][] visit)
	{
		int total_edges = 0;
		for (int i = 0; i < edges.length; i++)
		{
			total_edges += edges[i].length;
		}
		
		int head = -1;
		Edge[] stack = new Edge[total_edges];
		
		stack[0] = edge;
		head++;
		
		while (head > -1)
		{
			Edge e = stack[head];
			head--;
			
			int[] index = e.get_index();
			visit[index[0]][index[1]] = true;
			
			// found this edge
			if (e_tracker[index[0]][index[1]])
			{
				e_res[index[0]][index[1]] = true;
			}
			
			for (int i = 0; i < 2; i++)
			{
				NodeVertex v = e.node_edge.vertices[i];
				
				index = v.vertex.get_index();
				
				int val = v_tracker[index[0]][index[1]];
				boolean tracked = val == 1 || val == 3;
				if (v.vertex.get_type() > -1 && v.vertex.get_player() == player && tracked)
				{
					v_res[index[0]][index[1]] = true;
				}
				
				for (int j = 0; j < 3; j++)
				{
					if (v.edges[j] != null)
					{
						Edge n = v.edges[j].edge;
						
						index = n.get_index();
						if (!visit[index[0]][index[1]])
						{
							// has road, one of ours
							if (n.get_type() == 0 && n.get_player() == player)
							{
								head++;
								stack[head] = n;
							}
						}
					}
				}
			}
		}
	}
	
	// a method to generate tile_setup array based on variant mode
	public int[] get_tile_setup()
	{
		int number_tiles = 0;
		
		for (int i = 0; i < tiles.length; i++)
		{
			number_tiles += tiles[i].length;
		}
		
		int desert_tiles = (length > 1) ? length - 2 : 0;
		
		int x = number_tiles - desert_tiles;
		int count = 0;
		
		int[] tile_setup = new int[6];
		tile_setup[5] = desert_tiles;
		
		while (x > 0 && count < 5)
		{
			tile_setup[count]++;
			
			x--;
			count++;
		}
			
		while (x > 0)
		{
			int r = rng.nextInt(5); // populate with equal chance for each tile type
			
			tile_setup[r]++;
			
			x--;
		}
		
		return tile_setup;
	}
	
	// method to generate token_setup based on variant mode
	// desert_tiles - how many desert_tiles so we can make the token array correctly
	public int[] get_token_setup(int desert_tiles)
	{
		int number_tiles = 0;
		
		for (int i = 0; i < tiles.length; i++)
		{
			number_tiles += tiles[i].length;
		}
		
		number_tiles -= desert_tiles;
		
		int token_setup[] = new int[11];
		
		int base = number_tiles / 10;
		int mod = number_tiles % 10;
		
		int low = 4; // 4th index is 6
		int high = 6; // 6th index is 8
		for (int i = 0; i < 5; i++)
		{	
			token_setup[low] = base;
			token_setup[high] = base;
			
			if (mod > 0)
			{
				token_setup[low]++;
				mod--;
			}
			
			if (mod > 0)
			{
				token_setup[high]++;
				mod--;
			}
			
			low--;
			high++;
		}
		
		return token_setup;
	}
	
	// a method to set tiles with random hexes and tokens
	// board must be initialized first with 3-reg or 4-ext modes
	// new methods needed for other boards as this assumes symmetric board types!!
	// tile_setup - an array representing tile counts summing up # tiles
	// tile_setup[0] wood, 1 brick, 2 sheep, 3 wheat, 4 ore, 5 desert
	// token_setup - array representng tokens for tiles, sum to # tiles - #desert tiles
	// 0 #2s, 1 #3s, .... 
	// 6_8 - if we need to remove instances of too op tiles, aka 6 or 8 next to each other.
	public void set_board(int[] tile_setup, int[] token_setup, boolean rule6_8)
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
			
			tile_a[r]--;
			hexes[i] = r;
		}
	
		int[] tokens = new int[number_tiles - desert]; // don't set the desert tile
		r = 0;
		for (int i = 0; i < tokens.length; i++)
		{
			while (token_a[r] == 0)
				r++;
			
			token_a[r]--;
			tokens[i] = r + 2; 
		}
		
		// later we may check for board constraints
		boolean ok = true;
		shuffle(hexes);
		
		// check for token constraints
		do
		{
			ok = true;
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
			if (rule6_8)
			{
				ok &= check_rule6_8();
			}
			
		} while (!ok);
		
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
	
	// rule6_8 token constraint
	// returns false if the current token setting violates the 6_8 rule6_8
	// true otherwise
	// loops over tiles, if it is 6 or 8 it then checks neighbours
	public boolean check_rule6_8()
	{
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				Tile t = tiles[i][j];
				int n = t.get_number();
				if (n == 6 || n == 8) // check neighbours
				{
					for (int k = 0; k < 6; k++)
					{
						NodeHex n_h = t.node_hex.hexes[k];
						if (n_h != null)
						{
							Tile neigb = n_h.tile;
							int n2 = neigb.get_number();
							
							if (n2 == 6 || n2 == 8)
							{
									return false;
							}
						}
					}
				}
			}
		}
		
		return true;
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
	
	// sets the ports assuming its a 3-catan board
	public void set_ports_reg()
	{
		// pedantic check
		if (type != 0 || length != 3)
			return;
		
		// clear any remaining/hidden ports
		for (int i = 0; i < edges.length; i++)
		{
			for (int j = 0; j < edges[i].length; j++)
			{
				edges[i][j].set_port(-1);
			}
		}
		
		// sets edges according to normal catan 
		for (int k = 0; k < Config.REG_PORT_VALUES.length; k++)
		{
			int i = Config.REG_PORT_INDEXES_I[k];
			int j = Config.REG_PORT_INDEXES_J[k];
			int port = Config.REG_PORT_VALUES[k];
			edges[i][j].set_port(port);
		}
	}
	
	// sets ports assuming 4-ext board
	public void set_ports_ext()
	{
		// check
		if (type != 1 || length != 4)
		{
			return;
		}
		
		// clear any remaining/hidden ports
		for (int i = 0; i < edges.length; i++)
		{
			for (int j = 0; j < edges[i].length; j++)
			{
				edges[i][j].set_port(-1);
			}
		}
		
		// sets edges according to normal catan 
		for (int k = 0; k < Config.EXT_PORT_VALUES.length; k++)
		{
			int i = Config.EXT_PORT_INDEXES_I[k];
			int j = Config.EXT_PORT_INDEXES_J[k];
			int port = Config.EXT_PORT_VALUES[k];
			edges[i][j].set_port(port);
		}
	}
	
	// checks that the current board is fully set
	// all hexes set, tokens for all non-des tile
	public boolean check_full_set()
	{
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				int res = tiles[i][j].get_resource();
				int token = tiles[i][j].get_number();
				if (res > -1 && res < 6)
				{
					if (res != 5) // desert no token
					{
						if (token < 2 || token > 12)
							return false;
					}
					
				}
				else
					return false;
			}
		}
		
		return true;
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
				int port = rng.nextInt(7) - 1;
				edges[i][j].set(player, road, port);
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
				
				vertices[i][j].set(player, type);
			}
		}
	}
	
	// clears the board 
	public void clear_board()
	{
		for (int i = 0; i < tiles.length; i++)
		{
			for (int j = 0; j < tiles[i].length; j++)
			{
				// no res, no number, no robber
				tiles[i][j].set(-1,-1,false);
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

	public Random get_rng()
	{
		return rng;
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
	
	// A factory copy type method that allows us to get a board instance completely independent from 
	// an original one. For security reasons, to ensure agents can't maliciously edit
	// board variables
	public static Board clone_board(Board original)
	{
		// default structure as we DON'T want the same rng
		// otherwise agents could read the future!!!
		Board new_board = new Board(); 
		
		int type = original.get_type();
		int length = original.get_length();
		
		new_board.set_type_length(type, length);
		
		new_board.deep_copy_tiles(original.get_tiles());
		new_board.deep_copy_vertices(original.get_vertices());
		new_board.deep_copy_edges(original.get_edges());
		
		new_board.initialize_nodes_normal();
		
		return new_board;
	}
	
	// sets type and length
	public void set_type_length(int t, int l)
	{
		this.type = t;
		this.length = l;
	}
	
	// deep copies tiles to this boards tiles
	// assuming they aren't initialized yet
	public void deep_copy_tiles(Tile[][] t)
	{
		this.tiles = new Tile[t.length][];
		for (int i = 0; i < t.length; i++)
		{
			this.tiles[i] = new Tile[t[i].length];
			for (int j = 0; j < t[i].length; j++)
			{
				this.tiles[i][j] = new Tile(t[i][j]);
			}
		}
	}
	
	// deep copies tiles to this boards vertices
	public void deep_copy_vertices(Vertex[][] v)
	{
		this.vertices = new Vertex[v.length][];
		for (int i = 0; i < v.length; i++)
		{
			this.vertices[i] = new Vertex[v[i].length];
			for (int j = 0; j < v[i].length; j++)
			{
				// deep copies v[i][j]
				this.vertices[i][j] = new Vertex(v[i][j]);
			}
		}
	}
	
	// deep copy edges
	public void deep_copy_edges(Edge[][] e)
	{
		this.edges = new Edge[e.length][];
		for (int i = 0; i < e.length; i++)
		{
			this.edges[i] = new Edge[e[i].length];
			for (int j = 0; j < e[i].length; j++)
			{
				this.edges[i][j] = new Edge(e[i][j]);
			}
		}
	}
}