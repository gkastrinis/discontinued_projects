package com.ifeed.gameboard.makabana.view;

import java.awt.Color;


class ColorSet {
	Color FG_COLOR;
	Color BG_COLOR;
	Color HOVER_BG_COLOR;
	Color ACTIVE_BG_COLOR;
	
	ColorSet() {
    	FG_COLOR = new Color(0, 135, 200).brighter();
    	BG_COLOR = new Color(3, 59, 90);
    	HOVER_BG_COLOR = BG_COLOR.brighter();
    	ACTIVE_BG_COLOR = HOVER_BG_COLOR.brighter();
	}
}

class TappedCS extends ColorSet {
	TappedCS() {
		super();
		Color tmp = FG_COLOR;
		FG_COLOR = BG_COLOR;
		BG_COLOR = tmp;
    	HOVER_BG_COLOR = BG_COLOR.darker();
    	ACTIVE_BG_COLOR = HOVER_BG_COLOR.darker();
	}
}

class NonTappedCS extends ColorSet {
	NonTappedCS() {
		super();
		Color tmp = FG_COLOR;
    	FG_COLOR = new Color(173, 190, 200);
    	BG_COLOR = new Color(93, 131, 140);
    	HOVER_BG_COLOR = BG_COLOR.brighter();
    	ACTIVE_BG_COLOR = tmp;
	}
}

class CancelCS extends ColorSet {
	CancelCS() {
		super();
    	FG_COLOR = BG_COLOR;
    	BG_COLOR = new Color(230, 54, 0);
    	HOVER_BG_COLOR = BG_COLOR.brighter();
    	ACTIVE_BG_COLOR = HOVER_BG_COLOR.brighter();
	}
}

class DisabledCS extends ColorSet {
	DisabledCS() {
		ColorSet tmp = new NonTappedCS();
    	FG_COLOR = tmp.FG_COLOR;
    	BG_COLOR = tmp.BG_COLOR;
    	HOVER_BG_COLOR = tmp.BG_COLOR;
    	ACTIVE_BG_COLOR = tmp.BG_COLOR;
	}
}

class MessageCS extends ColorSet {
	MessageCS() {
		super();
		FG_COLOR = BG_COLOR;
		BG_COLOR = Color.ORANGE;
	}
}

class SuccessCS extends ColorSet {
	SuccessCS() {
		super();
		FG_COLOR = BG_COLOR;
		BG_COLOR = new Color(8, 235, 0);
	}
}

class ErrorCS extends ColorSet {
	ErrorCS() {
		super();
		FG_COLOR = BG_COLOR;
		BG_COLOR = new Color(230, 54, 0).brighter();
	}
}
