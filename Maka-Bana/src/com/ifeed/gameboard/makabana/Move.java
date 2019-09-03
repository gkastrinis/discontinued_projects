package com.ifeed.gameboard.makabana;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Move {
	public final String playerName;
	public final List<String> cards;
	public final boolean paint;
	public final String revealCard;
	boolean isValid;
	
	@SuppressWarnings("unchecked")
	public Move(JSONObject json) {
		playerName = (String) json.get("player");

		cards = new ArrayList<>();
		JSONArray list = (JSONArray) json.get("cards");
		list.forEach(card -> cards.add((String) card));
		
		Boolean p = (Boolean) json.get("paint");
		paint = (p != null && p);
		
		revealCard = (String) json.get("reveal");
		
		isValid = true;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject cardsJSON() {
		JSONObject json = new JSONObject();
		json.put("player", playerName);
		JSONArray cardsList = new JSONArray();
		cards.forEach(card -> cardsList.add(card));
		json.put("cards", cardsList);
		return json;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject projectJSON() {
		JSONObject json = cardsJSON();
		json.put("paint", paint);
		json.put("isValid", isValid);
		return json;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject revealJSON() {
		JSONObject json = new JSONObject();
		json.put("player", playerName);
		json.put("reveal", revealCard);
		json.put("paint", paint);
		return json;
	}
}