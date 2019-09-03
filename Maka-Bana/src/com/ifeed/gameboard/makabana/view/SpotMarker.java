package com.ifeed.gameboard.makabana.view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ifeed.gameboard.makabana.view.panels.GUI;

@SuppressWarnings("serial")
public class SpotMarker extends JPanel {
	
	static final String HUT = "<HTML><FONT FACE=\"Segoe UI Emoji\">\uD83C\uDFE0</FONT></HTML>";
	static final String TIKI = "<HTML><FONT FACE=\"Segoe UI Emoji\">\uD83D\uDC7A</FONT></HTML>";
	
	JLabel symbol;
	boolean withHut;

    @Override
    protected void paintComponent(Graphics g) {
    	g.setColor(Color.BLACK);
    	g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    	//g.setColor(getBackground());
		g.setColor(Color.DARK_GRAY.darker());
		g.fillOval(3, 3, g.getClipBounds().width-6, g.getClipBounds().height-6);
    }

	public SpotMarker() {
		symbol = new JLabel();
		symbol.setHorizontalAlignment(JLabel.CENTER);
		symbol.setVerticalAlignment(JLabel.CENTER);
		symbol.setVisible(false);
		add(symbol);
		
		setVisible(false);
	}

	public void setHut(Color color) {
		setBackground(color);
		
		symbol.setText(HUT);
		symbol.setFont(GUI.altFont1.deriveFont(getWidth() * 0.65f));
		symbol.setForeground(color);
		//symbol.setForeground(Color.BLACK);
		symbol.setVisible(true);
		setVisible(true);
		
		withHut = true;
	}
	
	public void setTiki(Color color) {
		setBackground(color);
		
		symbol.setText(TIKI);
		symbol.setFont(GUI.altFont1.deriveFont(getWidth() * 0.65f));
		symbol.setForeground(color);
		//symbol.setForeground(Color.BLACK);
		symbol.setVisible(true);
		setVisible(true);
	}
	
	public void unsetTiki() {
		if (withHut)
			symbol.setText(HUT);
		else
			setVisible(false);
	}
}
