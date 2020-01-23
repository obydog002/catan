package src.gui;

import src.game.Config;

import javax.swing.*;
import java.awt.*;

import java.awt.event.*;

import java.util.Random;

/*
a panel that allows a user to choose options relating to the game
what mode it is, game mode options, players,
*/
public class OptionsPanel extends JPanel implements ActionListener
{
	public static final int POWER2_24 = 16777216;
	
	private Random rng;
	
	private JPanel options[];
	
	// parent frame
	private JFrame frame;
	
	private JPanel player_scroll_header;
	private JTextField type_header;
	private JTextField name_header;
	private JTextField color_header;
	private JTextField color_picker_header;
	private JTextField team_header;
	private JTextField starting_header;
	
	private JScrollPane player_scroll_pane;
	private JPanel player_select_panel;
	private PlayerOptionPanel player_options_array[];
	
	private JRadioButton engine;
	private JRadioButton observer;
		
	private JRadioButton reg;
	private JRadioButton ext;
	
	private JPanel size;
	private JLabel size_label;
	private JTextField size_input;
	
	private JPanel players;
	private JLabel players_label;
	private JTextField players_input;
	
	private JCheckBox special_build;
	
	private JCheckBox team;
	
	private JCheckBox starting;
	
	private JButton submit;
	
	private GameData game_data;
	
