package src.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Color;

import src.game.GameData;

import src.agent.Agent;

public class GameInfoPanel extends JPanel
{
    private JLabel title;
	
    private JLabel players[];
	
	// additional information to tack on to the end of each label
	// like current turn, dice roll
	private JLabel info[];
	
    public GameInfoPanel(GameData game_data)
    {
        this.setLayout(new GridLayout(0,1));
		
		Font font = new Font("Comic Sans MS", Font.BOLD, 20);
		
        title = new JLabel("Players");
		title.setFont(font);
		
		this.add(title);
		
        players = new JLabel[game_data.players_amount];
		info = new JLabel[game_data.players_amount];
		
        for(int i = 0; i < game_data.players_amount; i++)
        {
			JPanel hold = new JPanel(new GridLayout(1,2));
			
			players[i] = new JLabel();
			players[i].setFont(font);
			players[i].setOpaque(true);
			
			info[i] = new JLabel("");
			info[i].setFont(font);
			
			hold.add(players[i]);
			hold.add(info[i]);
			this.add(hold);
        }
    }
	
	// called when the starting order of the players has been decided
	public void set_order(Agent[] agents)
	{
		for (int i = 0; i < players.length; i++)
		{
			players[i].setText(agents[i].get_name());
			players[i].setBackground(agents[i].get_color());
			info[i].setText("");
		}
	}
	
	// to update corresponding infos for players
	public void update_info(int index, String update)
	{
		info[index].setText(update);
	}
}