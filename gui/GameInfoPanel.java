
package catan.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Color;

public class GameInfoPanel extends JPanel
{

    private JLabel Title;
    private JLabel Players[];

    public GameInfoPanel(GameData game_data)
    {
        this.setLayout(new GridLayout(0,1));
        Title = new JLabel("Players");
        Players = new JLabel[game_data.players_amount];
        this.add(Title);

        for(int i=0; i < game_data.players_amount; i++)
        {
         Players[i]= new JLabel(game_data.names[i]);
         Players[i].setOpaque(true);
         Players[i].setBackground(Color.decode(Integer.toString(game_data.colors[i])).darker());
         this.add(Players[i]);
        }
    }

}