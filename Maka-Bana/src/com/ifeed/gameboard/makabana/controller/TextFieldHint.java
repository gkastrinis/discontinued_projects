package com.ifeed.gameboard.makabana.controller;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class TextFieldHint implements FocusListener {
	String hint;
	
	public TextFieldHint(String hint) {
		this.hint = hint;
	}

	@Override
	public void focusGained(FocusEvent e) {
		JTextField field = (JTextField) e.getSource();
    	if (field.getText().equals(hint))
    		field.setText("");
	}

	@Override
	public void focusLost(FocusEvent e) {
		JTextField field = (JTextField) e.getSource();
    	if (field.getText().isEmpty())
    		field.setText(hint);
	}
}