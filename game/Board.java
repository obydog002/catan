package catan.game;

import java.util.Random;

public class Board
{
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
	private int board_type = -1;
	
	// determines the dimensions of the board
	private int board_length = -1;
	
	static Random rng = new Random();
	
	public Board(Tile tiles[][], Vertex vertices[][], Edge edges[][], int board_type, int board_length)
	{
		this.tiles = tiles;
		this.vertices = vertices;
		this.edges = edges;
		this.board_type = board_type;
		this.board_length = board_length;
	}
	
	public Board()
	{
		this.tiles = null;
		this.vertices = null;
		this.edges = null;
	}
	
	// set to a reguler hexagon with length of each side
	public void set_reg_hex(int length)
	{
		board_type = 0;
		board_length = length;
		
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
				tiles[i][j] = new Tile(rng.nextInt(6),i+2*j);
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
				vertices[i][j] = new Vertex();
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
				edges[i][j] = new Edge();
			}
		}
	}

	// set the board to be a hexagon with 
	// length, length + 1, length + 1, length, length + 1, length + 1 dimensions
	public void set_ext_hex(int length)
	{
		board_type = 1;
		board_length = length;
		
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
				tiles[i][j] = new Tile(rng.nextInt(6),i+2*j);
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
				vertices[i][j] = new Vertex();
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
				edges[i][j] = new Edge();
			}
		}
	}
	
	public void print_test()
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
	// must be called after a board is initialized with the set_board methods
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
				int road = rng.nextInt(3) == 0 ? 0 : -1;
				
				edges[i][j].set(player, road);
			}
		}
		
		for (int i = 0; i < vertices.length; i++)
		{
			for (int j = 0; j < vertices[i].length; j++)
			{
				int player = rng.nextInt(4);
				int type = rng.nextInt(3) - 1;
				int port = rng.nextInt(10) == 0 ? 0 : -1;
				
				vertices[i][j].set(player, type, port);
			}
		}
	}
	
	public Tile[][] getTiles()
	{
		return tiles;
	}
	
	public Vertex[][] getVertices()
	{
		return vertices;
	}
	
	public Edge[][] getEdges()
	{
		return edges;
	}
	
	public int getType()
	{
		return board_type;
	}
	
	public int getLength()
	{
		return board_length;
	}
}