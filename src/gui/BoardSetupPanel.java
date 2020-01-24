package src.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

// a panel for setting up the board in the beginning
// this can be its own action listener
public class BoardSetupPanel extends JPanel implements ActionListener
{
	// an instance of board panel so we can call relevant methods
	private BoardPanel board_panel;
	
	// whether the board should only take tiles and tokens from configurations of the original games
	// meaning if its regular catan or ext it will have the right amount of tiles, tokens etc..
	// will be grayed out if its some variant like 4-sized board normal catan 
	private JCheckBox reg_settings;
	
	// whether we enforce 6_8 rule in board generation or manual placement
	private JCheckBox rule6_8;
	
	// labels
	private JLabel tile_label;
	private JLabel token_label;
	
	// rotate the board
	private JButton rotate_board;
	
	// generate tiles and tokens
	private JButton generate_board;
	
	// confirm board and start game
    private JButton confirm;

	// instance of board data to pass
	private BoardSetupData board_setup_data;
	
    public BoardSetupPanel(boolean reg_game, BoardPanel board_panel)
    {
		this.board_panel = board_panel;
		
		board_setup_data = new BoardSetupData();
		
        this.setLayout(new GridLayout(0,1));
		
		reg_settings = new JCheckBox("regular game settings");
		reg_settings.setSelected(true);
		reg_settings.setActionCommand("reg_settings");
		reg_settings.addActionListener(this);
		
		tile_label = new JLabel("tile options");
		
		token_label = new JLabel("token options");
		rule6_8 = new JCheckBox("6-8 token rule");
		rule6_8.setSelected(true);
		
		rotate_board = new JButton("rotate board");
		rotate_board.setActionCommand("rotate");
		rotate_board.addActionListener(this);
		
		generate_board = new JButton("generate board");
		generate_board.setActionCommand("generate");
		generate_board.addActionListener(this);
		
		confirm = new JButton("confirm");
        confirm.setActionCommand("confirm");
		confirm.addActionListener(this);
		
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
		this.add(rotate_board);
		this.add(generate_board);
        this.add(confirm);
    }

	// action listener
	public void actionPerformed(ActionEvent e)
	{
		String act = e.getActionCommand();
		
		if (act == "rotate")
		{
			board_panel.toggle_rotate();
		}
		else if (act == "generate")
		{
			board_setup_data.regular_game = reg_settings.isSelected();
			board_setup_data.rule6_8 = rule6_8.isSelected();
			
			board_panel.generate_board(board_setup_data);
		}
		else if (act == "confirm")
		{
			System.out.println("confirm");
		}
		else if (act == "reg_settings")
		{
			System.out.println("reg settings is now " + reg_settings.isSelected());
		}
	}
	
}