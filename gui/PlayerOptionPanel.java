package catan.gui;

import java.util.Random;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

// a class to represent creating a player in optionsPanel
// with all the neccessary info
public class PlayerOptionPanel extends JPanel implements ActionListener
{
	private Random rng;
	
	// type of player
	private JComboBox type;
	
	// player types the player can choose
	// human - gives control to the user of gui when it is turn
	// Random Bot - stupid bot (dumb IDIOT bot, IDIOT)
	private static final String[] TYPES = {"Human", "Random Bot"};
	
	private JTextField name;
	
	private ColorBox color_box;
	
	private JTextField color_picker;
	
	private JTextField team;
	
	private JTextField starting;
	
	// variable to track how far down this player is on the list
	private int player_num;
	
	private int team_number = -1;
	
	private int starting_number = -1;
	
	private static PlayerInfo player_info[];
	private static boolean already_init = false;
	private static int player_amount = -1;
	
	public PlayerOptionPanel(int player_num, int initial_color, boolean team_enabled, boolean starting_enabled)
	{
		int index = player_num - 1;
		player_info[index] = new PlayerInfo("Human", "Human " + player_num, initial_color);
		
		rng = new Random();
		
		this.player_num = player_num;
		
		type = new JComboBox(TYPES);
		type.setActionCommand("change_type");
		type.addActionListener(this);
		
		name = new JTextField("Human " + player_num);
		name.setActionCommand("change_name");
		name.addActionListener(this);
		
		color_box = new ColorBox(initial_color);
		
		String color_str = String.format("0x%08x", initial_color);
		color_str = color_str.toUpperCase();
		
		color_picker = new JTextField(color_str);
		color_picker.setActionCommand("change_color");
		color_picker.addActionListener(this);
		
		team = new JTextField();
		team.setEditable(team_enabled);
		if (team_enabled)
		{
			this.team_number = player_num;
			team.setText(this.team_number + "");
			player_info[index].set_team(player_num);
		}

		starting = new JTextField();
		starting.setEditable(starting_enabled);
		if (starting_enabled)
		{
			this.starting_number = player_num;
			starting.setText(this.starting_number + "");
			player_info[index].set_pos(player_num);
		}
		
		this.setLayout(new GridLayout(1,0));
		
		this.add(type);
		this.add(name);
		this.add(color_box);
		this.add(color_picker);
		this.add(team);
		this.add(starting);
	}
	
	// to initialize the static variable players info
	// so we can keep track of player infos
	public static void init_infos(int number)
	{
		if (already_init) 
			return;
		else
		{
			player_info = new PlayerInfo[number];
			player_amount = number;
		}
	}
	
	public static PlayerInfo[] get_infos()
	{
		return player_info;
	}
	
	// my own method because Integer.decode is not good
	// throws an Exception if it can't be parsed
	public int parse_hex(String hex) throws Exception
	{
		// 1 byte for the 0x; 4 for the hex code
		if (hex.length() != 10)
		{
			throw new NumberFormatException();
		}
		
		char a[] = hex.toCharArray();
		
		if (a[0] != '0' || (a[1] != 'x' && a[1] != 'X'))
		{
			throw new NumberFormatException();
		}
		
		int number = 0;
		for (int i = 2; i < 10; i++)
		{
			
			int value = 0;
			
			if (a[i] >= 48 && a[i] <= 57) // 0 - 9
			{
				value = a[i] - 48;
			}
			else if (a[i] >= 65 && a[i] <= 70)
			{
				value = a[i] - 65 + 10;
			}
			else if (a[i] >= 97 && a[i] <= 102)
			{
				value = a[i] - 97 + 10;
			}
			else
			{
				throw new NumberFormatException();
			}
			
			number += value;
			
			if (i != 9)
				number = number << 4;
		}
		
		// System.out.println(String.format("0x%08x",number));
		
		return number;
	}

	public void actionPerformed(ActionEvent e)
	{
		String act = e.getActionCommand();
		
		if (act == "change_type")
		{
			String type_str = (String)type.getSelectedItem();
			name.setText(type_str + " " + player_num);
			player_info[player_num - 1].set_type(type_str);
			player_info[player_num - 1].set_name(type_str + " " + player_num);
		}
		else if (act == "change_name")
		{
			String name_str = name.getText();
			player_info[player_num - 1].set_name(name_str);
		}
		else if (act == "change_color")
		{
			String code = color_picker.getText();
			int hex = 0;
			try
			{
				hex = parse_hex(code);
			}
			catch (Exception exc)
			{
				hex = rng.nextInt();
				color_picker.setText(String.format("0x%08x", hex));
			}
			
			color_box.set_color(hex);
			player_info[player_num - 1].set_color(hex);
		}
	}
}