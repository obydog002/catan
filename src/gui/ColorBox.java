package src.gui;

import java.awt.*;
import javax.swing.*;

// fill the label with a color
public class ColorBox extends JLabel
{
	private Color color;
	
	private int hex;
	
	public ColorBox(int hex)
	{
		this.hex = hex;
		color = new Color(hex);
	}
	
	public int get_hex()
	{
		return hex;
	}
	
	public void set_color(int hex)
	{
		this.hex = hex;
		color = new Color(hex);
		
		this.repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
			
		g.setColor(color);
			
		Dimension dim = this.getSize();
			
		g.fillRect(0, 0, (int)dim.getWidth(), (int)dim.getHeight());
	}
}