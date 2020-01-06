package catan.game;

public class NodeHex
{
	// order string for printing
	static String string_order[] = {"top-left", "top-right", "right", "bot-right", "bot-left", "left"};
	
	// this hex
	Tile tile;
	
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
	
	NodeHex hexes[];
	NodeVertex vertices[];
	
	public NodeHex(Tile tile)
	{
		this.tile = tile;
		
		hexes = new NodeHex[6];
		vertices = new NodeVertex[6];
		
		for (int i = 0; i < 6; i++)
		{
			hexes[i] = null;
			vertices[i] = null;
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
}