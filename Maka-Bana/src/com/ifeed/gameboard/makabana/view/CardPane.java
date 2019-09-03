package com.ifeed.gameboard.makabana.view;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import javax.swing.GrayFilter;

@SuppressWarnings("serial")
public class CardPane extends ScalablePane {

	public final String name;

	Image original;
	boolean locked;
	boolean enabled;
	boolean selected;
	ImageFilter disabledFilter;
	ImageFilter unselectedFilter;
	
	public CardPane(String name, Image master, boolean toFit) {
		super(master, toFit);
		original = master;
		this.name = name;
		disabledFilter = new GrayFilter(false, 75);
		unselectedFilter = new GrayFilter(true, 0);
		reset();
	}
	
	public void reset() {
		locked = false;
		enabled = true;
		selected = false;
		unselectedFilter();
		revalidate();
	}
	
	public void lock() {
		locked = true;
	}
	
	public void unlock() {
		locked = false;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public boolean tap() {
		if (locked || !enabled) return false;

		if (selected) {
			doUnselect();
		} else {
			doSelect();
		}
		return true;
	}
	
	public void doSelect() {
		noFilter();
		selected = true;
	}
	
	public void doUnselect() {
		unselectedFilter();
		selected = false;
	}
	
	public void doEnable() {
		if (selected) doSelect();
		else doUnselect();
		enabled = true;
	}
	
	public void doDisable() {
		disabledFilter();
		enabled = false;
	}

    
    void disabledFilter() {
    	ImageProducer producer = new FilteredImageSource(original.getSource(), disabledFilter);
    	super.setImage(Toolkit.getDefaultToolkit().createImage(producer));
    	revalidate();
    	repaint();
    }
    
    void unselectedFilter() {
    	ImageProducer producer = new FilteredImageSource(original.getSource(), unselectedFilter);
    	super.setImage(Toolkit.getDefaultToolkit().createImage(producer));
    	revalidate();
    	repaint();
    }
    
    void noFilter() {
    	super.setImage(original);
    	revalidate();
    	repaint();
    }
}
