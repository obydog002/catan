package src.game;

public class Edge
{
	// belongs to who, -1 if nothing
	private int player;
	
	// what is on it
	// -1 for nothing, 0 for road
	private int type;
	
	// port info for neighbouring vertices to use
	// makes more sense to tie this data to edges
	// -1 nothing, 0 - wood, 1 - brick, 2 - sheep, 3 - wheat, 4 - ore, 5 - any
	private int port;
	
	// array indices
	private int i, j;
	
	// parent NodeEdge 
	public NodeEdge node_edge;
	
	public Edge()
	{
		this.player = -1;
		this.type = -1;
		this.port = -1;
		i = j = -1;
		
		node_edge = null;
	}
	
	public Edge(int i, int j)
	{
		this.player = -1;
		this.type = -1;
		this.port = -1;
		this.i = i;
		this.j = j;
		
		node_edge = null;
	}

	public void set_player(int player)
	{
		this.player = player;
	}
	
	public void set_type(int type)
	{
		this.type = type;
	}
	
	public void set_port(int port)
	{
		this.port = port;
	}
	
	public void set(int player, int type, int port)
	{
		this.player = player;
		this.type = type;
		this.port = port;
	}
	
	public String toString()
	{
		return "{(" + i + "," + j + ")," + player + "," + type + "}";
	}
	
	public int get_player()
	{
		return player;
	}
	
	public int get_type()
	{
		return type;
	}
	
	public int get_port()
	{
		return port;
	}
	
	// get i and j in 
	public int[] get_index()
	{
		return new int[] {i,j};
	}
}