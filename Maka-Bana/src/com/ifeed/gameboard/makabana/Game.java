package com.ifeed.gameboard.makabana;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.ifeed.gameboard.Log;
import com.ifeed.gameboard.makabana.Spot.Direction;

public class Game {	

	// "Fixed" Board info
	// NOTE: Stupid use of Map since Set doesn't expose a get method!
	protected Map<Spot, Spot> spots;
	protected Map<Integer, Set<Spot>> spotsPerBeach;
	
	// Running Game info
	boolean finalTurn;
	final int numPlayers;
	List<GamePlayer> players;
	Map<Integer, GamePlayer> idToPlayer;
	Map<Integer, Integer> pointsPerPlayer;

	
	public Game(int numPlayers) {		
		manualFill();

		this.numPlayers = numPlayers;
		idToPlayer = new HashMap<>(numPlayers);
		pointsPerPlayer = new HashMap<>(numPlayers);
	}
	
	public Collection<Spot> getSpots() {
		return spots.values();
	}
	
	public boolean isFinalTurn() {
		return finalTurn;
	}
	
	public void setPlayers(List<GamePlayer> players) {
		this.players = players;
		players.forEach(player -> idToPlayer.put(player.name.hashCode(), player));
	}
	
	GamePlayer getPlayer(String name) {
		return idToPlayer.get(name.hashCode());
	}
	
	GamePlayer getPlayer(int id) {
		return idToPlayer.get(id);
	}
	
	GamePlayer getPlayer(Move move) {
		return idToPlayer.get(move.playerName.hashCode());
	}
	
	int getID(String name) {
		return name.hashCode();
	}
	
	int getID(Move move) {
		return move.playerName.hashCode();
	}
	
	int getID(GamePlayer player) {
		return player.name.hashCode();
	}
	
	
	public void placeTiki(Move tiki) {
		Spot key = Spot.fromMove(tiki);
		Spot spot = spots.get(key);
		if (spot == null) {
			tiki.isValid = false;
			return;
		}
		// Tikis can only be placed on an empty spot, or on a spot occupied by the same player
		if ( ! (spot.occupantID == 0 || (spot.occupantID == getID(tiki))) ) {
			tiki.isValid = false;
			return;
		}
		
		GamePlayer player = getPlayer(tiki);
		spot.color = player.color;
		spot.tikiID = getID(player);
	}
	
	public GamePlayer playTurn(List<Move> projects) {
		projects.forEach(project -> {
			Spot key = Spot.fromMove(project);
			Spot spot = spots.get(key);
			if (spot == null) {
				project.isValid = false;
				return;
			}
			
			GamePlayer projectPlayer = getPlayer(project);
			if (project.paint) projectPlayer.paints--;
			
			// INVALID because of TIKI
			if (spot.tikiID != 0) {
				project.isValid = false;
			}
			// Take-Back project
			else if (spot.occupantID != 0 && project.paint) {
				GamePlayer oldPlayer = getPlayer(spot.occupantID);
				
				spot.color = projectPlayer.color;
				spot.occupantID = getID(projectPlayer);
				oldPlayer.huts++;
				projectPlayer.huts--;
			}
			// Building project
			else if (spot.occupantID == 0 && !project.paint) {
				spot.color = projectPlayer.color;
				spot.occupantID = getID(projectPlayer);
				projectPlayer.huts--;
			}
			// INVALID because of project or wrong cards
			else {
				project.isValid = false;
			}
		});
		
		calculatePoints();
		
		
		// IMMEDIATE GAME OVER
		int finishedPlayers = (int) players.stream().filter(player -> player.huts == 0).count();
		if (finishedPlayers > 0) {
			GamePlayer winner = players.stream().max((p1, p2) -> Integer.compare(p1.points, p2.points)).get();
			if (finishedPlayers == 1)
				Log.out("GAME OVER");
			else
				Log.err(this, "GAME OVER - TIE!");
			return winner;
		}
		
		// FINAL TURN
		spotsPerBeach.values().forEach(beachSpots -> {
			int occupiedSpots = (int) beachSpots.stream()
					.filter(spot -> spot.occupantID != 0 || spot.tikiID != 0)
					.count();
			if (occupiedSpots == beachSpots.size()) {
				finalTurn = true;
				Log.out("FINAL TURN");
			}
		});
		
		return null;
	}
	
