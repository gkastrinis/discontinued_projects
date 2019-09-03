package com.ifeed.gameboard.makabana.view.panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import com.ifeed.gameboard.makabana.Config;
import com.ifeed.gameboard.makabana.controller.TextFieldHint;
import com.ifeed.gameboard.makabana.view.GameButton;
import com.ifeed.gameboard.makabana.view.GameTextField;
import com.ifeed.gameboard.makabana.view.PanelWithBackground;

@SuppressWarnings("serial")
public class Panel_01_Intro extends PanelWithBackground {

	static final String hint = "Your name?";
	
	GameTextField fld;

	public Panel_01_Intro(JFrame window) {
		super(new GridLayout(2, 1, 0, GUI.PADDING), Config.getInstance().getImage("playerBg"));
		setBorder(BorderFactory.createEmptyBorder(GUI.PADDING, GUI.PADDING, GUI.PADDING, GUI.PADDING));

		double ratio = backgroundIMG.getWidth(this) / (double) backgroundIMG.getHeight(this);
		Dimension windowDimension = new Dimension(
				backgroundIMG.getWidth(this) + 100,
				backgroundIMG.getHeight(this) + (int) (100 * ratio));
		
		window.setExtendedState(JFrame.NORMAL);
		window.setMinimumSize(windowDimension);
		
		// Get username from user config file (if any)
		String fldText = (String) Config.getInstance().get("username");
		if (fldText == null) fldText = hint;
		fld = new GameTextField(fldText);
		fld.addFocusListener(new TextFieldHint(hint));
		add(fld);
		
		GameButton btn = new GameButton("Start");
		btn.addActionListener(new BtnListener());
		add(btn);
	}
	
	
	class BtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String name = fld.getText();
			if (!name.trim().isEmpty() && !name.equals(hint)) {
				// Store username in user config file
				Config.getInstance().putUserConf("username", name);

				JFrame window = (JFrame) SwingUtilities.getWindowAncestor(Panel_01_Intro.this);
				window.getContentPane().removeAll();
				window.getContentPane().add(new Panel_02_PreGame(name));
				window.setTitle(window.getTitle() +" - "+ name);

				window.setExtendedState(JFrame.MAXIMIZED_BOTH);
				window.setMinimumSize(new Dimension(1024, 768));
				window.setResizable(true);
				window.revalidate();
			}
		}
	}
}
