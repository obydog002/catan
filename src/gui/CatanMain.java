package src.gui;

import java.awt.*;
import javax.swing.*;

public class CatanMain
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				create_and_show_GUI();
			}
		});
	}
	
	public static void create_and_show_GUI()
	{
		JFrame frame = new JFrame("Options");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		long seed = 1;
		
		OptionsPanel options = new OptionsPanel(frame, seed);
		frame.add(options);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}