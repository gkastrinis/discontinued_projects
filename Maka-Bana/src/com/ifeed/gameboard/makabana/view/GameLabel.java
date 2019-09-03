package com.ifeed.gameboard.makabana.view;

import javax.swing.JLabel;


@SuppressWarnings("serial")
public class GameLabel extends JLabel {

	static final ColorSet DEFAULT_CS = new ColorSet();
	static final ColorSet TAPPED_CS = new TappedCS();
	static final ColorSet CANCEL_CS = new CancelCS();
	static final ColorSet DISABLED_CS = new DisabledCS();
	
    public GameLabel() {
        this(null);
    }

    public GameLabel(String text) {
        super(text);
        setHorizontalAlignment(JLabel.CENTER);
        toDefault();
    }
    
    public void toDefault() {
    	setForeground(DEFAULT_CS.BG_COLOR);
    }
    
    public void toIdle() {
    	setForeground(DISABLED_CS.FG_COLOR);
    }
    
    public void toStatus() {
    	setForeground(TAPPED_CS.HOVER_BG_COLOR);
    }
    
    public void toError() {
    	setForeground(CANCEL_CS.BG_COLOR);
    }
}
