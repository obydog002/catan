package src.game.engine;

import src.game.CatanEngine;
import src.game.Board;

import src.agent.Agent;

// state class at any point in the game
// store info of board and other relevant info
public class State
{
	// what resource cards everyone holds
	private int[][] res_held;
	
	// development cards held by players
	// 0 - knight
	// 1 - victory point
	// 2 - monopoly
	// 3 - road builder
	// 4 - year of plenty
	private int[][] dev_held;
	
	// scores of players
	private int[] scores;
	
	public State(int amount_players)
	{
		res_held = new int[amount_players][5];
		dev_held = new int[amount_players][5];
		scores = new int[amount_players];
	}
}