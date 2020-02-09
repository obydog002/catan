package src.game;

// final arrays don't protect the indexes
// why?

// a config class to store initial setup info, that sort of thing
public abstract class Config
{
	// types of agents
	// 0 - human (controllable)
	// 1 - random agent (stupid idiot bot)
	public static final String AGENT_TYPES[] = {"Human", "Random Bot"};
	
	// player colors that come with the board game
	// red, blue, white, orange, brown, dark green
	public static final int PLAYER_COLORS[] = {0xFFFF0000, 0xFF0000FF, 0xFFFFFFFF, 0xFFFFFF00, 0xFF541305, 0xFF1E7B07};
	
	// 3 length reg board
	
	// 4 wood, 3 brick, 4 sheep, 4 wheat, 3 ore, 1 desert
	public static final int REG_TILES[] = {4,3,4,4,3,1};
	
	// 1 "2", 2 "3","4","5","6", 0 "7", 2 "8","9","10","11", 1 "12"
	public static final int REG_TOKENS[] = {1,2,2,2,2,0,2,2,2,2,1};
	
	// 14 knights, 5 victory point, 2 monopoly, 2 road builder, 2 year of plenty
	public static final int REG_DEV_CARDS[] = {14, 5, 2, 2, 2};
	
	// 19 of each resource card
	public static final int REG_RES_CARDS[] = {19,19,19,19,19};
	
	// port indexes for edges
	public static final int REG_PORT_INDEXES_I[] = {0,0,2,3,5,7,8,10,10};
	public static final int REG_PORT_INDEXES_J[] = {2,5,0,4,0,4,0,2 ,5 };
	
	// port values for edges
	public static final int REG_PORT_VALUES[] = {0,5,1,3,5,4,5,2,5};
	
	// port counts
	// 1 of each resource, 4 any ports
	public static final int REG_PORT_COUNTS[] = {1,1,1,1,1,4};
	
	// 4 length ext board
	// 6 wood, 5 brick, 6 sheep, 6 wheat, 5 ore, 2 desert
	public static final int EXT_TILES[] = {6,5,6,6,5,2};
	
	// 2 "2", 3 "3","4","5","6", 0 "7", 3 "8","9","10","11", 2 "12"
	public static final int EXT_TOKENS[] = {2,3,3,3,3,0,3,3,3,3,2};
	
	// 20 knights, 5 victory point, 3 monopoly, 3 road builder, 3 year of plenty
	public static final int EXT_DEV_CARDS[] = {20, 5, 3, 3, 3};
	
	// 24 of each resource card
	public static final int EXT_RES_CARDS[] = {24,24,24,24,24};
	
	// port indexes
	public static final int EXT_PORT_INDEXES_I[] = {0,0,1,3,4,6 ,7,9,12,14,14};
	public static final int EXT_PORT_INDEXES_J[] = {2,5,0,4,0,11,0,5,0 ,2 ,5 };
	
	// port values
	public static final int EXT_PORT_VALUES[] = {0,5,2,3,1,5,5,4,5,2,5};
	
	// port counts
	// 1 of each except 2 for sheep, 5 for any
	public static final int EXT_PORT_COUNTS[] = {1,1,2,1,1,5};
}