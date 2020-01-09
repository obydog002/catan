package src.game;

public class NodeVertex
{
	// vertex data at this point
	public Vertex vertex;
	
	// hexes, edges, vertices order
	// hexes are on left of number
	// 
	//  v0      v1				    v0
	//   \  1  /				    |
	//    0   1					    0
	//     \ /					0   |	1
	//      v				   		v
	//  0   |	2				   / \
	//		2					  2   1
  	// 		|					 /	2  \
	//      v2 					v2	   v1
	//
	

	public NodeHex hexes[];
	public NodeVertex vertices[];
	public NodeEdge edges[];
	
	public NodeVertex(Vertex vertex)
	{
		this.vertex = vertex;
		
		edges = new NodeEdge[3];
		hexes = new NodeHex[3];
		vertices = new NodeVertex[3];
		
		for (int i = 0; i < 3; i++)
		{
			hexes[i] = null;
			edges[i] = null;
			vertices[i] = null;
		}
	}
	
	public String neighbour_info()
	{
		String result = "neigbour hexes:\n";
		
		for (int i = 0; i < 3; i++)
		{
			if (hexes[i] != null)
				result += i + " " + hexes[i].tile + "\n";
			else
				result += i + " null\n";
		}
		
		result += "neighbour vertices:\n";
		
		for (int i = 0; i < 3; i++)
		{
			if (vertices[i] != null)
				result += i + " " + vertices[i].vertex + "\n";
			else
				result += i + " null\n";
		}
		
		result += "neighbour edges:\n";
		
		for (int i = 0; i < 3; i++)
		{
			if (edges[i] != null)
				result += edges[i].edge + "\n";
			else
				result += "null\n";
		}
		
		return result;
	}
}