package com.ifeed.gameboard.makabana;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GameProtocol {
	
	// Coordinator
	@SuppressWarnings("unchecked")
	public static JSONObject preGame(int numPlayers) {
		JSONObject json = new JSONObject();
		json.put("action", "PRE_GAME");
		json.put("numPlayers", numPlayers);
		return json;
	}
	
	// Player
	@SuppressWarnings("unchecked")
	public static JSONObject connect(String playerName) {
		JSONObject json = new JSONObject();
		json.put("action", "CONNECT");
		json.put("player", playerName);
		return json;
	}
	
	// Player
	@SuppressWarnings("unchecked")
	public static JSONObject quit() {
		JSONObject json = new JSONObject();
		json.put("action", "QUIT");
		return json;
	}
	
	// Coordinator
	@SuppressWarnings("unchecked")
	public static JSONObject gameStart(String inGameName) {
		JSONObject json = new JSONObject();
		json.put("action", "GAME_START");
		json.put("inGameName", inGameName);
		return json;
	}
	
	
	// Coordinator
	@SuppressWarnings("unchecked")
	public static JSONObject turn(List<GamePlayer> players, int turn, boolean finalTurn) {
		JSONObject json = new JSONObject();
		json.put("action", "TURN");
		json.put("turn", turn);
		json.put("final", finalTurn);
		
		JSONArray list = new JSONArray();
		players.forEach(player -> list.add(player.toJSON()));
		json.put("players", list);

		return json;
	}

	// Player
	@SuppressWarnings("unchecked")
	public static JSONObject project(String playerName, List<String> cards, boolean paint, String revealCard) {
		JSONObject json = new JSONObject();
		json.put("action", "PROJECT");
		json.put("player", playerName);
		
		JSONArray list = new JSONArray();
		cards.forEach(card -> list.add(card));
		json.put("cards", list);
		
		json.put("paint", paint);
		json.put("reveal", revealCard);		
		return json;
	}
	
	// Coordinator
	@SuppressWarnings("unchecked")
	public static JSONObject reveals(List<Move> reveals) {
		JSONObject json = new JSONObject();
		json.put("action", "REVEALS");
		
		JSONArray list = new JSONArray();
		reveals.forEach(reveal -> list.add(reveal.revealJSON()));
		json.put("reveals", list);

		return json;
	}
	
	// Coordinator
	@SuppressWarnings("unchecked")
	public static JSONObject tikiReq() {
		JSONObject json = new JSONObject();
		json.put("action", "TIKI_REQ");
		return json;
	}
	
	// Player
	@SuppressWarnings("unchecked")
	public static JSONObject tikiResp(String playerName, List<String> cards) {
		JSONObject json = new JSONObject();
		json.put("action", "TIKI_RESP");
		json.put("player", playerName);
		
		JSONArray list = new JSONArray();
		cards.forEach(card -> list.add(card));
		json.put("cards", list);	
		
		return json;
	}
	
	// Coordinator
	@SuppressWarnings("unchecked")
	public static JSONObject tiki(JSONObject tikiResp) {
		tikiResp.put("action", "TIKI");
		return tikiResp;
	}

	// Coordinator
	@SuppressWarnings("unchecked")
	public static JSONObject projects(List<Move> projects, String winner) {
		JSONObject json = new JSONObject();
		json.put("action", "PROJECTS");
		
		JSONArray projectsList = new JSONArray();
		projects.forEach(reveal -> projectsList.add(reveal.projectJSON()));
		json.put("projects", projectsList);
		
		if (winner != null)
			json.put("winner", winner);

		return json;
	}
}
