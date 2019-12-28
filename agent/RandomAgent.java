package catan.agent;

import java.awt.Color;

// randomly does moves
public class RandomAgent implements Agent
{
	private Color color;
	
	public RandomAgent(Color color)
	{
		this.color = color;
	}
	
	public Color get_color()
	{
		return color;
	}
}