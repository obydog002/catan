package src.gui.engine;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.*;

import java.awt.*;

import java.util.Random;

// Panel to draw the dice themselves
// performs random drawing of dice before displaying result
public class DicePanel extends JPanel implements ActionListener
{
	// so we can pass control pack to the button
	private DiceDialog parent;
	
	// dice rolls to show at end
	private int d1, d2;
	
	// variables to track random dice and proper rolls at the end
	private int r1, r2;
	
	// how long between ticks (ms)
	private int tick = 100;
	
	// how many ticks before we stop
	private int tick_length = 9;
	
	// count of ticks
	private int tick_count = 0;
	
	// so we draw what the result is
	private boolean done = false;
	
	// random
	private Random rng;
	
	// timer
	private Timer timer;
	
	public DicePanel(DiceDialog parent, int d1, int d2, Dimension size)
	{
		this.setPreferredSize(size);
		
		this.parent = parent;
		this.d1 = d1;
		this.d2 = d2;
		
		timer = new Timer(tick, this);
		
		rng = new Random();
		
		r1 = roll();
		r2 = roll();
		
		timer.start();
	}
	
	public int roll()
	{
		return rng.nextInt(6) + 1;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Dimension dim = this.getSize();
		
		int width = (int)dim.getWidth();
		int height = (int)dim.getHeight();
		
		int half_width = width/2;
		
		Graphics2D g2D = (Graphics2D)g;
		
		int dis_half_width = 3*(half_width/2)/4;
		int dis_half_height = 3*(height/2)/4;
		
		draw_dice_roll(g2D, half_width/2, height/2, dis_half_width, dis_half_height, r1, Color.YELLOW, Color.RED);
		draw_dice_roll(g2D, 3*half_width/2, height/2, dis_half_width, dis_half_height, r2, Color.RED, Color.YELLOW);
		
		if (done)
		{
			g.setColor(Color.BLACK);
			int font = height/8;
			g.setFont(new Font("Comic Sans MS", Font.BOLD, font));
			String numb = (r1 + r2) + "";
			g2D.drawString((r1 + r2) + "", half_width - numb.length()*font/2 + 10, 7*height/8 + font - 10);
		}
	}
	
	// draws a dice roll onto the screen
	// g - graphics
	// x0,y0 - mid point of the roll
	// width - half-width of rectangle
	// height - half-height of rectangle
	// number - number from 1-6 (does nothing if its bigger), amount of dots to draw
	// back_col - background color of rectangle behind dots
	// dot_col - color of the dots
	public void draw_dice_roll(Graphics2D g, int x0, int y0, int width, int height, int number, Color back_col, Color dot_col)
	{
		g.setColor(back_col);
		
		g.fillRect(x0 - width, y0 - height, 2*width, 2*height);
		
		g.setColor(dot_col);
		
		// return early if invalid number
		if (number < 1 || number > 6)
			return;
		
		int r = width/5;
		if (height < width)
			r = height/5;
	
		if (number % 2 == 1) // odd draw middle dot
		{
			g.fillOval(x0 - r, y0 - r, 2*r, 2*r);
		}
		
		if (number >= 2)
		{
			g.fillOval(x0 - width/2 - r, y0 - height/2 - r, 2*r, 2*r);
			g.fillOval(x0 + width/2 - r, y0 + height/2 - r, 2*r, 2*r);
		}
		
		if (number >= 4)
		{
			g.fillOval(x0 - width/2 - r, y0 + height/2 - r, 2*r, 2*r);
			g.fillOval(x0 + width/2 - r, y0 - height/2 - r, 2*r, 2*r);
		}
		
		if (number == 6)
		{
			g.fillOval(x0 - width/2 - r, y0 - r, 2*r, 2*r);
			g.fillOval(x0 + width/2 - r, y0 - r, 2*r, 2*r);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (tick_count < tick_length)
		{
			tick_count++;
			
			// if its the last one make it so the random rolls cant be the same as our actual rolls
			// just to improve clarity
			if (tick_count == tick_length - 1)
			{
				r1 = rng.nextInt(5) + 1;
				if (r1 >= d1)
					r1++;
				
				r2 = rng.nextInt(5) + 1;
				if (r2 >= d2)
					r2++;
			}
			else
			{
				r1 = roll();
				r2 = roll();
			}
		}
		else
		{
			r1 = d1;
			r2 = d2;
			done = true;
			timer.stop();
			parent.enable_ok();
		}
		
		repaint();
	}
}