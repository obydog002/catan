package src.game;

// a struct type class for storing data relevant to board setup
// for use in generation and selecting manaully
// regular_game - whether is 3-catan or 4-ext
// rule6_8 - whether the 6,8 token rule should be enforced
public class BoardSetupData
{
	public boolean regular_game;
	
	public boolean rule6_8;
	
	public String toString()
	{
		String str = regular_game + " " + rule6_8;
		
		return str;
	}
}