package com.ifeed.gameboard.makabana.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.ColorUIResource;

import com.ifeed.gameboard.makabana.Config;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	
	public static final int PADDING = 56;
	public static final Font altFont1 = new Font("Jokerman", Font.PLAIN, 24);
	public static final Font altFont2 = new Font("Jokerman", Font.PLAIN, 26);

	public GUI() {
		super(Config.getInstance().get("title"));

		Font defaultFont = new Font("Jokerman", Font.PLAIN, 48);
		UIManager.put("TextField.font", defaultFont);
		UIManager.put("Label.font", defaultFont);
		UIManager.put("Button.font", defaultFont);
		UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		SwingUtilities.updateComponentTreeUI(this);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		Image icon = Config.getInstance().getImage("icon");
		if (icon != null) setIconImage(icon);

		add(new Panel_01_Intro(this), BorderLayout.CENTER);

		setVisible(true);

		// Hack to keep focus on window but not on internal component
		setAlwaysOnTop(true);
		requestFocus();
		setAlwaysOnTop(false);
	}
}