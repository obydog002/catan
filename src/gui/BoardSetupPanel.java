package src.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

// a panel for setting up the board in the beginning
public class BoardSetupPanel extends JPanel
{
    private JButton confirm;

    public BoardSetupPanel()
    {
        this.setLayout(new GridLayout(0,1));
		
		confirm = new JButton("confirm");
        confirm.setActionCommand("confirm");

        this.add(confirm);
    }

}