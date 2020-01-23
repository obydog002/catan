package src.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

// a panel for setting up the board in the beginning
public class BoardSetupPanel extends JPanel
{
	// whether the board should only take tiles and tokens from configurations of the original games
	// meaning if its regular catan or ext it will have the right amount of tiles, tokens etc..
	// will be grayed out if its some variant like 4-sized board normal catan 
	private JCheckBox reg_settings;
	
	// whether we enforce 6_8 rule in board generation or manual placement
	private JCheckBox rule6_8;
	
	// labels
	private JLabel tile_label;
	private JLabel token_label;
	
	// generate tiles and tokens
	private JButton generate_board;
	
	// confirm board and start game
    private JButton confirm;

    public BoardSetupPanel(boolean reg_game)
    {
        this.setLayout(new GridLayout(0,1));
		
		reg_settings = new JCheckBox("regular game settings");
		reg_settings.setSelected(true);
		
		tile_label = new JLabel("tile options");
		
		token_label = new JLabel("token options");
		rule6_8 = new JCheckBox("6-8 token rule");
		rule6_8.setSelected(true);
		
		generate_board = new JButton("generate board");
		generate_board.setActionCommand("generate");
		
		confirm = new JButton("confirm");
        confirm.setActionCommand("confirm");

		// if this isnt 3-catan and 4-ext,
		// then disable reg_settings as they dont apply
		// and make everything not selected
		if (!reg_game)
		{
			reg_settings.setEnabled(false);
			reg_settings.setSelected(false);
			rule6_8.setSelected(false);
		}
		
		this.add(reg_settings);
		this.add(tile_label);
		this.add(token_label);
		this.add(rule6_8);
		this.add(generate_board);
        this.add(confirm);
    }

}