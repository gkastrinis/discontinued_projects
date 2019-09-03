package com.codemonkeys.crmlite;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.codemonkeys.crmlite.model.Initiator;
import com.codemonkeys.crmlite.view.MainGUI;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.init();
		(new Initiator()).init();
		
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				new MainGUI();
			}
		});
	}

}
