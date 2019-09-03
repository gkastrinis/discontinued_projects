package com.ifeed.gameboard.makabana.view;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GameTextField extends JTextField {
	
	static final ColorSet DEFAULT_CS = new ColorSet();

    public GameTextField() {
        this(null);
    }

    public GameTextField(String text) {
        super(text);
        setHorizontalAlignment(JTextField.CENTER);
        setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(0,5,0,5)));
        toDefault();
    }
    
    public void toOnlyText() {
		setOpaque(false);
		setBorder(null);
    }

    public void toDefault() {
    	setForeground(DEFAULT_CS.BG_COLOR);
    }
}
