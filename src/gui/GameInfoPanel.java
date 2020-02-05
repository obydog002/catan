package src.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Color;

public class GameInfoPanel extends JPanel
{
    private JLabel title;
    private JLabel players[];

    public GameInfoPanel(GameData game_data)
    {
        this.setLayout(new GridLayout(0,1));
		
        title = new JLabel("Players");
		this.add(title);
		
        players = new JLabel[game_data.players_amount];

        for(int i = 0; i < game_data.players_amount; i++)
        {
			players[i] = new JLabel(game_data.names[i]);
			players[i].setOpaque(true);
			players[i].setBackground(new Color(game_data.colors[i]));
			this.add(players[i]);
        }
    }
}