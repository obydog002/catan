package src.game;

import java.awt.Color;

import src.agent.*;

import src.game.BoardSetupData;

// either game engine or game observer
// essentially the difference is that the game engine simulates dice rolls, handles trades between players,
// does stealing from robber, validates building costs from resources, dev cards etc..
// observer means the instance will only show the board, and handle trades for any agents/players that are local 
// to the engine. The instance can't see what other players have in dev cards for instance, or what they steal
// with robber
public interface Catan
{
	public void setup(int length, int type);
	
	public Board get_board();
	
	public Color[] get_player_colors();
	
	public void generate_ports(BoardSetupData setup);
	
	public void generate_board(BoardSetupData setup);
	
	public int[] set_player_order(int player, int[] working_order);
	
	public Agent[] get_agents();
	
	// gets an agent by its index
	public Agent get_agent(int index);
	
	public void clear_board();
	
	public int get_state();
}