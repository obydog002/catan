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
	int engine_mode;
	
	int game_mode;
	
	int board_size;
	
	int players_amount;
	
	boolean special_building_enabled;
	
	boolean team_enabled;
	
	boolean starting_enabled;
	
	String types[];
	
	String names[];
	
	int colors[];
	
	int teams[];
	
	int poses[];
}