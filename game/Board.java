package catan.game;

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
	private int edges[][];
	
	// dimensions of the overall big hexagon
	private int dim[];
	
	public Board(Tile tiles[][], Vertex vertices[][], int edges[][], int dim[])
	{
		this.tiles = tiles;
		this.vertices = vertices;
		this.edges = edges;
		this.dim = dim;
	}
	
	public Board()
	{
		this.tiles = null;
		this.vertices = null;
		this.edges = null;
		this.dim = null;
	}
	
	// set to a reguler hexagon with length of each side
	public void set_reg_hex(int length)
	{
		int x = length*2 - 1;
		
		tiles = new Tile[x][];
		
		int r = 0;
		for (int i = length; i <= x; i++)
		{
			tiles[r] = new Tile[i];
			
			for (int j = 0; j < i; j++)
			{
				tiles[r][j] = new Tile(i, j);
			}
			
			r++;
		}
		
		for (int i = x - 1; i >= length; i--)
		{
			tiles[r] = new Tile[i];
			
			for (int j = 0; j < i; j++)
			{
				tiles[r][j] = new Tile(i, j);
			}
			
			r++;
		}
		
		vertices = new Vertex[4*length][];
		
		vertices[2*length - 1] = new Vertex[2*length];
		vertices[2*length - 2] = new Vertex[2*length - 1];
		
		vertices[2*length] = new Vertex[2*length];
		vertices[2*length + 1] = new Vertex[2*length - 1];
		
		r = 2*length - 2;
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
		
		edges = new int[4*length - 1][];
		
		edges[2*length - 1] = new int[2*length]; // middle row of edges
		
		edges[2*length - 2] = new int[4*length - 2]; // row above
		edges[2*length] = new int[4*length - 2]; // row below
		
		r = 2*length - 2;
		for (int i = 1; i < length; i++)
		{
			edges[2*length - 2*i - 1] = new int[r + 1];
			edges[2*length - 2*i - 2] = new int[2*r];
			
			edges[2*length + 2*i - 1] = new int[r + 1];
			edges[2*length + 2*i] = new int[2*r];
			
			r--;
		}
		
		dim = new int[6];
		for (int i = 0; i < 6; i++)
		{
			dim[i] = length;
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
}