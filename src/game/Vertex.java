package src.game;

public class Vertex
{
	// who this belongs to if there is a building
	// -1 not owned
	private int player;
	
	// what type of structure of this vertex
	// -1 for nothing, 0 for settlement, 1 for city
	private int type;
	
	// array indices
	private int i, j;
	
	// parent NodeVertex
	public NodeVertex node_vertex;
	
	public Vertex()
	{
		this.player = -1;
		this.type = -1;
		i = j = -1;
		
		node_vertex = null;
	}
	
	public Vertex(Vertex v)
	{
		this.player = v.get_player();
		this.type = v.get_type();
		
		int index[] = v.get_index();
		this.i = index[0];
		this.j = index[1];
		
		this.node_vertex = null;
	}
	
	public Vertex(int i, int j)
	{
		this.player = -1;
		this.type = -1;
		this.i = i;
		this.j = j;
		
		node_vertex = null;
	}
	
	public void set(int player, int type)
	{
		this.player = player;
		this.type = type;
	}

	// checks eligability of current placement
	// new_type - 0 house, 1 city
	// free_house - true means houses can be placed anywhere without road connection
	public boolean eligable_placement(int current_player, int new_type, boolean free_house)
	{
		if (type > -1 && player != current_player) // something here that isn't ours
			return false;
		
		// cant put a city on nothing
		// or a city on someone's else house
		if (new_type == 1 && (type != 0 || (type == 0 && player != current_player)))
			return false;
		
		// cant put a house on another house/city
		if (new_type == 0 && type != -1)
			return false;
		
		// a few additional checks to do for houses	
		if (new_type == 0)
		{
			// check we connected by a road if there is no free house
			// for house only since we can assume by this point there is a house underneath a city
			if (!free_house)
			{
				boolean road_found = false;
				for (int i = 0; i < 3; i++)
				{
					if (node_vertex.edges[i] != null)
					{
						Edge e = node_vertex.edges[i].edge;
						
						if (e.get_type() == 0 && e.get_player() == current_player)
						{
							road_found = true; 
							break;
						}
					}
				}
				
				if (!road_found)
					return false;
			}
			
			// now check neighbouring vertices dont have houses on them
			for (int i = 0; i < 3; i++)
			{
				if (node_vertex.vertices[i] != null)
				{
					Vertex v = node_vertex.vertices[i].vertex;
					
					// building present
					if (v.get_type() > -1)
						return false;
				}
			}
		}
		
		return true;
	}
	
	public int get_player()
	{
		return player;
	}
	
	public int get_type()
	{
		return type;
	}
	
	public String toString()
	{
		return "{(" + i + "," + j + ")," + player + "," + type + "}";
	}
	
	// get i and j in 
	public int[] get_index()
	{
		return new int[] {i,j};
	}
}
