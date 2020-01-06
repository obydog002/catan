package catan.game;

import java.awt.Color;

import catan.agent.*;

// either game engine or game observer
// essentially the difference is that the game engine simulates dice rolls, handles trades between players,
// does stealing from robber, validates building costs from resources, dev cards etc..
// observer means the instance will only show the board, and handle trades for any agents/players that are local 
// to the engine. The instance can't see what other players have in dev cards for instance, or what they steal
// with robber
public abstract class Catan
{
	Board board = null;
	
	private Agent[] agents;
	
	// if special building is enabled
	private boolean special_building;
	
	public Catan()
	{
		this.board = new Board();
		agents = new Agent[4];
		special_building = false;
	}
	
	public void setup(int length, int type)
	{
		if (type == 0) 
		{
			board.set_reg_hex(length);
			
			if (length == 3)
				board.set_board(Config.REG_TILES, Config.REG_TOKENS, true);
			else
				board.test_randomize_all();
		}
		else
		{
			board.set_ext_hex(length);
			
			if (length == 4)
				board.set_board(Config.EXT_TILES, Config.EXT_TOKENS, true);
			else
				board.test_randomize_all();
		}
		
		board.initialize_nodes_normal();
		
		agents[0] = new RandomAgent(Color.WHITE);
		agents[1] = new RandomAgent(Color.BLUE);
		agents[2] = new RandomAgent(Color.RED);
		agents[3] = new RandomAgent(Color.ORANGE);
	}
	
	public void print_state()
	{
		board.print_test();
	}
	
	// get current board
	public Board getBoard()
	{
		return board;
	}
	
	public Color[] get_player_colors()
	{
		Color[] colors = new Color[agents.length];
		for (int i = 0; i < agents.length; i++)
		{
			colors[i] = agents[i].get_color();
		}
		
		return colors;
	}
	
	// reset method for testing
	// just randomizes it
	public void reset_board()
	{
		if (board.get_type() == 0 && board.get_length() == 3)
			board.set_board(Config.REG_TILES, Config.REG_TOKENS, true);
		else if (board.get_type() == 1 && board.get_length() == 4)
			board.set_board(Config.EXT_TILES, Config.EXT_TOKENS, true);
	}
	
	/*public static void main(String[] arg)
	{
		Catan c = new Catan();
		c.board.set_reg_hex(3);
		c.board.print_test();
	}*/
}