package src.agent;

import java.awt.Color;

// randomly does moves
public class HumanAgent implements Agent
{
	private Color color;
	
	// name of this agent
	private String name;
	
	public HumanAgent(Color color, String name)
	{
		this.color = color;
		this.name = name;
	}
	
	public Color get_color()
	{
        return color;
    }
    
    public Color get_alt_color()
	{
		return get_color().darker();
	}
}