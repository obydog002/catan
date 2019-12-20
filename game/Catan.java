package catan.game;

import java.awt.Color;

// either game engine or game observer
// essentially the difference is that the game engine simulates dice rolls, handles trades between players,
// does stealing from robber, validates building costs from resources, dev cards etc..
// observer means the instance will only show the board, and handle trades for any agents/players that are local 
// to the engine. The instance can't see what other players have in dev cards for instance, or what they steal
// with robber
public abstract class Catan
{
	Board board = null;
	
	private Color[] player_colors;
	
	public Catan()
	{
		this.board = new Board();
		player_colors = new Color[4];
	}
	
	public void setup(int length, int type)
	{
		if (type == 0)
			board.set_reg_hex(length);
		else
			board.set_ext_hex(length);
		
		board.test_randomize_all();
		
		player_colors[0] = Color.WHITE;
		player_colors[1] = Color.RED;
		player_colors[2] = Color.BLUE;
		player_colors[3] = Color.ORANGE;
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
		return player_colors;
	}
	
	/*public static void main(String[] arg)
	{
		Catan c = new Catan();
		c.board.set_reg_hex(3);
		c.board.print_test();
	}*/
}