	public OptionsPanel(JFrame frame, long seed)
	{
		this.frame = frame;
		this.game_data = new GameData();
		
		rng = new Random(seed);
		
		this.setPreferredSize(new Dimension(700, 400));
		setLayout(new GridLayout(1,0));
		
		options = new JPanel[3];
		
		options[0] = new JPanel(new GridLayout(0,1));
		options[1] = new JPanel(new GridLayout(0,1));
		options[2] = new JPanel(new GridLayout(0,1));
		
		player_scroll_header = new JPanel(new GridLayout(1,0));
		type_header = new JTextField("type of player");
		type_header.setEditable(false);
		name_header = new JTextField("name of player");
		name_header.setEditable(false);
		color_header = new JTextField("color");
		color_header.setEditable(false);
		color_picker_header = new JTextField("color selection");
		color_picker_header.setEditable(false);
		team_header = new JTextField("team number");
		team_header.setEditable(false);
		starting_header = new JTextField("starting number");
		starting_header.setEditable(false);
		
		player_scroll_header.add(type_header);
		player_scroll_header.add(name_header);
		player_scroll_header.add(color_header);
		player_scroll_header.add(color_picker_header);
		player_scroll_header.add(team_header);
		player_scroll_header.add(starting_header);
		player_select_panel = new JPanel(new GridLayout(0,1));	
		
		engine = new JRadioButton("engine");	
		observer = new JRadioButton("observer");
		
		reg = new JRadioButton("catan");	
		ext = new JRadioButton("catan extension");
	
		engine.setActionCommand("engine");
		observer.setActionCommand("observer");
		
		ButtonGroup group = new ButtonGroup();
		
		group.add(engine);
		group.add(observer);
		
		engine.addActionListener(this);
		observer.addActionListener(this);
		
		options[0].add(engine);
		options[0].add(observer);
		
		reg.setActionCommand("reg");
		ext.setActionCommand("ext");
		
		reg.addActionListener(this);
		ext.addActionListener(this);
		
		size = new JPanel(new GridLayout(1,0));
		size_label = new JLabel("size of board");
		size_input = new JTextField("3",3);
		
		size.add(size_label);
		size.add(size_input);
		
		players = new JPanel(new GridLayout(1,0));
		players_label = new JLabel("number of players");
		players_input = new JTextField("4",3);
		
		players.add(players_label);
		players.add(players_input);
		
		special_build = new JCheckBox("special building");
		team = new JCheckBox("enable teams");
		starting = new JCheckBox("choose starting positions");
		
		submit = new JButton("Choose players");
		submit.setActionCommand("submit");
		submit.addActionListener(this);
		
		add(options[0]);
		add(options[1]);
		add(options[2]);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String act = e.getActionCommand();
		
		if (act == "engine")
		{
			game_data.engine_mode = 0;
			
			options[1].removeAll();
			options[2].removeAll();
			
			ButtonGroup group = new ButtonGroup();
			
			group.add(reg);
			group.add(ext);
			
			options[1].add(reg);
			options[1].add(ext);
			
			options[1].revalidate();
			options[1].repaint();
			
			options[2].removeAll();
			options[2].repaint();
		}
		else if (act == "observer")
		{
			game_data.engine_mode = 1;
			
			options[1].removeAll();
			options[2].removeAll();
			
			JLabel label = new JLabel("work in progress!");
			
			options[1].add(label);
			
			options[1].revalidate();
			options[1].repaint();
			
			options[2].revalidate();
			options[2].repaint();
		}
		else if (act == "reg" || act == "ext")
		{
			if (act == "reg")
			{
				game_data.game_mode = 0;
				
				size_input.setText("3");
				players_input.setText("4");
				special_build.setSelected(false);
				team.setSelected(false);
				team.setSelected(false);
			}
			else
			{
				game_data.game_mode = 1;
				
				size_input.setText("4");
				players_input.setText("6");
				special_build.setSelected(true);
				team.setSelected(false);
				team.setSelected(false);
			}
			
			options[2].removeAll();
			
			options[2].add(size);
			options[2].add(players);
			options[2].add(special_build);
			options[2].add(team);
			options[2].add(starting);
			options[2].add(submit);
			
			options[2].revalidate();
			options[2].repaint();
		}
		else if (act == "submit")
		{
			String player_str = players_input.getText();
			String size_str = size_input.getText();
			boolean players_validated = true;
			boolean size_validated = true;
			
			int player_lower = 1;
			int player_upper = 1000000; // 1 million should be enough
			int size_lower = 1;
			int size_upper = 1000000;
			
			boolean ok = true;
			boolean first = true;
			
			players_validated = validate(player_str, player_lower, player_upper);
			size_validated = validate(size_str, size_lower, size_upper);
			
			String error = "";
			
			if (!players_validated)
			{
				ok = false;
				
				if (first)
				{
					first = false;
				}
				else
				{
					error += "\n";
				}
				
				error += "Amount of players must be between " + player_lower + " and " + player_upper + ".";
			}
			
			if (!size_validated)
			{
				ok = false;
				
				if (first)
				{
					first = false;
				}
				else
				{
					error += "\n";
				}
				
				error += "Size of board must be between " + size_lower + " and " + size_upper + ".";
			}
			
			// validate failed
			if (!ok)
			{
				JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				boolean special_build_enabled = false;
				boolean team_enabled = false;
				boolean starting_enabled = false;

				special_build_enabled = special_build.isSelected();
				team_enabled = team.isSelected();
				starting_enabled = starting.isSelected();
				
				game_data.players_amount = Integer.parseInt(player_str);
				game_data.board_size = Integer.parseInt(size_str);
				
				this.removeAll();
				
				player_select_panel.add(player_scroll_header);
				
				player_options_array = new PlayerOptionPanel[game_data.players_amount];
				
				for (int i = 0; i < game_data.players_amount; i++)
				{
					// use stock colors first
					int initial_color = 0;
					if (i < 6)
						initial_color = Config.PLAYER_COLORS[i];
					else
						initial_color = rng.nextInt(POWER2_24) + 0xFF000000;
					
					player_options_array[i] = new PlayerOptionPanel(i + 1, initial_color, team_enabled, starting_enabled, rng);
					player_select_panel.add(player_options_array[i]);
				}
				
				player_scroll_pane = new JScrollPane(player_select_panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				
				submit.setActionCommand("submit2");
				submit.setText("Play Game");
				
				this.setLayout(new GridLayout(0,1));
				this.add(player_scroll_pane);
				this.add(submit);
				
				this.revalidate();
				this.repaint();
			}
		}
		else if (act == "submit2")
		{	
			game_data.special_building_enabled = false;
			game_data.team_enabled = false;
			game_data.starting_enabled = false;

			if (game_data.game_mode == 1) // 6 player catan
			{
				game_data.special_building_enabled = true;
			}
			else if (game_data.game_mode > 1) // variable type games
			{
				game_data.special_building_enabled = special_build.isSelected();
				game_data.team_enabled = team.isSelected();
				game_data.starting_enabled = starting.isSelected();
			}
			
			boolean ok = true;
			if (game_data.starting_enabled)
			{
				// validate starting positions
				
				int len = player_options_array.length;
				boolean pos_seen[] = new boolean[len];
				for (int i = 0; i < len && ok; i++)
				{
					int pos = player_options_array[i].get_pos();
					
					if (pos < 1 || pos > len)
					{
						ok = false;
					}
					else if (pos_seen[pos - 1]) // seen already
					{
						ok = false;
					}
					else
					{
						pos_seen[pos - 1] = true;
					}
					
				}
			}
			
			if (ok)
			{
				game_data.types = new String[game_data.players_amount];
				game_data.names = new String[game_data.players_amount];
				game_data.colors = new int[game_data.players_amount];
				game_data.teams = new int[game_data.players_amount];
				game_data.poses = new int[game_data.players_amount];
				
				for (int i = 0; i < game_data.players_amount; i++)
				{
					game_data.types[i] = player_options_array[i].get_type();
					game_data.names[i] = player_options_array[i].get_name();
					game_data.colors[i] = player_options_array[i].get_color();
					game_data.teams[i] = player_options_array[i].get_team();
					game_data.poses[i] = player_options_array[i].get_pos();
				}
				
				create_game_panels();
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Starting positions must be from 1 to " + player_options_array.length + " and may not have repeats.", "Validation Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	// pass player data to board panels, and cause parent frame to draw board
	public void create_game_panels()
	{
		removeAll();
		
		frame.getContentPane().removeAll();
		
		frame.setVisible(false);
		
		JPanel main_panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		BoardSetupPanel setup_panel = new BoardSetupPanel();
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		main_panel.add(setup_panel, c);
		
		BoardPanel board_panel = new BoardPanel(game_data, rng);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridwidth = 3;
		c.weightx = 1;
		c.weighty = 1;
		main_panel.add(board_panel, c);
		
		JPanel test1 = new JPanel();
		c.fill = GridBagConstraints.NONE;
		c.gridx = 4;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		main_panel.add(test1, c);
		
		frame.add(main_panel);
		
		frame.setLocationRelativeTo(null);
		
		frame.revalidate();
		frame.pack();
		frame.setVisible(true);
		frame.repaint();
	}
	
	// takes the input and checks:
	// - that it is a number
	// - it is between lower and higher, inclusive i.e [lower,higher]
	public boolean validate(String input, int lower, int higher)
	{
		int numb = 0;
		try
		{
			numb = Integer.parseInt(input);
		}
		catch (Exception e)
		{
			return false;
		}
		
		return numb >= lower && numb <= higher;
	}
}