package src.game;

public class Vertex
{
	// who this belongs to if there is a building
	// -1 not owned
	private int player;
	
	// what type of structure of this vertex
	// -1 for nothing, 0 for settlement, 1 for city
	private int type;
	
	// if there is a port
	// -1 for no port
	private int port;
	
	// array indices
	private int i, j;
	
	// parent NodeVertex
	public NodeVertex node_vertex;
	
	public Vertex()
	{
		this.player = -1;
		this.type = -1;
		this.port = -1;
		i = j = -1;
		
		node_vertex = null;
	}
	
	public Vertex(int i, int j)
	{
		this.player = -1;
		this.type = -1;
		this.port = -1;
		this.i = i;
		this.j = j;
		
		node_vertex = null;
	}
	
	public void set(int player, int type, int port)
	{
		this.player = player;
		this.type = type;
		this.port = port;
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
	
	public String toString()
	{
		return "{(" + i + "," + j + ")," + player + "," + type + "}";
	}
	
	// get i and j in 
	public int[] get_index()
	{
		return new int[] {i,j};
	}
}
