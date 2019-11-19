package game.gui;

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
		frame.setSize(500,500);
		frame.setVisible(true);
		
	}
}