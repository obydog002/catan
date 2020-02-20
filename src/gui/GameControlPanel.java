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
	
	// trade with ports button
	private JButton port_trade;
	
	// buy item button
	private JButton buy;
	
	// play dev card button
	private JButton play_dev;
	
	// end turn button;
	private JButton end_turn;
	
	// for refunding pieces if there are some present
	private JButton refund;
	
	// rotate board button
	private JButton rotate;
	
	public GameControlPanel(BoardPanel board_panel)
	{
		this.board_panel = board_panel;
			
		this.setLayout(new GridLayout(0,1));
		
		current_turn = new JLabel();
		
		roll_dice = new JButton("roll dice");
		roll_dice.setActionCommand("roll");
		roll_dice.addActionListener(this);
		
		trade = new JButton("trade");
		trade.setActionCommand("trade");
		trade.addActionListener(this);
		
		port_trade = new JButton("trade with port");
		port_trade.setActionCommand("port");
		port_trade.addActionListener(this);
		
		buy = new JButton("buy");
		buy.setActionCommand("buy");
		buy.addActionListener(this);
		
		play_dev = new JButton("play development card");
		play_dev.setActionCommand("dev");
		play_dev.addActionListener(this);
		
		end_turn = new JButton("end turn");
		end_turn.setActionCommand("end");
		end_turn.addActionListener(this);
		
		refund = new JButton("refund pieces");
		refund.setActionCommand("refund");
		refund.addActionListener(this);
		refund.setEnabled(false); // initially cant refund first pieces
		
		JPanel padded_refund = new JPanel(new GridLayout(1,0));
		padded_refund.add(new JLabel());
		padded_refund.add(refund);
		
		rotate = new JButton("rotate board");
		rotate.setActionCommand("rotate");
		rotate.addActionListener(this);
		
		this.add(current_turn);
		this.add(roll_dice);
		this.add(buy);
		this.add(padded_refund);
		this.add(trade);
		this.add(port_trade);
		this.add(play_dev);
		
		this.add(new JLabel()); // padding
		
		this.add(end_turn);
		
		this.add(new JLabel()); // padding
		this.add(new JLabel());
		
		this.add(rotate);
	}
	
	// sets game control panel to roll_dice stage (includes initial starting order, as well as pre turn phase)
	public void roll_dice()
	{
		roll_dice.setEnabled(true);
		trade.setEnabled(false);
		port_trade.setEnabled(false);
		buy.setEnabled(false);
		play_dev.setEnabled(false);
		end_turn.setEnabled(false);
	}
	
	// sets game control panel to initial placement stage
	public void initial_placement()
	{
		roll_dice.setEnabled(false);
		trade.setEnabled(false);
		port_trade.setEnabled(false);
		buy.setEnabled(false);
		play_dev.setEnabled(false);
		end_turn.setEnabled(true);
	}
	
	// sets game control panel to turn stage
	public void turn()
	{
		roll_dice.setEnabled(false);
		trade.setEnabled(true);
		port_trade.setEnabled(true);
		buy.setEnabled(true);
		play_dev.setEnabled(true);
		end_turn.setEnabled(true);
	}
	
	public void refund_enabled(boolean b)
	{
		refund.setEnabled(b);
	}
	
	// sets the current turn to reflect the name of whoever is now
	public void set_player_turn(String name)
	{
		current_turn.setText(name + " turn");
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String act = e.getActionCommand();
		
		if (act == "roll")
		{
			board_panel.process_roll();
		}
		else if (act == "trade")
		{
			board_panel.process_trade();
		}
		else if (act == "port")
		{
			board_panel.process_trade_port();
		}
		else if (act == "buy")
		{
			board_panel.process_buy();
		}
		else if (act == "dev")
		{
			board_panel.process_dev();
		}
		else if (act == "end")
		{
			board_panel.process_end_turn();
		}
		else if (act == "refund")
		{
			board_panel.process_refund();
		}
		else if (act == "rotate")
		{
			board_panel.toggle_rotate();
		}
	}
}