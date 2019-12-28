package catan.game;

public class Tile
{
	// 0 for wood, 1 for brick, 2 for SHEEP, 3 for wheat, 4 for ore, 5 for desert
	// -1 denotes havent set yet
	private int resource;
	
	// what dice role on this tile or -1 if blank token(desert)
	private int number;
	
	// if robber on this tile
	private boolean robber = false;
	
	public Tile()
	{
		this.resource = -1;
		this.number = 0;
		
	}
	
	public Tile(int resource)
	{
		this.resource = resource;
		this.number = 0;
	}
	
	public Tile(int resource, int number)
	{
		this.resource = resource;
		this.number = number;
	}
	
	public String toString()
	{
		return resource + " " + number;
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
	
	public void set_robber(boolean robber)
	{
		this.robber = robber;
	}
}