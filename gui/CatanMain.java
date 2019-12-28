package catan.gui;

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
				createAndShowGUI();
			}
		});
	}
	
	public static void createAndShowGUI()
	{
		JFrame frame = new JFrame("catan");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		OptionsPanel options = new OptionsPanel();
		
		frame.add(options);
		
		/*BoardPanel board = new BoardPanel();
		
		frame.add(board);*/
		
		frame.pack();
	}
}