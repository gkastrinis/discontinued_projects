package com.ifeed.gameboard.makabana.view.panels;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import com.ifeed.gameboard.makabana.Config;
import com.ifeed.gameboard.makabana.view.CardPane;

public class CardFactory {

	static List<CardPane> createBeachCards(int numPlayers) {
		int numBeaches = numPlayers <= 4 ? 4 : 5;
		List<CardPane> beachCards = new ArrayList<>(numBeaches);
		beachCards.add(new CardPane("beachA", Config.getInstance().getImage("beachA"), false));
		beachCards.add(new CardPane("beachB", Config.getInstance().getImage("beachB"), false));
		beachCards.add(new CardPane("beachC", Config.getInstance().getImage("beachC"), false));
		beachCards.add(new CardPane("beachD", Config.getInstance().getImage("beachD"), false));
		if (numBeaches == 5)
			//beachCards.add(new CardPane("beachE", Config.getInstance().getImage("beachE"), false));
			beachCards.add(new CardPane("beachE", Config.getInstance().getImage("playerBg"), false));
		return beachCards;
	}
	
	static List<CardPane> createSiteCards() {
		List<CardPane> siteCards = new ArrayList<>(4);
		siteCards.add(new CardPane("tribal", Config.getInstance().getImage("tribal"), false));
		siteCards.add(new CardPane("flowers", Config.getInstance().getImage("flowers"), false));
		siteCards.add(new CardPane("fish", Config.getInstance().getImage("fish"), false));
		//siteCards.add(new CardPane("diver", Config.getInstance().getImage("diver"), false));
		siteCards.add(new CardPane("diver", Config.getInstance().getImage("playerBg"), false));
		return siteCards;
	}
	
	static List<CardPane> createTerrainCards() {
		List<CardPane> terrainCards = new ArrayList<>(4);
		terrainCards.add(new CardPane("stone", Config.getInstance().getImage("stone"), false));
		terrainCards.add(new CardPane("grass", Config.getInstance().getImage("grass"), false));
		terrainCards.add(new CardPane("sand", Config.getInstance().getImage("sand"), false));
		terrainCards.add(new CardPane("sea", Config.getInstance().getImage("sea"), false));
		return terrainCards;
	}
	
	static List<CardPane> createPaintCards() {
		Image paintImg = Config.getInstance().getImage("paint");
		List<CardPane> paintCards = new ArrayList<>(2);
		paintCards.add(new CardPane("paint", paintImg, true));
		paintCards.add(new CardPane("paint", paintImg, true));
		return paintCards;
	}
}