	public GamePlayer playTurn(List<Move> projects, List<Move> tikis) {
		spots.values().forEach(spot -> spot.tikiID = 0);
		
		tikis.forEach(tiki -> placeTiki(tiki));
		
		return playTurn(projects);
	}
	
	
	void calculatePoints() {
		players.forEach(player -> pointsPerPlayer.put(player.name.hashCode(), 0));

		spots.values().forEach(spot -> spot.marked = false);
		spots.values().stream().filter(spot -> spot.occupantID == 0).forEach(spot -> spot.marked = true);
		
		spots.values().forEach(spot -> {
			if (spot.marked) return;
			
			int count = sameHutsInGroup(spot, spot.occupantID);
			int points = count;
			if (count == 2) points = 3;
			else if (count > 2) points = 3 + 3*(count-2);
			int totalPoints = pointsPerPlayer.get(spot.occupantID) + points;
			pointsPerPlayer.put(spot.occupantID, totalPoints);
		});
		
		
		spotsPerBeach.values().forEach(beachSpots -> {
			Map<Integer, Integer> hutsPerPlayer = new HashMap<>(numPlayers);
			
			int max = players.stream()
					.map(player -> {
						final int id = player.name.hashCode();
						int huts = (int) beachSpots.stream().filter(spot -> spot.occupantID == id).count();
						hutsPerPlayer.put(id, huts);
						return huts;
					})
					.max(Integer::compare).get();

			// Beach is not empty
			if (max != 0) {
				List<Integer> dominatingPlayers = hutsPerPlayer.entrySet().stream()
						.filter(entry -> entry.getValue() == max)
						.map(Entry::getKey)
						.collect(Collectors.toList());
				if (dominatingPlayers.size() == 1) {
					int totalPoints = pointsPerPlayer.get(dominatingPlayers.get(0)) + 4;
					pointsPerPlayer.put(dominatingPlayers.get(0), totalPoints);
				} else if (dominatingPlayers.size() == 2) {
					int totalPoints;
					totalPoints = pointsPerPlayer.get(dominatingPlayers.get(0)) + 2;
					pointsPerPlayer.put(dominatingPlayers.get(0), totalPoints);
					
					totalPoints = pointsPerPlayer.get(dominatingPlayers.get(1)) + 2;
					pointsPerPlayer.put(dominatingPlayers.get(1), totalPoints);
				}
			}
		});
		
		players.forEach(p -> p.points = pointsPerPlayer.get(p.name.hashCode()));
	}
	
