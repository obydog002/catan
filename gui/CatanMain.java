package catan.gui;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

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
		
		BoardPanel board = new BoardPanel();
		
		frame.add(board);
		
		frame.pack();
	}
}