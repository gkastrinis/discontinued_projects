package com.ifeed.gameboard.makabana;

import java.awt.Color;

public class Spot {
	
	enum Direction {

	    NORTH, SOUTH, EAST, WEST;

	    Direction opposite;

	    static {
	        NORTH.opposite = SOUTH;
	        SOUTH.opposite = NORTH;
	        EAST.opposite = WEST;
	        WEST.opposite = EAST;
	    }
	}
	
	int beachID;
	int siteID;
	int terrainID;
	Spot[] neighbours;
	// Distance from top corner of actual image, in pixels
	public final int xPx;
	public final int yPx;
	
	public Color color;
	public int occupantID;
	public int tikiID;
	
	boolean marked;
	
	public Spot(String beach, String site, String terrain, int xPx, int yPx) {
		this.beachID = beach.hashCode();
		this.siteID = site.hashCode();
		this.terrainID = terrain.hashCode();
		neighbours = new Spot[4];
		this.xPx = xPx;
		this.yPx = yPx;
	}
	
	public void setNeighbours(Spot neighbour, Direction direction) {
		this.neighbours[direction.ordinal()] = neighbour;
		neighbour.neighbours[direction.opposite.ordinal()] = this;
	}
	
	public static Spot fromMove(Move move) {
		assert(move.cards.size() == 3);

		String beach = null, site = null, terrain = null;
		for(String card : move.cards) {
			if (card.equals("beachA") ||
				card.equals("beachB") ||
				card.equals("beachC") ||
				card.equals("beachD") ||
				card.equals("beachE"))
				beach = card;
			else if (card.equals("tribal") ||
					card.equals("flowers") ||
					card.equals("fish") ||
					card.equals("diver"))
				site = card;
			else if (card.equals("stone") ||
					card.equals("grass") ||
					card.equals("sand") ||
					card.equals("sea"))
				terrain = card;
		}
		return new Spot(beach, site, terrain, 0, 0);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + beachID;
		result = prime * result + siteID;
		result = prime * result + terrainID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Spot))
			return false;
		Spot other = (Spot) obj;
		if (beachID != other.beachID)
			return false;
		if (siteID != other.siteID)
			return false;
		if (terrainID != other.terrainID)
			return false;
		return true;
	}
}
