package catan.game;

import java.util.Random;
import catan.agent.*;
import java.awt.Color;

public class CatanEngine implements Catan
{
	private static Random rng;
	
	Board board = null;
		
	private Agent[] agents;
	
	private catan.gui.GameData game_data;

	// if special building is enabled
	private boolean special_building;

	public CatanEngine(long seed)
	{
		super();
		rng = new Random(seed);
	}
	
	public CatanEngine(catan.gui.GameData game_data)
	{
		this.game_data = game_data;
		this.board = new Board();
		agents = new Agent[4];
		special_building = false;
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
		if (type == 0) 
		{
			board.set_reg_hex(length);
			if (length == 3)
				board.set_board(Config.REG_TILES, Config.REG_TOKENS, true);
		}
		else
		{
			board.set_ext_hex(length);
			if (length == 4)
				board.set_board(Config.EXT_TILES, Config.EXT_TOKENS, true);
		}
			
		agents[0] = new RandomAgent(Color.RED);
		agents[1] = new RandomAgent(Color.BLUE);
		agents[2] = new RandomAgent(Color.RED);
		agents[3] = new RandomAgent(Color.ORANGE);
	}
		
	public void print_state()
	{
		board.print_test();
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
	// just random-izes it
	public void reset_board()
	{
		if (board.get_type() == 0 && board.get_length() == 3)
			board.set_board(Config.REG_TILES, Config.REG_TOKENS, true);
		else if (board.get_type() == 1 && board.get_length() == 4)
			board.set_board(Config.EXT_TILES, Config.EXT_TOKENS, true);
	}

}