package src.gui;

import java.awt.*;
import javax.swing.*;

import java.util.Random;

public class CatanMain
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				create_and_show_GUI(args);
			}
		});
	}
	
	public static void create_and_show_GUI(String[] args)
	{
		JFrame frame = new JFrame("Options");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Random rng = new Random();
		long seed = rng.nextLong();
		
		if (args.length >= 2) // try to parse the seed
		{
			seed = Long.parseLong(args[1]);
		}
		
		OptionsPanel options = new OptionsPanel(frame, seed);
		frame.add(options);
		
		// set the image icon
		ImageIcon img = new ImageIcon("res/icon/sheep_icon.png");
		frame.setIconImage(img.getImage());
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}