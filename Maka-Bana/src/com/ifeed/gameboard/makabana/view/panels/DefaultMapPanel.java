package com.ifeed.gameboard.makabana.view.panels;

import java.util.HashMap;
import java.util.Map;

import com.ifeed.gameboard.makabana.Config;
import com.ifeed.gameboard.makabana.Game;
import com.ifeed.gameboard.makabana.Spot;
import com.ifeed.gameboard.makabana.view.PanelWithBackground;
import com.ifeed.gameboard.makabana.view.SpotMarker;

@SuppressWarnings("serial")
public class DefaultMapPanel extends PanelWithBackground {
	
	Game game;
	Map<Spot, SpotMarker> markers;

	DefaultMapPanel(Game game) {
		super(null, Config.getInstance().getImage("defaultMap"));

		this.game = game;
		markers = new HashMap<>();
		
		game.getSpots().forEach(spot -> {
			SpotMarker marker = new SpotMarker();
			markers.put(spot, marker);
			add(marker);			
		});
	}
	
	@Override
	public void revalidate() {
		updateMarkers();
	}

	@Override
	public void repaint() {
		updateMarkers();
	}
	
	void updateMarkers() {		
		super.revalidate();
		super.repaint();
		if (game == null) return;
	
		int xPxTotalOrig = 800;
		int yPxTotalOrig = 553;
		
		int xPxTotalActual = getWidth();
		int yPxTotalActual = getHeight();
		
		double xRatio = xPxTotalActual / (double) xPxTotalOrig;
		double yRatio = yPxTotalActual / (double) yPxTotalOrig;
		
		int sizeOrig = 30;

		int xSizeActual = (int)(xRatio * sizeOrig);
		int ySizeActual = (int)(yRatio * sizeOrig);
		
		game.getSpots().forEach(spot -> {
			int xPxOrig = spot.xPx;
			int yPxOrig = spot.yPx;
			
			int xPxActual = (int) (xRatio * xPxOrig) - (xSizeActual / 2);
			int yPxActual = (int) (yRatio * yPxOrig) - (ySizeActual / 2);

			SpotMarker marker = markers.get(spot);
			marker.setBounds(xPxActual, yPxActual, xSizeActual, ySizeActual);
			if (spot.tikiID != 0)
				marker.setTiki(spot.color);
			else if (spot.occupantID != 0)
				marker.setHut(spot.color);
			else if (spot.tikiID == 0)
				marker.unsetTiki();
			marker.invalidate();
			marker.repaint();
		});
	}
}
