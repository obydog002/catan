package catan.game;

// either game engine or game observer
// essentially the difference is that the game engine simulates dice rolls, handles trades between players,
// does stealing from robber, validates building costs from resources, dev cards etc..
// observer means the instance will only show the board, and handle trades for any agents/players that are local 
// to the engine. The instance can't see what other players have in dev cards for instance, or what they steal
// with robber
public abstract class Catan
{
	Board board = null;
	
	public Catan()
	{
		this.board = new Board();
	}
	
	public void setup(int length, int type)
	{
		if (type == 0)
			board.set_reg_hex(length);
		else
			board.set_ext_hex(length);
		
		board.test_randomize_all();
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
	
	/*public static void main(String[] arg)
	{
		Catan c = new Catan();
		c.board.set_reg_hex(3);
		c.board.print_test();
	}*/
}