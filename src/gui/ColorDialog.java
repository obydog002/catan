package src.gui;

import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

// a class that handlers color choosing in player select
// dialog formed when button is pressed
public class ColorDialog extends JDialog implements ActionListener
{
	private JTextField color_text;
	
	private JButton validate_color;
	
	private ColorBox color_display;
	
	private JButton confirm;
	
	// raw_hex for valid values so we can fall back should the validation fail of entered color
	private String raw_hex;
	
	public ColorDialog(JFrame parent, int color)
	{
		super(parent, true);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JPanel main_panel = new JPanel(new GridLayout(1,2));
		
		Dimension dim = parent.getSize();
		
		Point loc = parent.getLocation();
		
		JPanel left = new JPanel();
		JPanel right = new JPanel(new GridLayout(0,1));
		
		raw_hex = String.format("0x%08x", color);
		raw_hex = hexify(raw_hex);
		
		color_text = new JTextField(raw_hex);
		
		validate_color = new JButton("validate color");
		validate_color.setActionCommand("validate");
		validate_color.addActionListener(this);
		
		color_display = new ColorBox(color);
		
		confirm = new JButton("confirm");
		confirm.setActionCommand("confirm");
		confirm.addActionListener(this);
		
		right.add(color_text);
		right.add(validate_color);
		right.add(color_display);
		right.add(confirm);
		
		main_panel.add(left);
		main_panel.add(right);
		
		add(main_panel);
		pack();
		setVisible(false);
		
		Dimension this_size = getSize();
		
		setLocation(loc.x + (int)(dim.getWidth() - this_size.getWidth())/2, loc.y + (int)(dim.getHeight() - this_size.getHeight())/2);
	}
	
	public int run()
	{
		this.setVisible(true);
		
		return color_display.get_hex();
	}
	
	// my own method because Integer.decode is not good for this it seems
	// throws an Exception if it can't be parsed
	public int parse_hex(String hex) throws Exception
	{
		// 2 bytes for the 0x; 8 for the hex code
		if (hex.length() != 10)
		{
			throw new NumberFormatException();
		}
		
		char a[] = hex.toCharArray();
		
		if (a[0] != '0' || (a[1] != 'x' && a[1] != 'X'))
		{
			throw new NumberFormatException();
		}
		
		int number = 0;
		for (int i = 2; i < 10; i++)
		{
			int value = 0;
			
			if (a[i] >= 48 && a[i] <= 57) // 0 - 9
			{
				value = a[i] - 48;
			}
			else if (a[i] >= 65 && a[i] <= 70)
			{
				value = a[i] - 65 + 10;
			}
			else if (a[i] >= 97 && a[i] <= 102)
			{
				value = a[i] - 97 + 10;
			}
			else
			{
				throw new NumberFormatException();
			}
			
			number += value;
			
			// shift number to its respective place
			if (i != 9)
				number = number << 4;
		}
		
		return number;
	}
	
	// this just converts the String into a 'prettier' hex string
	// capitilizes everything except for the 0x
	public String hexify(String str)
	{
		String res = "0x";
		res += str.substring(2).toUpperCase();
		
		return res;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String act = e.getActionCommand();
		
		if (act == "validate")
		{
			String code = color_text.getText();
			int hex = 0;
			try
			{
				hex = parse_hex(code);
				color_display.set_color(hex);
				raw_hex = hexify(code);
			}
			catch (Exception exc)
			{
				// do nothing
			}
			
			color_text.setText(raw_hex);
		}
		else if (act == "confirm")
		{
			dispose();
		}
	}
}