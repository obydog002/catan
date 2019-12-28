package catan.gui;

// a class to store player data conviniently
public class PlayerInfo
{
	private String type;
	
	private String name;
	
	private int color;
	
	private int team_number;
	
	private int starting_position;
	
	public PlayerInfo()
	{
		team_number = -1;
		starting_position = -1;
	}
	
	public PlayerInfo(String type, String name, int color)
	{
		this.type = type;
		this.name = name;
		this.color = color;
		this.team_number = -1;
		this.starting_position = -1;
	}
	
	public PlayerInfo(String type, String name, int color, int team_number, int starting_position)
	{
		this.type = type;
		this.name = name;
		this.color = color;
		this.team_number = team_number;
		this.starting_position = starting_position;
	}
	
	public void set_type(String type)
	{
		this.type = type;
	}
	
	public void set_name(String name)
	{
		this.name = name;
	}
	
	public void set_color(int color)
	{
		this.color = color;
	}
	
	public void set_team(int team_number)
	{
		this.team_number = team_number;
	}
	
	public void set_pos(int starting_position)
	{
		this.starting_position = starting_position;
	}
	
	public String toString()
	{
		return this.type + " " + this.name + " " + this.color + " " + this.team_number + " " + this.starting_position;
	}
}