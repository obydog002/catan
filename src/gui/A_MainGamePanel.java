package src.gui;

import javax.swing.*;
import java.awt.*;

public class A_MainGamePanel extends JPanel
{
    public A_MainGamePanel(JFrame frame, GameData game_data)
    {
		frame.getContentPane().removeAll();

        setLayout(new GridLayout(1,0));
        BoardPanel board = new BoardPanel(game_data);
        JPanel side_panel = new JPanel();
        GameInfoPanel game_info = new GameInfoPanel(game_data);
		BoardOptionsPanel options_panel = new BoardOptionsPanel();
		InputHandler input = new InputHandler(board, options_panel);

        add(board);
        side_panel.setLayout(new GridLayout(0,1));
        side_panel.add(game_info); 
        side_panel.add(options_panel);
        add(side_panel);
		
		frame.setVisible(false);
		frame.add(this);
		
        frame.revalidate();
        frame.setTitle("Catan - Game");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
    }

}