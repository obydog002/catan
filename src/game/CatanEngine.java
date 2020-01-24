package src.game;

import java.util.Random;

import java.awt.Color;

import src.gui.GameData;
import src.gui.BoardSetupData;

import src.game.engine.State;

import src.agent.*;

// handles game and validation

public class CatanEngine implements Catan
{
	private Random rng;
	
	Board board = null;
		
	private Agent[] agents;
	
	private GameData game_data;

	// state of the game
	// 0 - determine start order of players
	// 1 - initial placement of houses/roads
	// 2 - special building (if enabled)
	// 3 - current player roll dice 
	// 4 - pick-up/discard/move-robber
	// 5 - player turn: building/buy dev cards, trading (including ports), playing dev cards
	// used to make sure flow of the game is followed properly
	private int current_state;
	
	// object to contain board and player info and to validate majority of moves
	private State state;
	
	public CatanEngine(GameData game_data, Random rng)
	{
		this.rng = rng;
		
		this.game_data = game_data;
		this.board = new Board(rng);
		
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
		if (type == 0)
		{
			board.set_reg_hex(length);
			
		}
		else if (type == 1)
		{
			board.set_ext_hex(length);
		}
		
		board.initialize_nodes_normal();
		
		// this will need to change eventually
		for (int i = 0; i < agents.length; i++)
		{
			agents[i] = new HumanAgent(new Color(game_data.colors[i]));
		}
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
	// if its regular catan 4-player or 6 it will use the standard setups
	// otherwise it generates randomly using test_randomize_all
	public void generate_board(BoardSetupData setup)
	{
		if (setup.regular_game)
		{
			// regular 3-catan
			if (game_data.game_mode == 0)
			{
				board.set_board(Config.REG_TILES, Config.REG_TOKENS, setup.rule6_8);
			}
			else // 4-ext
			{
				board.set_board(Config.EXT_TILES, Config.EXT_TOKENS, setup.rule6_8);
			}
		}
		else 
		{
			int tile_setup[] = board.get_tile_setup();
			int token[] = board.get_token_setup(tile_setup[5]);
			board.set_board(tile_setup, token, setup.rule6_8);
		}
	}

}