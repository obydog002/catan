package src.gui;

import java.util.Random;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;

import src.game.Config;

// a class to represent creating a player in optionsPanel
// with all the neccessary info
public class PlayerOptionPanel extends JPanel
{
	// so we can create color dialogs
	private JFrame parent;
	
	private Random rng;
	
	private JComboBox<String> type;
	
	private JTextField name;
	
	private ColorBox color_box;
	
	private JButton change_color;
	
	private JTextField team;
	
	private JTextField starting;
	
	// variable to track how far down this player is on the list
	private int player_num;
	
	private int team_number = -1;
	
	private int starting_number = -1;
	
	private static int player_amount = -1;
	
	public PlayerOptionPanel(JFrame parent, int player_num, int initial_color, boolean team_enabled, boolean starting_enabled, Random rng)
	{
		this.rng = rng;
		
		this.player_num = player_num;
		
		type = new JComboBox<>(Config.AGENT_TYPES);
		type.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String new_name = type.getSelectedItem().toString();
				name.setText(new_name + " " + player_num);
			}
		});
		
		name = new JTextField("Human " + player_num);
		
		color_box = new ColorBox(initial_color);
		
		//raw_hex = String.format("0x%08x", initial_color);
		//raw_hex = raw_hex.toUpperCase();
		
		change_color = new JButton("change color");
		change_color.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ColorDialog dialog = new ColorDialog(parent, color_box.get_hex());
				
				color_box.set_color(dialog.run());
			}
		});
		
		team = new JTextField();
		team.setEditable(team_enabled);
		this.team_number = player_num;
		if (team_enabled)
		{
			team.setText(this.team_number + "");
		}

		starting = new JTextField();
		starting.setEditable(starting_enabled);
		this.starting_number = player_num;
		if (starting_enabled)
		{
			starting.setText(this.starting_number + "");
		}
		
		this.setLayout(new GridLayout(1,0));

		this.add(type);
		this.add(name);
		this.add(color_box);
		this.add(change_color);
		this.add(team);
		this.add(starting);
	}
	
	public String get_type()
	{
		return (String)type.getSelectedItem();
	}
	
	public String get_name()
	{
		return name.getText();
	}
	
	// returns hex 
	// doesnt need to be string as we validate here
	public int get_color()
	{
		return color_box.get_hex();
	}
	
	// strings for validation later on
	public String get_team()
	{
		if (team.isEditable()) // if it is enabled pull from textbox
			return team.getText();
		else // return the raw team value
			return team_number + "";
	}
	
	public String get_pos()
	{
		if (starting.isEditable()) // pull from textbox
			return starting.getText();
		else
			return starting_number + "";
	}
}