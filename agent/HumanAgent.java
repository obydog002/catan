package catan.agent;

import java.awt.Color;

// randomly does moves
public class HumanAgent implements Agent
{
	private Color color;
	
	public HumanAgent(Color color)
	{
		this.color = color;
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