package src.game;

import java.util.Random;

import java.awt.Color;

import src.game.Config;
import src.game.GameData;
import src.game.BoardSetupData;

import src.game.engine.State;

import src.agent.*;

// handles game and validation

public class CatanEngine implements Catan
{
	private Random rng;
	
	private Board board = null;
	
	// order of this array is turn order of agents
	// i.e. agents[0] is person to go first
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
	
	private int current_player;
	
	// constructor used for test purposes
	public CatanEngine()
	{
		rng = new Random();
		agents = null;
		game_data = null;
		current_state = 0;
		state = null;
	}
	
	public CatanEngine(GameData game_data, Random rng)
	{
		this.rng = rng;
		
		this.game_data = game_data;
		this.board = new Board(rng);
		
		current_player = 0;
		
		agents = new Agent[game_data.players_amount];
		
		setup(game_data.board_size, game_data.game_mode);
		
		// starting positions have already been decided
		if (game_data.starting_enabled)
		{
			current_state = 1;
			set_actual_order(game_data.poses);
		}
		else
		{
			current_state = 0;
		}
	}
	
	public Agent[] get_agents()
	{
		return agents;
	}
	
	public Agent get_agent(int index)
	{
		return agents[index];
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
			if (types[i] == Config.AGENT_TYPES[0]) // human
			{
				agents[i] = new HumanAgent(new Color(game_data.colors[i]), game_data.names[i]);
			}
			else if (types[i] == Config.AGENT_TYPES[1]) // random bot
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
	
	// interface methods for setting various player things
	
	// sequentially (1 per call) determines orders of players as per catan rules
	// e.g. for 4 players 4 calls needed initially to determine rolls,
	// then if any are the same those are pitted against each-other again until there is one left
	// in those subsequent rounds set working_rolls[player] < 0 (= -1) so that it doesnt process them
	// player - current index to process
	// working_rolls - an array supplied at each step which has the current order being decided
	// initially 0 means no roles on this player yet
	// return the dice roll itself if needed, aswell as a flag
	// flag values: (int[2] of return)
	// -1 error, 0 : (working_rolls.length - 1) next player to roll, (working_rolls.length) process finished on current player
	// (so current player is winner)
	public int[] set_player_order(int player, int[] working_rolls)
	{
		this.current_player = player;
		
		int d1 = dice_roll();
		int d2 = dice_roll();
		
		working_rolls[player] = d1 + d2;
		
		// counts how many -1 there are if players have been eliminated from this process
		int j = working_rolls.length - 1;
		while (working_rolls[j] < 0) 
		{
			j--;
		}
		
		if (player == j) // last player rolled
		{
			// get max dice roll from this set
			int max = -1;
			for (j = 0; j < working_rolls.length; j++)
			{
				if (working_rolls[j] > max)
				{
					max = working_rolls[j];
				}
			}
			
			int players_same = 0;
			int winner_index = 0;
			// then check if there is 1 winner or not
			for (j = 0; j < working_rolls.length; j++)
			{
				if (working_rolls[j] == max)
				{
					players_same++;
					winner_index = j;
				}
				else
					working_rolls[j] = -1;
			}
			
			if (players_same == 1) // winner found
			{
				// set our agents array to reflect the order given
				int order[] = new int[working_rolls.length];
				j = winner_index;
				int count = 0;
				while (count < working_rolls.length)
				{
					order[j] = count;
					
					count++;
					j++;
					if (j == working_rolls.length)
						j = 0;
				}
				
				set_actual_order(order);
				
				return new int[] {d1, d2, working_rolls.length};
			}
			else // no winner found
			{
				// set remaining players to 0
				for (j = 0; j < working_rolls.length; j++)
				{
					if (working_rolls[j] > -1)
						working_rolls[j] = 0;
				}
				
			}
		}
		
		// find next player
		j = player + 1;
		if (j == working_rolls.length)
			j = 0; // set back to beginning
		
		while (working_rolls[j] != 0)
		{
			j++;
			if (j == working_rolls.length)
				j = 0;
		}
		
		return new int[] {d1, d2, j};
	} 

	// called when  the order is known through working_rolls
	// will move the agents array to match the order given by order
	public void set_actual_order(int[] order)
	{
		Agent temp[] = new Agent[order.length];

		current_player = 0;
		
		current_state = 1;
		
		for (int i = 0; i < order.length; i++)
		{
			temp[order[i]] = agents[i];
		}
		
		agents = temp;
	}
	
	private boolean backwards = false;
	
	// uses the temp boolean value to know when to go backwards
	// then a value of -1 will indicate the end
	public int initial_placement_next_turn(int current_player)
	{
		if (current_player == agents.length - 1)
			backwards = true;
		
		if (backwards)
			return current_player - 1;
		
		return current_player + 1;
	}
	
	
	// these methods return -1 if cant do and 0 if successful
	
	// checks if there can be a road at i,j for current player
	// doesn't do it, just tests
	public int eligable_road(int i, int j, int current_player)
	{
		Edge edges[][] = board.get_edges();
		if (i < 0 || i >= edges.length)
			return -1;
		
		if (j < 0 || j >= edges[i].length)
			return -1;
		
		if (edges[i][j].eligable_placement(current_player)) // can be set
			return 0;
		
		return -1;
	}
	
	// requests a road at i,j index for current_player
	public int request_road(int i, int j, int current_player)
	{
		if (eligable_road(i, j, current_player) == 0) // set it
		{
			Edge edges[][] = board.get_edges();
			edges[i][j].set_player(current_player);
			edges[i][j].set_type(0);
			return 0;
		}
		
		return -1;
	}
	
	// checks if there can be house/city at i,j
	// doesnt do it, just checks
	public int eligable_building(int i, int j, int current_player, int type, boolean free_house)
	{
		Vertex vertices[][] = board.get_vertices();
		if (i < 0 || i >= vertices.length)
			return -1;
		
		if (j < 0 || j >= vertices[i].length)
			return -1;
		
		if (vertices[i][j].eligable_placement(current_player, type, free_house))
			return 0;
		
		return -1;
	}
	
	// request house or city at i,j index for current_player
	// type - 0 house, 1 city
	// free_house - if true means that a house can be placed anywhere without connections to roads
	public int request_building(int i, int j, int current_player, int type, boolean free_house)
	{
		if (eligable_building(i, j, current_player, type, free_house) == 0)
		{
			Vertex[][] vertices = board.get_vertices();
			vertices[i][j].set(current_player, type);
			return 0;
		}
		
		return -1;
	}
	
	// testing of set_player_order
	public static void main(String[] arg)
	{
		CatanEngine c = new CatanEngine();
		
		int[] order = {0,0,0,0};
		int player = 0;
		
		boolean done = false;
		while (!done)
		{
			int[] res = c.set_player_order(player,order);
			
			System.out.println("rolls: " + res[0] + "," + res[1] + "\t" + "next player: " + res[2]);
			
			System.out.println("order value: ");
			for (int i = 0; i < order.length; i++)
			{
				System.out.print(order[i] + ",");
			}
			
			System.out.println();
			
			if (res[2] == order.length)
				done = true;
			
			player = res[2];
		}
	}
}