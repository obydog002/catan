package catan.game;

public class Catan
{
	Board board;
	
	public Catan()
	{
		this.board = new Board();
	}
	
	public static void main(String[] arg)
	{
		Catan c = new Catan();
		c.board.set_reg_hex(3);
		c.board.print_test();
	}
}