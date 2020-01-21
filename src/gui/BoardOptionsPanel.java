package src.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class BoardOptionsPanel extends JPanel
{
    private JButton up;
    private JButton down;
    private JButton left;
    private JButton right;
    private JButton confirm;

    public BoardOptionsPanel()
    {
        this.setLayout(new GridLayout(0,1));
        up = new JButton("up");
        down = new JButton("down");
        left = new JButton("left");
        right = new JButton("right");
        confirm = new JButton("confirm");

        up.setActionCommand("up");
        down.setActionCommand("down");
        left.setActionCommand("left");
        right.setActionCommand("right");
        confirm.setActionCommand("confirm");

        this.add(up);
        this.add(down);
        this.add(left);
        this.add(right);
        this.add(confirm);
    }

    public void setListeners(InputHandler input)
    {
        up.addActionListener(input);
        down.addActionListener(input);
        left.addActionListener(input);
        right.addActionListener(input);
        confirm.addActionListener(input);
    }
}