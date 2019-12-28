package catan.game;

import java.util.Random;

public class CatanEngine extends Catan
{
	private static Random rng;
	
	public CatanEngine()
	{
		super();
		rng = new Random();
	}
	
	public CatanEngine(long seed)
	{
		super();
		rng = new Random(seed);
	}
	
	// singular dice roll
	public int dice_roll()
	{
		return rng.nextInt(6) + 1;
	}
	
	
}