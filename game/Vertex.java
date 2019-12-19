package catan.game;

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
	
	public Vertex()
	{
		this.player = -1;
		this.type = -1;
		this.port = -1;
	}
	
	public String toString()
	{
		return "(" + player + " " + type + " " + port + ")";
	}
}
