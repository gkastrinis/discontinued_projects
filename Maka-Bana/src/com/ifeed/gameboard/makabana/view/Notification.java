package com.ifeed.gameboard.makabana.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import com.ifeed.gameboard.makabana.view.panels.GUI;

@SuppressWarnings("serial")
public class Notification extends JPanel {
	
	public enum Level { MESSAGE, SUCCESS, ERROR };
	public enum Position { TOP, CENTER, BOTTOM };

	public Notification(String msg, Level level, Position position) {
		super(new GridBagLayout());
		setOpaque(false);
		
		ColorSet cs = null;
		if (level == Level.MESSAGE)
			cs = new MessageCS();
		else if (level == Level.SUCCESS)
			cs = new SuccessCS();
		else
			cs = new ErrorCS();

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(cs.BG_COLOR);
		panel.setBorder(BorderFactory.createEmptyBorder(GUI.PADDING, GUI.PADDING, GUI.PADDING, GUI.PADDING));
		
		GameLabel label = new GameLabel(msg);
		label.setForeground(cs.FG_COLOR);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		
		panel.add(label, c);
		
		if (position == Position.TOP) {
			c.gridy = 0;
			c.weighty = 0;
			add(panel, c);
			
			c.gridy = 1;
			c.weighty = 1;
			add(Box.createGlue(), c);
		} else if (position == Position.CENTER) {
			add(panel, c);
		} else if (position == Position.BOTTOM) {
			c.gridy = 0;
			c.weighty = 1;
			add(Box.createGlue(), c);
			
			c.gridy = 1;
			c.weighty = 0;
			add(panel, c);
		}	
	}
}