	int sameHutsInGroup(Spot spot, int playerID) {
		if (spot == null) return 0;
		if (spot.marked) return 0;
		if (spot.occupantID != playerID) return 0;
		
		spot.marked = true;
		
		return  sameHutsInGroup(spot.neighbours[0], playerID) +
				sameHutsInGroup(spot.neighbours[1], playerID) +
				sameHutsInGroup(spot.neighbours[2], playerID) +
				sameHutsInGroup(spot.neighbours[3], playerID) +
				1;
	}
	
	
	protected void manualFill() {
		spots = new HashMap<>(43);		
		spotsPerBeach = new HashMap<>(4);

		Spot a01 = new Spot("beachA", "flowers", "stone", 295, 490);
		Spot a02 = new Spot("beachA", "tribal", "stone", 256, 488);
		Spot a03 = new Spot("beachA", "fish", "stone", 285, 425);
		Spot a04 = new Spot("beachA", "fish", "grass", 300, 380);
		Spot a05 = new Spot("beachA", "flowers", "grass", 390, 356);
		Spot a06 = new Spot("beachA", "tribal", "grass", 472, 380);
		Spot a07 = new Spot("beachA", "tribal", "sea", 505, 416);
		Spot a08 = new Spot("beachA", "flowers", "sea", 531, 442);
		Spot a09 = new Spot("beachA", "fish", "sea", 586, 480);
		Spot a10 = new Spot("beachA", "diver", "sea", 408, 438);
		
		Spot b01 = new Spot("beachB", "fish", "sand", 704, 424);
		Spot b02 = new Spot("beachB", "flowers", "sand", 668, 408);
		Spot b03 = new Spot("beachB", "tribal", "sand", 547, 347);
		Spot b04 = new Spot("beachB", "tribal", "stone", 517, 311);
		Spot b05 = new Spot("beachB", "flowers", "stone", 532, 182);
		Spot b06 = new Spot("beachB", "flowers", "grass", 566, 151);
		Spot b07 = new Spot("beachB", "fish", "stone", 560, 210);
		Spot b08 = new Spot("beachB", "fish", "grass", 594, 177);
		Spot b09 = new Spot("beachB", "tribal", "grass", 634, 130);
		Spot b10 = new Spot("beachB", "tribal", "sea", 633, 318);
		Spot b11 = new Spot("beachB", "flowers", "sea", 626, 281);
		Spot b12 = new Spot("beachB", "fish", "sea", 620, 244);
		Spot b13 = new Spot("beachB", "diver", "sea", 681, 245);
		
		Spot c01 = new Spot("beachC", "tribal", "grass", 164, 144);
		Spot c02 = new Spot("beachC", "flowers", "grass", 197, 164);
		Spot c03 = new Spot("beachC", "fish", "grass", 229, 186);
		Spot c04 = new Spot("beachC", "tribal", "sea", 234, 264);
		Spot c05 = new Spot("beachC", "flowers", "sea", 222, 315);
		Spot c06 = new Spot("beachC", "fish", "sea", 205, 348);
		Spot c07 = new Spot("beachC", "fish", "sand", 184, 390);
		Spot c08 = new Spot("beachC", "flowers", "sand", 130, 419);
		Spot c09 = new Spot("beachC", "tribal", "sand", 96, 438);
		Spot c10 = new Spot("beachC", "diver", "sea", 144, 283);
		
		Spot d01 = new Spot("beachD", "fish", "sea", 534, 79);
		Spot d02 = new Spot("beachD", "flowers", "sea", 503, 147);
		Spot d03 = new Spot("beachD", "tribal", "sea", 475, 174);
		Spot d04 = new Spot("beachD", "tribal", "sand", 441, 205);
		Spot d05 = new Spot("beachD", "flowers", "sand", 412, 229);
		Spot d06 = new Spot("beachD", "fish", "sand", 293, 173);
		Spot d07 = new Spot("beachD", "fish", "stone", 254, 148);
		Spot d08 = new Spot("beachD", "flowers", "stone", 220, 128);
		Spot d09 = new Spot("beachD", "tribal", "stone", 154, 62);
		Spot d10 = new Spot("beachD", "diver", "sea", 378, 123);
		
		
		Set<Spot> beachSpots;

		beachSpots = new HashSet<>(10);
		beachSpots.add(a01);
		beachSpots.add(a02);
		beachSpots.add(a03);
		beachSpots.add(a04);
		beachSpots.add(a05);
		beachSpots.add(a06);
		beachSpots.add(a07);
		beachSpots.add(a08);
		beachSpots.add(a09);
		beachSpots.add(a10);
		spotsPerBeach.put("beachA".hashCode(), beachSpots);
		beachSpots.forEach(spot -> spots.put(spot, spot));
		
		beachSpots = new HashSet<>(13);
		beachSpots.add(b01);
		beachSpots.add(b02);
		beachSpots.add(b03);
		beachSpots.add(b04);
		beachSpots.add(b05);
		beachSpots.add(b06);
		beachSpots.add(b07);
		beachSpots.add(b08);
		beachSpots.add(b09);
		beachSpots.add(b10);
		beachSpots.add(b11);
		beachSpots.add(b12);
		beachSpots.add(b13);
		spotsPerBeach.put("beachB".hashCode(), beachSpots);
		beachSpots.forEach(spot -> spots.put(spot, spot));

		beachSpots = new HashSet<>(10);
		beachSpots.add(c01);
		beachSpots.add(c02);
		beachSpots.add(c03);
		beachSpots.add(c04);
		beachSpots.add(c05);
		beachSpots.add(c06);
		beachSpots.add(c07);
		beachSpots.add(c08);
		beachSpots.add(c09);
		beachSpots.add(c10);
		spotsPerBeach.put("beachC".hashCode(), beachSpots);
		beachSpots.forEach(spot -> spots.put(spot, spot));

		beachSpots = new HashSet<>(10);
		beachSpots.add(d01);
		beachSpots.add(d02);
		beachSpots.add(d03);
		beachSpots.add(d04);
		beachSpots.add(d05);
		beachSpots.add(d06);
		beachSpots.add(d07);
		beachSpots.add(d08);
		beachSpots.add(d09);
		beachSpots.add(d10);
		spotsPerBeach.put("beachD".hashCode(), beachSpots);
		beachSpots.forEach(spot -> spots.put(spot, spot));
		
		
		a01.setNeighbours(a02, Direction.WEST);
		
		a03.setNeighbours(a04, Direction.NORTH);

		a06.setNeighbours(a07, Direction.EAST);
		a07.setNeighbours(a08, Direction.EAST);
		
		b01.setNeighbours(b02, Direction.EAST);
		
		b03.setNeighbours(b04, Direction.EAST);
		
		b05.setNeighbours(b06, Direction.EAST);
		b05.setNeighbours(b07, Direction.SOUTH);
		b06.setNeighbours(b08, Direction.SOUTH);
		b07.setNeighbours(b08, Direction.EAST);
		b05.setNeighbours(d02, Direction.NORTH);
		
		b10.setNeighbours(b11, Direction.EAST);
		b11.setNeighbours(b12, Direction.EAST);
		
		c01.setNeighbours(c02, Direction.EAST);
		c02.setNeighbours(c03, Direction.EAST);
		c02.setNeighbours(d08, Direction.NORTH);
		c03.setNeighbours(d07, Direction.NORTH);
		
		c05.setNeighbours(c06, Direction.EAST);
		c06.setNeighbours(c07, Direction.EAST);
		
		c08.setNeighbours(c09, Direction.EAST);
		
		d02.setNeighbours(d03, Direction.WEST);
		d03.setNeighbours(d04, Direction.WEST);
		d04.setNeighbours(d05, Direction.WEST);
		
		d06.setNeighbours(d07, Direction.WEST);
		d07.setNeighbours(d08, Direction.WEST);
	}
}