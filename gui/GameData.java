package catan.gui;

// a class to hold game data, like amount of players, board size, and so on....
// engine_mode - engine or observer
// game_mode - what type of game it is (0 - 4 player catan, 1 - extension, 2 - variable normal, 3 - variable ext)
// board_size - size of board
// players_amount - how many players are there
// special_building_enabled - whether there is special building enabled
// team_enabled - whether there are teams
// starting_enabled - whether the players have decided starting order before hand
// types - player types array
// names - player name array
// colors - player colors array
// teams - player team number
// poses - player starting order
public class GameData
{
	public int engine_mode;
	
	public int game_mode;
	
	public int board_size;
	
	public int players_amount;
	
	public boolean special_building_enabled;
	
	public boolean team_enabled;
	
	public boolean starting_enabled;
	
	public String types[];
	
	public String names[];
	
	public int colors[];
	
	public int teams[];
	
	public int poses[];

	public String toString()
	{
		String str = "";
		str += engine_mode + " " + game_mode + " " + board_size + " " + players_amount + " " + special_building_enabled + " " + team_enabled + " " + starting_enabled + "\n";
		for (int i = 0; i < players_amount; i++)
		{
			str += types[i] + "\t\t" + names[i] + "\t\t" + String.format("0x%08x",colors[i]) + "\t\t" + teams[i] + "\t\t" + poses[i] + "\n";
		}

		return str;
	}
}