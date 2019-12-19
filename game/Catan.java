package catan.game;

public class Catan
{
	private Board board = null;
	
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