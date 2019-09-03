package com.ifeed.gameboard.makabana;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.json.simple.JSONObject;

import com.ifeed.gameboard.Coordinator;
import com.ifeed.gameboard.Log;
import com.ifeed.gameboard.Player;

public class GameCoordinator extends Coordinator {

	static final Color[] COLORS = {Color.RED, Color.CYAN, Color.ORANGE, Color.GREEN, Color.YELLOW, Color.MAGENTA};
	public static final int MIN_PLAYERS = 1;
	public static final int MAX_PLAYERS = 6;
	public static final int INITIAL_HUTS = 10;
	public static final int INITIAL_PAINTS = 2;
	
	List<Color> availableColors;
	int lastIndex;

	final Game game;
	final int numPlayers;
	List<GamePlayer> players;
	
	final IUpdatable updatable;
	
	public GameCoordinator(int numPlayers, IUpdatable updatable) {
		availableColors = new ArrayList<Color>(Arrays.asList(COLORS));
		Collections.shuffle(availableColors, new Random(System.nanoTime()));
		
		this.game = new Game(numPlayers);
		this.numPlayers = numPlayers;
		this.players = new ArrayList<>(numPlayers);
		this.updatable = updatable;
	}
	
	@Override
	public boolean start(int port) {
		boolean isReady = super.start(port);
		if (isReady) updatable.update("HOST_START");
		else updatable.update("HOST_ERR");
		return isReady;
	}
	
	@Override
	public synchronized void stop() {
		super.stop();
		players.forEach((player) -> player.disconnect());
		players.clear();
		updatable.update("HOST_STOP");
	}

	@Override
	public Player accept() {
		GamePlayer p = null;
		Player basePlayer = super.accept();
		if (basePlayer == null) return null;
		
		basePlayer.send(GameProtocol.preGame(numPlayers));

		JSONObject obj = basePlayer.receive();
		if (obj.get("action").equals("CONNECT")) {
			synchronized(this) {
				String origName = (String) obj.get("player");
				String name = (players.stream().filter(pl -> pl.name.equals(origName)).count() > 0) ?
								origName +"_"+ (++lastIndex) : origName;

				if (availableColors.isEmpty())
					Log.fail(this, "No more colors");
				Color c = availableColors.remove(0);
				
				p = new GamePlayer(basePlayer, name, c, INITIAL_HUTS, INITIAL_PAINTS);
				players.add(p);
			}
		} else {
			basePlayer.disconnect();
		}
		return p;
	}
	
	public synchronized int getPlayers() {
		return players == null ? 0 : players.size();
	}
	
//	synchronized void checkConnections() {
//		for (GamePlayer p : players)
//			if (!p.isConnectedRemotely()) {
//				players.remove(p);
//				System.out.println("p disconnected");
//			}		
//	}
	
	
	
	public void serve() {
		while (!Thread.currentThread().isInterrupted() && players.size() < numPlayers)
			if (accept() == null)
				return;
		super.stop();
		
		play();
	}
	
	void play() {
		updatable.update("HOST_GAME_START");

		Collections.shuffle(players, new Random(System.nanoTime()));
		game.setPlayers(players);

		players.forEach(player -> player.send(GameProtocol.gameStart(player.name)));

		int turn = 1;
		boolean finalTurn = false;
		List<Move> projects = new ArrayList<>(numPlayers);
		List<Move> tikis = new ArrayList<>(numPlayers);
		
		while (true) {
			// (TURN ->)
			finalTurn = game.isFinalTurn();
			Log.out("============ "+ (finalTurn ? " (final) " : "") +"turn "+ turn + " ============");
			JSONObject turnJSON = GameProtocol.turn(players, turn, finalTurn);
			players.forEach(player -> player.send(turnJSON));
			
			// (PROJECT <-)
			projects.clear();
			players.forEach(player -> projects.add(new Move(player.receive())));

			// (REVEALS ->)
			JSONObject revealsJSON = GameProtocol.reveals(projects);
			players.forEach(player -> player.send(revealsJSON));
	
			// In order of play
			tikis.clear();
			JSONObject tikiReqJSON = GameProtocol.tikiReq();
			players.forEach(player -> {
				// (TIKI_REQ ->)
				player.send(tikiReqJSON);
				
				// (TIKI_RESP <-)
				JSONObject json = player.receive();
				tikis.add(new Move(json));
				
				// (TIKI ->)
				players.forEach(p -> p.send(GameProtocol.tiki(json)));
			});
			
			// Update game state
			GamePlayer winner = game.playTurn(projects, tikis);
			String winnerName = (winner == null ? null : winner.name);
			
			// (MOVES ->)
			JSONObject tikisJSON = GameProtocol.projects(projects, winnerName);
			players.forEach(player -> player.send(tikisJSON));
			
			// Game ended?
			if (finalTurn) break;
			
			// Next turn
			turn++;
			GamePlayer first = players.remove(0);
			players.add(first);
		}
	}
}