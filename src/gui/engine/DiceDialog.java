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
	
	public DiceDialog(JFrame parent)
	{
		super(parent, true);
		
		JPanel main_panel = new JPanel(new GridLayout(0,1));
		
		ok = new JButton("okay");
		ok.addActionListener(this);
		
		dice_panel = new DicePanel();
		
		main_panel.add(dice_panel);
		main_panel.add(ok);
		
		add(main_panel, BorderLayout.CENTER);
		
		this.pack();
		
		// location relative to middle of frame
		Point loc = parent.getLocation();
		Dimension dim = parent.getSize();
		
		Dimension this_size = this.getSize();
		
		setLocation(loc.x + (int)(dim.getWidth() - this_size.getWidth())/2, loc.y + (int)(dim.getHeight() - this_size.getHeight())/2);
		
		this.setVisible(false);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		dispose();
	}
	
	public int run()
	{
		this.setVisible(true);
		
		return 2;
	}
}