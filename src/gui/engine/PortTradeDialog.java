package src.gui.engine;

import javax.swing.*;

import java.awt.*;

// dialog windows for trading with ports
// or just general 4:1
public class PortTradeDialog extends JDialog
{
	// what ports are available for trading
	// 0 - 2:1 wood, 1 - 2:1 brick, 2 - 2:1 sheep, 3 - 2:1 wheat, 4 - 2:1 ore, 5 - 3:1 any
	private boolean available[];
	
	private JButton cancel;
	
	private JButton confirm;
	
	// resources we have
	private int[] amount_res;
	
	public PortTradeDialog(JFrame parent, boolean[] available, int[] amount_res)
	{
		super(parent, true);
		
		this.available = available;
		
		this.amount_res = amount_res;
		
		int res_av = 1;
		for (int i = 0; i < 5; i++)
		{
			if (available[i])
				res_av++;
		}
		
		Dimension dim = parent.getSize();
		Dimension size;
		
		if (res_av >= 3) // scale it half of parent size
		{
			size = new Dimension((int)(dim.getWidth()/2), (int)(dim.getHeight()/2));
		}
		else // otherwise just go in thirds 
		{
			size = new Dimension((int)(res_av*dim.getWidth()/6), (int)(dim.getHeight()/2));
		}
		
		JPanel main_panel = new JPanel(new GridBagLayout());
		
		JPanel top = new JPanel(new GridLayout(1,0));
		
		for (int i = 0; i < 5; i++)
		{
			if (available[i])
			{
				top.add(new PortTradePanel(i));
			}
		}
		
		if (available[5]) // replace with 3:1
		{
			top.add(new PortTradePanel(5));
		}
		else // else standard 4:1
		{
			top.add(new PortTradePanel(6));
		}
		
		top.setPreferredSize(size);
		
		cancel = new JButton("cancel");
		cancel.setActionCommand("cancel");
		// actionlistener
		
		confirm = new JButton("confirm");
		confirm.setActionCommand("confirm");
		// actionlistener
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = 1.0;
		c.weighty = 0.8;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		
		main_panel.add(top, c);
		
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
		
		this.add(main_panel);
		
		this.pack();
		
		this.setVisible(false);
	}
	
	public int run()
	{
		this.setVisible(true);
		
		return -1;
	}
}