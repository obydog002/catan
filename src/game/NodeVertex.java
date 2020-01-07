package src.game;

public class NodeVertex
{
	// vertex data at this point
	public Vertex vertex;
	
	// edges which correspond to edges from this vertex to the other 3 vertex nodes
	// must be same edge as linked with the actual vertex!
	public Edge edges[];
	
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
	
	public NodeVertex(Vertex vertex)
	{
		this.vertex = vertex;
		
		edges = new Edge[3];
		
		hexes = new NodeHex[3];
		vertices = new NodeVertex[3];
		
		for (int i = 0; i < 3; i++)
		{
			hexes[i] = null;
			edges[i] = null;
			vertices[i] = null;
		}
	}
	
	public String toString()
	{
		String result = vertex + "\n";
		
		for (int i = 0; i < 3; i++)
		{
			if (hexes[i] != null)
				result += hexes[i].tile + "\n";
		}
		
		result += "edges:\n";
		
		for (int i = 0; i < 3; i++)
		{
			result += edges[i] + "\n";
		}
		
		return result;
	}
}