package src.game;

public class Edge
{
	// belongs to who, -1 if nothing
	private int player;
	
	// what is on it
	// -1 for nothing, 0 for road
	private int type;
	
	// array indices
	private int i, j;
	
	public Edge()
	{
		this.player = -1;
		this.type = -1;
		i = j = -1;
	}
	
	public Edge(int i, int j)
	{
		this.player = -1;
		this.type = -1;
		this.i = i;
		this.j = j;
	}

	public void set(int player, int type)
	{
		this.player = player;
		this.type = type;
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
}