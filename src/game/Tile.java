package src.game;

public class Tile
{
	// 0 for wood, 1 for brick, 2 for SHEEP, 3 for wheat, 4 for ore, 5 for desert
	// -1 denotes havent set yet
	private int resource;
	
	// what dice role on this tile or -1 if blank token(desert)
	private int number;
	
	// if robber on this tile
	private boolean robber = false;
	
	// array indices
	// may be useful if using the node data structures
	private int i, j;
	
	// parent NodeHex
	public NodeHex node_hex;
	
	public Tile()
	{
		this.resource = -1;
		this.number = -1;
		i = j = -1;
		
		node_hex = null;
	}
	
	public Tile(int i, int j)
	{
		this.resource = -1;
		this.number = -1;
		this.i = i;
		this.j = j;
		
		node_hex = null;
	}
	
	public Tile(int resource, int number, int i, int j)
	{
		this.resource = resource;
		this.number = number;
		this.i = i;
		this.j = j;
		
		node_hex = null;
	}
	
	public String toString()
	{
		return "{(" + i + "," + j + ")," + resource + "," + number + "," + robber + "}";
	}
	
	// individual setters
	public void set_resource(int resource)
	{
		this.resource = resource;
	}
	
	public void set_number(int number)
	{
		this.number = number;
	}
	
	public void set_robber(boolean robber)
	{
		this.robber = robber;
	}
	
	// set resource type, number, robber
	public void set(int resource, int number, boolean robber)
	{
		this.resource = resource;
		this.number = number;
		this.robber = robber;
	}
	
	public int get_resource()
	{
		return resource;
	}
	
	public int get_number()
	{
		return number;
	}
	
	public boolean get_robber()
	{
		return robber;
	}
	
	// get i and j in 
	public int[] get_index()
	{
		return new int[] {i,j};
	}
}