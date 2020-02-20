package src.gui.engine;

import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

// dialog class for handling buying roads/houses/cities/dev cards
public class BuyDialog extends JDialog implements ActionListener
{
	// cancel
	private JButton cancel;
	
	// confirm buy 
	private JButton confirm;
	
	// panels for buying
	private BuyPanel[] buy_panel;
	
	// how many resources we have at the moment
	// 0 - wood, 1 - brick, 2 - sheep, 3 - wheat, 4 - ore
	private int[] amount_res;
	
	// how many things have been bought
	// 0 - road, 1 - house, 2 - city, 3 - dev
	private int[] bought;
	
	public BuyDialog(JFrame parent, Color player_col, int[] amount_res)
	{
		super(parent, true);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setTitle("shop");
		
		this.amount_res = amount_res;
		
		JPanel main_panel = new JPanel(new GridBagLayout());
		
		cancel = new JButton("cancel");
		cancel.addActionListener(this);
		cancel.setActionCommand("cancel");
		
		confirm = new JButton("confirm");
		confirm.addActionListener(this);
		confirm.setActionCommand("confirm");
		
		Dimension dim = parent.getSize();
		
		buy_panel = new BuyPanel[4];
		
		bought = new int[4];
		
		// half of parent frame 
		Dimension size = new Dimension((int)dim.getWidth()/2, (int)dim.getHeight()/2);
		
		for (int i = 0; i < 4; i++)
		{
			buy_panel[i] = new BuyPanel(this, player_col, i, amount_res, bought);
		}
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = 1.0;
		c.weighty = 0.8;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		
		// so it always fills available space
		JPanel buffer = new JPanel(new GridLayout(1,4));
		buffer.setPreferredSize(size);
		for (int i = 0; i < 4; i++)
		{
			buffer.add(buy_panel[i]);
		}
		
		main_panel.add(buffer, c);
		
		c.gridwidth = 1;
		c.gridy = 1;
		c.weightx = 0.0;
		c.weighty = 0.2;
		c.fill = GridBagConstraints.VERTICAL;
		
		main_panel.add(cancel, c);
		
		c.gridx = 1;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.BOTH;
		
		DisplayResourcesPanel display_res = new DisplayResourcesPanel(amount_res);
		
		main_panel.add(display_res, c);
		
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 2;
		c.weightx = 0;
		c.fill = GridBagConstraints.VERTICAL;
		
		main_panel.add(confirm, c);
		
		add(main_panel);
		
		this.pack();
		
		// location relative to middle of frame
		Point loc = parent.getLocation();
		
		Dimension this_size = this.getSize();
		
		setLocation(loc.x + (int)(dim.getWidth() - this_size.getWidth())/2, loc.y + (int)(dim.getHeight() - this_size.getHeight())/2);
		
		this.setVisible(false);
	}
	
	public void update()
	{
		for (int i = 0; i < 4; i++)
		{
			buy_panel[i].update();
		}
		
		this.repaint();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String act = e.getActionCommand();
		
		if (act == "cancel")
		{
			for (int i = 0; i < 4; i++)
			{
				bought[i] = 0; // reset to 0
			}
		}
		
		dispose();
	}
	
	public int[] run()
	{
		this.setVisible(true);
		
		return bought;
	}
}