package com.codemonkeys.crmlite.view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.codemonkeys.crmlite.LanguageController;

public class EventsPanel extends JPanel {

	private static final long serialVersionUID = -2880571835313849930L;

	static String tabTitle = LanguageController.getInstance().w("eventsTabTitle");
	
	public EventsPanel() {
        JLabel filler = new JLabel("Events Tab Contents");
        filler.setHorizontalAlignment(JLabel.CENTER);
        setLayout(new GridLayout(1, 1));
        add(filler);
	}

}
