package src.game;

public class NodeEdge
{
	// edge data
	public Edge edge;
	
	// vertex order based on orientation of this edge
	// always top most index 0
	//
	// 0-				      -0			0
	//   \				     /				|
	//    \				    /				|
	//     \			   /				|
	//      \			  /					|
	//       -1			1-					1
	
	// neighbouring vertices
	public NodeVertex[] vertices;
	
	public NodeEdge(Edge edge)
	{
		this.edge = edge;
		
		if (edge != null)
			edge.node_edge = this;
		
		vertices = new NodeVertex[2];
		vertices[0] = null;
		vertices[1] = null;
	}
	
	public String neighbour_info()
	{
		String result = "neighbour vertices:\n";
		
		for (int i = 0; i < 2; i++)
		{
			result += i + ": " + vertices[i].vertex + "\n";
		}
		
		return result;
	}
}