package src.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;

// side panel to board that allows dice roll commands, buy commands, trade, etc...
public class GameControlPanel extends JPanel implements ActionListener
{
	// instance variable of BoardPanel to link to
	private BoardPanel board_panel;
	
	
	// whoever's turn it is at the moment
	private JLabel current_turn;
	
	// roll dice button
	private JButton roll_dice;
	
	// initiate trade button
	private JButton trade;
	
	// buy item button
	private JButton buy;
	
	// end turn button;
	private JButton end_turn;
	
	public GameControlPanel(BoardPanel board_panel)
	{
		this.board_panel = board_panel;
			
		this.setLayout(new GridLayout(0,1));
		
		current_turn = new JLabel("player 1");
		
		roll_dice = new JButton("roll dice");
		roll_dice.setActionCommand("roll");
		roll_dice.addActionListener(this);
		
		trade = new JButton("trade");
		trade.setActionCommand("trade");
		trade.addActionListener(this);
		
		buy = new JButton("buy");
		buy.setActionCommand("buy");
		buy.addActionListener(this);
		
		end_turn = new JButton("end turn");
		end_turn.setActionCommand("end");
		end_turn.addActionListener(this);
		
		this.add(current_turn);
		this.add(roll_dice);
		this.add(trade);
		this.add(buy);
		this.add(end_turn);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String act = e.getActionCommand();
		
		if (act == "roll")
		{
			System.out.println("roll");
		}
		else if (act == "trade")
		{
			System.out.println("trade");
		}
		else if (act == "buy")
		{
			System.out.println("buy");
		}
		else if (act == "end")
		{
			System.out.println("end");
		}
	}
}