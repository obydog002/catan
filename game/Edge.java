package catan.game;

public class Edge
{
	// belongs to who, -1 if nothing
	private int player;
	
	// what is on it
	// -1 for nothing, 0 for road
	private int type;
	
	public Edge()
	{
		this.player = -1;
		this.type = -1;
	}

	public void set(int player, int type)
	{
		this.player = player;
		this.type = type;
	}
	
	public String toString()
	{
		return "[" + player + "," + type + "]";
	}
}