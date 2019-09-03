package com.codemonkeys.crmlite.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.codemonkeys.crmlite.LanguageController;

public class CustomersPanel extends JPanel {

	private static final long serialVersionUID = -2624864943737378522L;
	
	static String tabTitle = LanguageController.getInstance().w("customersTabTitle");
	
	public CustomersPanel() {
		setBackground(Color.WHITE);
		
		
        JLabel filler = new JLabel("Customers Tab Contents");
        filler.setHorizontalAlignment(JLabel.CENTER);
        setLayout(new GridLayout(1, 1));
        add(filler);
	}

}
