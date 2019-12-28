package catan.gui;

import java.awt.*;
import javax.swing.*;

// fill the label with a color
public class ColorBox extends JLabel
{
	private Color color;
		
	public ColorBox(int hex)
	{
		color = new Color(hex);
	}
		
	public ColorBox(Color color)
	{
		this.color = color;
	}
	
	public void set_color(int hex)
	{
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