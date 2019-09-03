package com.ifeed.gameboard.makabana.view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelWithBackground extends JPanel {

	protected Image backgroundIMG;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	    if (backgroundIMG != null)
			g.drawImage(backgroundIMG, 0, 0, getSize().width, getSize().height, null);
	}
	
	public PanelWithBackground(LayoutManager layout) {
		this(layout, null);
	}
	
	public PanelWithBackground(LayoutManager layout, Image backgroundIMG) {
		super(layout);
		this.backgroundIMG = backgroundIMG;
	}
	
	public void setBackground(Image img) {
		backgroundIMG = img;
	}
}
