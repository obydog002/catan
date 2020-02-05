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
	
	// starting order of agents
	// order[0] - pos of agents[0], from 1 to agents.length
	// if not set current state will be 0
	private int[] order;
	
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
		
		// starting positions have already been decided
		if (game_data.starting_enabled)
		{
			current_state = 1;
			this.order = game_data.poses;
		}
		else
		{
			current_state = 0;
			this.order = new int[agents.length];
		}
		
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
		
		String types[] = game_data.types; // agent types
		
		for (int i = 0; i < agents.length; i++)
		{
			if (types[i] == "Human")
			{
				agents[i] = new HumanAgent(new Color(game_data.colors[i]), game_data.names[i]);
			}
			else if (types[i] == "Random Bot")
			{
				agents[i] = new RandomAgent(new Color(game_data.colors[i]), game_data.names[i]);
			} 
			// more types to come...........
		}
	}
	
	// get current state
	public int get_state()
	{
		return current_state;
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
	
	// generate_ports
	public void generate_ports(BoardSetupData setup)
	{
		if (setup.regular_game)
		{
			if (game_data.game_mode == 0)
			{
				board.set_ports_reg();
			}
			else
			{
				board.set_ports_ext();
			}
		}
		else
		{
			// future ports algos
		}
	}
	
	// if its regular 3-catan or 4-ext it will use the standard setups
	// otherwise it generates randomly using get_tile_setup and token_setup
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
		
		generate_ports(setup);
	}
	
	// clears hexes/tokens on this board
	public void clear_board()
	{
		board.clear_board();
	}
}