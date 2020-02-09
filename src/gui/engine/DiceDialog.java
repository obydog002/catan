package src.gui.engine;

import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

public class DiceDialog extends JDialog implements ActionListener
{
	// ok button
	private JButton ok;
	
	// panel for drawing dice stuff
	private DicePanel dice_panel;
	
	public DiceDialog(JFrame parent, int d1, int d2)
	{
		super(parent, true);
		
		// close button do nothing
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JPanel main_panel = new JPanel(new GridBagLayout());
		
		ok = new JButton("okay");
		ok.addActionListener(this);
		ok.setEnabled(false); // initially false so we wait for the dice roll to complete
		
		Dimension dim = parent.getSize();
		
		// a third of parent frame 
		Dimension third_size = new Dimension((int)dim.getWidth()/3, (int)dim.getHeight()/3);
		dice_panel = new DicePanel(this, d1, d2, third_size);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		
		// so it always fills available space
		JPanel buffer = new JPanel(new BorderLayout());
		buffer.add(dice_panel, BorderLayout.CENTER);
		
		main_panel.add(buffer, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		main_panel.add(ok, c);
		
		add(main_panel);
		
		this.pack();
		
		// location relative to middle of frame
		Point loc = parent.getLocation();
		
		Dimension this_size = this.getSize();
		
		setLocation(loc.x + (int)(dim.getWidth() - this_size.getWidth())/2, loc.y + (int)(dim.getHeight() - this_size.getHeight())/2);
		
		this.setVisible(false);
	}
	
	// called by panel after its done drawing
	public void enable_ok()
	{
		ok.setEnabled(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		dispose();
	}
	
	public int run()
	{
		this.setVisible(true);
		
		return 1;
	}
}