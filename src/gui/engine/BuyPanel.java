package src.gui.engine;

import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

import src.gui.BoardPanel;

public class BuyPanel extends JPanel implements ActionListener
{
	// instance to refer back to
	private BuyDialog parent;
	
	// color to draw pieces in
	private Color player_col;
	
	// gives the name of the piece 
	private JLabel piece_name;
	
	private BuyPiecePanel buy_piece;
	
	// subtract from buy count
	private JButton less;
	
	// how many we have currently
	private JLabel count_label;
	
	// add to buy count (if possible)
	private JButton more;
	
	// to see if we can build here
	private int[] amount_res;
	
	private int[] bought;
	
	// what costs are for the different types
	private int[][] costs;
	
	// which type we are
	private int type;
	
	public BuyPanel(BuyDialog parent, Color player_col, int type, int[] amount_res, int[] bought)
	{
		this.parent = parent;
		
		this.setLayout(new GridBagLayout());
		
		this.player_col = player_col;
		
		this.amount_res = amount_res;
		
		this.bought = bought;
		
		this.type = type;
		
		String name;
		if (type == 0)
		{
			name = "Road";
		}
		else if (type == 1)
		{
			name = "Settlement";
		}
		else if (type == 2)
		{
			name = "City";
		}
		else
		{
			name = "Development Card";
		}
		
		piece_name = new JLabel(name);
		
		int scale_x = 15;
		int scale_y = 15;
		Image img = BoardPanel.MINUS_DECAL;
		Image scaled = img.getScaledInstance(scale_x, scale_y, Image.SCALE_SMOOTH);
		
		less = new JButton(new ImageIcon(scaled));
		less.setPreferredSize(new Dimension(scale_x, scale_y));
		less.setActionCommand("less");
		less.addActionListener(this);
		
		count_label = new JLabel("0");
		
		img = BoardPanel.PLUS_DECAL;
		scaled = img.getScaledInstance(scale_x, scale_y, Image.SCALE_SMOOTH);
		
		more = new JButton(new ImageIcon(scaled));
		more.setPreferredSize(new Dimension(scale_x, scale_y));
		more.setActionCommand("more");
		more.addActionListener(this);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		
		JPanel top = new JPanel(new BorderLayout());
		top.add(piece_name, BorderLayout.PAGE_START);
		top.add(less, BorderLayout.LINE_START);
		top.add(count_label, BorderLayout.CENTER);
		top.add(more, BorderLayout.LINE_END);
		
		this.add(top, c);
		
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		
		// could be potentially changed later if introduce variable costs for structures
		costs = new int[4][];
		
		costs[0] = new int[] {1,1,0,0,0}; // road
		costs[1] = new int[] {1,1,1,1,0}; // settlement
		costs[2] = new int[] {0,0,0,2,3}; // city
		costs[3] = new int[] {0,0,1,1,1}; // dev
		
		buy_piece = new BuyPiecePanel(parent, type, player_col, costs[type], amount_res, bought);
		
		JPanel buffer = new JPanel(new BorderLayout());
		buffer.add(buy_piece, BorderLayout.CENTER);
		
		this.add(buffer, c);
	}
	
	public boolean compare_costs()
	{
		for (int i = 0; i < 5; i++)
		{
			if (amount_res[i] < costs[type][i])
				return false;
		}
		
		return true;
	}
	// checks if we can buy, if we can take back,
	// what the label should display
	public void update()
	{
		count_label.setText(bought[type] + "");
		
		if (bought[type] < 1) // cant choose less
		{
			less.setEnabled(false);
		}
		else
		{
			less.setEnabled(true);
		}
		
		if (compare_costs())
		{
			more.setEnabled(true);
		}
		else
		{
			more.setEnabled(false);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String act = e.getActionCommand();
		
		if (act == "less")
		{
			bought[type]--;
			
			// add back to amount array
			for (int i = 0; i < 5; i++)
			{
				amount_res[i] += costs[type][i];
			}
			
			parent.update();
		}
		else if (act == "more")
		{
			bought[type]++;
			
			// remove from amount array
			for (int i = 0; i < 5; i++)
			{
				amount_res[i] -= costs[type][i];
			}
			
			parent.update();
		}
	}
}