package src.gui;

import java.util.Random;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

// a class to represent creating a player in optionsPanel
// with all the neccessary info
public class PlayerOptionPanel extends JPanel implements ActionListener
{
	private Random rng;
	
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
	
	private static int player_amount = -1;
	
	public PlayerOptionPanel(int player_num, int initial_color, boolean team_enabled, boolean starting_enabled)
	{
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
		team.setActionCommand("change_team");
		team.addActionListener(this);
		if (team_enabled)
		{
			this.team_number = player_num;
			team.setText(this.team_number + "");
		}

		starting = new JTextField();
		starting.setEditable(starting_enabled);
		starting.setActionCommand("change_pos");
		starting.addActionListener(this);
		if (starting_enabled)
		{
			this.starting_number = player_num;
			starting.setText(this.starting_number + "");
		}
		
		this.setLayout(new GridLayout(1,0));
		
		this.add(type);
		this.add(name);
		this.add(color_box);
		this.add(color_picker);
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
	public int get_color()
	{
		return color_box.get_hex();
	}
	
	public int get_team()
	{
		return this.team_number;
	}
	
	public int get_pos()
	{
		return this.starting_number;
	}
	
	// my own method because Integer.decode is not good for this it seems
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
		}
		else if (act == "change_name")
		{
			String name_str = name.getText();
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
		}
		else if (act == "change_team")
		{
			try
			{
				int team_num = Integer.parseInt(team.getText());
				team_number = team_num;
			}
			catch (Exception exc)
			{
				JOptionPane.showMessageDialog(this, "Team number must be a numeric value.", "Validation Error", JOptionPane.ERROR_MESSAGE);
				team.setText(player_num + "");
				team_number = player_num;
			}
		}
		else if (act == "change_pos")
		{
			try
			{
				int start_pos = Integer.parseInt(starting.getText());
				starting_number = start_pos;
			}
			catch (Exception exc)
			{
				JOptionPane.showMessageDialog(this, "Starting position must be a numeric value between 1 and " + player_amount + " inclusive.", "Validation Error", JOptionPane.ERROR_MESSAGE);
				team.setText(player_num + "");
				starting_number = player_num;
			}
		}
	}
}