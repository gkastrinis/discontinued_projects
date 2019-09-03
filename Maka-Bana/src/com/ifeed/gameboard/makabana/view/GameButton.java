package com.ifeed.gameboard.makabana.view;

import java.awt.Graphics;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class GameButton extends JButton {

	static final ColorSet DEFAULT_CS = new ColorSet();
	static final ColorSet TAPPED_CS = new TappedCS();
	static final ColorSet NONTAPPED_CS = new NonTappedCS();
	static final ColorSet CANCEL_CS = new CancelCS();
	static final ColorSet DISABLED_CS = new DisabledCS();

	ColorSet currentCS;
	
    public GameButton() {
        this(null);
    }

    public GameButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
        setBorder(null);
        currentCS = DEFAULT_CS;
        setForeground(currentCS.FG_COLOR);
        setBackground(currentCS.BG_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
    	if (!isEnabled()) {
    		if (currentCS == TAPPED_CS)
    			g.setColor(DISABLED_CS.BG_COLOR.brighter());
    		else
    			g.setColor(DISABLED_CS.BG_COLOR);
        } else if (getModel().isPressed()) {
            g.setColor(currentCS.ACTIVE_BG_COLOR);
        } else if (getModel().isRollover()) {
            g.setColor(currentCS.HOVER_BG_COLOR);
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    @Override
    public void setContentAreaFilled(boolean b) {}

    public void tap() {
    	currentCS = TAPPED_CS;
    	setForeground(currentCS.FG_COLOR);
    	setBackground(currentCS.BG_COLOR);
    }
    
    public void untap() {
    	currentCS = NONTAPPED_CS;
    	setForeground(currentCS.FG_COLOR);
    	setBackground(currentCS.BG_COLOR);
    }
    
    public void active() {
    	currentCS = CANCEL_CS;
    	setForeground(currentCS.FG_COLOR);
    	setBackground(currentCS.BG_COLOR);
    }
    
    public void inactive() {
    	currentCS = DEFAULT_CS;
    	setForeground(currentCS.FG_COLOR);
    	setBackground(currentCS.BG_COLOR);
    }
}
