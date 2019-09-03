package com.codemonkeys.crmlite.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import com.codemonkeys.crmlite.Config;
import com.codemonkeys.crmlite.LanguageController;

public class MainGUI extends JFrame {

	private static final long serialVersionUID = -183177767421391640L;

	public MainGUI() {
		super(Config.title + " v." + Config.majorVersion + "." + Config.minorVersion);
		
		//setSize(800,600);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		/*
		JTabbedPane tabbedPane = new JTabbedPane();
		//ImageIcon icon = createImageIcon("images/middle.gif");

		tabbedPane.addTab(CustomersPanel.tabTitle, new CustomersPanel());
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab(EventsPanel.tabTitle, new EventsPanel());
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		JComponent panel3 = makeTextPanel("Panel #3");
		tabbedPane.addTab("Tab 3", panel3);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		
		add(tabbedPane, BorderLayout.CENTER);
		*/
		add(new CustomerDashboard(1), BorderLayout.CENTER);

		setVisible(true);
	}
	
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

}
