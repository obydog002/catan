package src.game;

import java.util.Random;

import java.awt.Color;

import src.gui.GameData;
import src.agent.*;

public class CatanEngine implements Catan
{
	private static Random rng;
	
	Board board = null;
		
	private Agent[] agents;
	
	private GameData game_data;

	public CatanEngine(long seed)
	{
		super();
		rng = new Random(seed);
	}
	
	public CatanEngine(GameData game_data)
	{
		this.game_data = game_data;
		this.board = new Board();
		
		agents = new Agent[game_data.players_amount];
		
		setup(game_data.board_size, game_data.game_mode);
	}

	public void set_player_order()
	{

	} 

	// singular dice roll
	public int dice_roll()
	{
		return rng.nextInt(6) + 1;
	}
		
	public void setup(int length, int type)
	{
		if (type == 0 || type == 2)
		{
			board.set_reg_hex(length);
			board.initialize_nodes_normal();
			
			board.test_randomize_all();
		}
		else if (type == 1 || type == 3)
		{
			board.set_ext_hex(length);
			board.initialize_nodes_normal();
			
			board.test_randomize_all();
		}
		/*
		if (type == 0 || type == 2) 
		{
			board.set_reg_hex(length);
			board.initialize_nodes_normal();
			
			if (length == 3)
				board.set_board(Config.REG_TILES, Config.REG_TOKENS, true);
			else
				board.test_randomize_all();
		}
		else if (type == 1 || type == 3)
		{
			board.set_ext_hex(length);
			board.initialize_nodes_normal();
			
			if (length == 4)
				board.set_board(Config.EXT_TILES, Config.EXT_TOKENS, true);
			else
				board.test_randomize_all();
		}
		*/
		//board.test_print_nodes();
		board.test_print();
		board.test_print_nodes();
		
		for (int i = 0; i < agents.length; i++)
		{
			agents[i] = new HumanAgent(new Color(game_data.colors[i]));
		}
	}
		
	public void print_state()
	{
		board.test_print();
	}
		
	// get current board
	public Board get_board()
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
	// if its catan 4-player or 6 it will use the standard setups
	// otherwise it generates randomly using test_randomize_all
	public void reset_board()
	{
		if (board.get_type() == 0 && board.get_length() == 3)
			board.set_board(Config.REG_TILES, Config.REG_TOKENS, true);
		else if (board.get_type() == 1 && board.get_length() == 4)
			board.set_board(Config.EXT_TILES, Config.EXT_TOKENS, true);
		else
			board.test_randomize_all();
	}

}