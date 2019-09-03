package com.ifeed.gameboard.makabana;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ifeed.gameboard.Log;
import com.ifeed.gameboard.Player;

public class GamePlayer extends Player {
	public String name;
	public Color color;
	public int huts;
	public int paints;
	public int points;

	public String revealCard;
	public boolean paint;
	
	public int numPlayers;
	List<GamePlayer> players;
	public Game game;
	
	public String opponentWinner;

	IUpdatable updatable;
	
	// Client side - actual player
	public GamePlayer(String name, int numPlayers, IUpdatable updatable) {
		this.name = name;
		this.numPlayers = numPlayers;
		this.game = new Game(numPlayers);
		this.players = new ArrayList<>(numPlayers);
		this.updatable = updatable;
	}

	// Client side - opponent player
	public GamePlayer(JSONObject obj) {
		this.name = (String) obj.get("player");
		this.color = Color.decode((String) obj.get("color"));
		this.huts = ((Long) obj.get("huts")).intValue();
		this.paints = ((Long) obj.get("paints")).intValue();
		this.points = ((Long) obj.get("points")).intValue();
		this.revealCard = (String) obj.get("revealCard");
		Boolean p = (Boolean) obj.get("paint");
		this.paint = p != null && p;
	}
	
	// Client side - dummy opponent player
	public GamePlayer() {
		this.name = "";
	}
	
	// Server side
	public GamePlayer(Player p, String name, Color color, int huts, int paints) {
		super(p);
		this.name = name;
		this.color = color;
		this.huts = huts;
		this.paints = paints;
	}
	
	public boolean equals(GamePlayer other) {
		return name.equals(other.name);
	}
	
	public void setUpdatable(IUpdatable updatable) {
		this.updatable = updatable;
	}
	
	public List<GamePlayer> getPlayers() {
		return players;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("player", name);
		obj.put("color", "#"+Integer.toHexString(color.getRGB()).substring(2));
		obj.put("huts", huts);
		obj.put("paints", paints);
		obj.put("points", points);
		return obj;
	}
	
	
	@Override
	public boolean connect(String host, int port) {
		boolean isReady = super.connect(host, port);
		if (!isReady) {
			updatable.update("CONNECT_ERR");
			return false;
		}

		JSONObject json = receive();
		if ((Long) json.get("numPlayers") == numPlayers) {
			send(GameProtocol.connect(name));
			updatable.update("CONNECT_START");
		} else {
			send(GameProtocol.quit());
			disconnect();
			updatable.update("CONNECT_STOP");
			updatable.update("CONNECT_INVALID");
		}
		return isReady;
	}


	public void preGame() {
		JSONObject json1 = receive();
		assert(json1.get("action").equals("GAME_START"));
		String inGameName = (String) json1.get("inGameName");
		if (!name.equals(inGameName)) {
			name = inGameName;
			updatable.update("INGAME_NAME");
		}
		updatable.update("CONNECT_GAME_START");
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void game() {
		while (true) {
			// (TURN <-)
			JSONObject json = receive();
			JSONArray list = (JSONArray) json.get("players");
			players.clear();
			list.forEach(o -> players.add(new GamePlayer((JSONObject) o)));
			game.setPlayers(players);

			if ((Boolean) json.get("final") == true)
				updatable.update("FINAL_TURN");
			else
				updatable.update("TURN");

			// (REVEALS <-)
			json = receive();
			JSONArray revealsList = (JSONArray) json.get("reveals");
			players.forEach(p -> {
				revealsList.forEach(obj -> {
					JSONObject reveal = (JSONObject) obj;
					if (reveal.get("player").equals(p.name)) {
						p.revealCard = (String) reveal.get("reveal");
						p.paint = (Boolean) reveal.get("paint");						
					}
				});
			});

			while (true) {
				json = receive();
				if (json.get("action").equals("TIKI_REQ")) {
					updatable.update("TIKI");
				} else if (json.get("action").equals("TIKI")) {
					Move tiki = new Move(json);
					game.placeTiki(tiki);
				} else {
					break;
				}
			}
			
			List<Move> projects = new ArrayList<>(numPlayers);
			JSONArray projectsList = (JSONArray) json.get("projects");
			projectsList.forEach(obj -> projects.add(new Move((JSONObject) obj)));
			
			game.playTurn(projects);
			
			String winnerName = (String) json.get("winner");
			if (winnerName != null) {
				if (!name.equals(winnerName))
					this.opponentWinner = winnerName;
				updatable.update("GAME_OVER");
				Log.err(this, "WINNER " + winnerName);
				return;
			}
		}
	}

	public void commitProjectAndReveal(List<String> cards, boolean paint, String revealCard) {
		send(GameProtocol.project(name, cards, paint, revealCard));
	}
	
	public void commitTiki(List<String> cards) {
		send(GameProtocol.tikiResp(name, cards));
	}
}
