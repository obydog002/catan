package src.game.engine;

import src.game.CatanEngine;
import src.game.Board;

import src.agent.Agent;

// state class at any point in the game
// store info of board and other relevant info
public class State
{
	private Board board;
	private Agent[] agents;
	
	// what resource cards everyone holds
	private int[][] res_held;
	
	// development cards held by players
	private int[][] dev_held;
	
	// scores of players
	private int[] scores;
	
	
	public State()
	{
		
	}
}