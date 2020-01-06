package src.game;

// final arrays don't protect the indexes
// why?

// a config class to store initial setup info, that sort of thing
public abstract class Config
{
	// player colors that come with the board game
	// red, blue, white, orange, brown, dark green
	public static final int[] PLAYER_COLORS = {0xFFFF0000, 0xFF0000FF, 0xFFFFFFFF, 0xFFFFFF00, 0xFF541305, 0xFF1E7B07};
	
	// 3 length reg board
	
	// 4 wood, 3 brick, 4 sheep, 4 wheat, 3 ore, 1 desert
	public static final int[] REG_TILES = {4,3,4,4,3,1};
	
	// 1 "2", 2 "3","4","5","6","8","9","10","11", 1 "12"
	public static final int[] REG_TOKENS = {1,2,2,2,2,2,2,2,2,1};
	
	// 14 knights, 5 victory point, 2 monopoly, 2 road builder, 2 year of plenty
	public static final int[] REG_DEV_CARDS = {14, 5, 2, 2, 2};
	
	// 19 of each resource card
	public static final int[] REG_RES_CARDS = {19,19,19,19,19};
	
	
	// 4 length ext board
	// 6 wood, 5 brick, 6 sheep, 6 wheat, 5 ore, 2 desert
	public static final int[] EXT_TILES = {6,5,6,6,5,2};
	
	// 2 "2", 3 "3","4","5","6","8","9","10","11", 2 "12"
	public static final int[] EXT_TOKENS = {2,3,3,3,3,3,3,3,3,2};
	
	// 20 knights, 5 victory point, 3 monopoly, 3 road builder, 3 year of plenty
	public static final int[] EXT_DEV_CARDS = {20, 5, 3, 3, 3};
	
	// 24 of each resource card
	public static final int[] EXT_RES_CARDS = {24,24,24,24,24};
}