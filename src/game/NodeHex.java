package src.game;

public class NodeHex
{
	// order string for printing
	static String string_order[] = {"top-left", "top-right", "right", "bot-right", "bot-left", "left"};
	
	// this hex
	public Tile tile;
	
	// array indexes order
	// vertices are on the left (clockwise) of the number
	//
	//			  v1
	// 			 /  \
	//		 0  /	 \   1
	// 		   /	  \
	//		  v0	  v2  
	//		  |	   	   |
	//	   5  |	       |  2
	//		  |        |
	// 		  v5	  v3
	//		   \	  /
	//		 4	\    /   3
	//           \  /
	// 			  v4
	//
	
	public NodeHex hexes[];
	public NodeVertex vertices[];
	public NodeEdge edges[];
	
	public NodeHex(Tile tile)
	{
		this.tile = tile;
		
		// set the parent info
		if (tile != null)
			tile.node_hex = this;
		
		hexes = new NodeHex[6];
		vertices = new NodeVertex[6];
		edges = new NodeEdge[6];
		
		for (int i = 0; i < 6; i++)
		{
			hexes[i] = null;
			vertices[i] = null;
			edges[i] = null;
		}
	}
	
	public String toString()
	{
		String result = "this: " + tile + " ";
		
		for (int i = 0; i < 6; i++)
		{
			result += string_order[i] + ": ";
			if (hexes[i] != null)
				result += hexes[i].tile + " ";
			else
				result += "null ";
		}
		
		return result;
	}
	
	// method to print neighbouring info
	public String neighbour_info()
	{
		String result = "neighbour hexes:\n";
		for (int i = 0; i < 6; i++)
		{
			if (hexes[i] != null)
				result += i + " " + hexes[i].tile + "\n";
			else
				result += i + " null\n";
		}
		
		result += "neighbour verticies:\n";
		for (int i = 0; i < 6; i++)
		{
			result += i + " " + vertices[i].vertex + "\n";
		}
		
		result += "neighbour edges:\n";
		for (int i = 0; i < 6; i++)
		{
			result += i + " " + edges[i].edge + "\n";
		}
		
		return result;
	}
}