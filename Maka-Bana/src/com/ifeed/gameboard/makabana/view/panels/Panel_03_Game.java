package com.ifeed.gameboard.makabana.view.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.ifeed.gameboard.Log;
import com.ifeed.gameboard.makabana.Config;
import com.ifeed.gameboard.makabana.GameCoordinator;
import com.ifeed.gameboard.makabana.GamePlayer;
import com.ifeed.gameboard.makabana.IUpdatable;
import com.ifeed.gameboard.makabana.view.CardPane;
import com.ifeed.gameboard.makabana.view.GameButton;
import com.ifeed.gameboard.makabana.view.Notification;
import com.ifeed.gameboard.makabana.view.PanelWithBackground;
import com.ifeed.gameboard.makabana.view.Notification.Level;
import com.ifeed.gameboard.makabana.view.Notification.Position;

@SuppressWarnings("serial")
public class Panel_03_Game extends PanelWithBackground implements IUpdatable {
	
	static final String HOUSE_SYMBOL = "<FONT FACE=\"Segoe UI Emoji\">\uD83C\uDFE0</FONT>";
	static final String PAINT_SYMBOL = "<FONT FACE=\"Segoe UI Emoji\">\uD83D\uDE47</FONT>";
	static final String POINTS_SYMBOL = "\u2605";
	static final String INFO_TEMPLATE = String.format("<HTML>%sx%%d|%sx%%d|%sx%%d&nbsp;</HTML>",
										HOUSE_SYMBOL, PAINT_SYMBOL, POINTS_SYMBOL);

	enum Stage { PROJECT, REVEAL, TIKI }

	GamePlayer player;
	
	JFrame window;
	
	DefaultMapPanel boardPanel;
	JPanel cardsPanel;
	JPanel infoPanel;
	JPanel miscPanel;

	List<CardPane> beachCards;
	List<CardPane> siteCards;
	List<CardPane> terrainCards;
	List<CardPane> paintCards;
	GameButton playBtn;

	List<CardPane> usedPaintCards;
	List<CardPane> selectedCards;
	CardPane revealCard;
	Stage stage;
	
	MouseAdapter cardAdapter;
	ActionListener projectAction;
	ActionListener revealAction;
	ActionListener tikiAction;

	public Panel_03_Game(GamePlayer player) {
		super(new GridBagLayout(), Config.getInstance().getImage("playBg"));
		this.player = player;
		this.player.setUpdatable(this);
		
		boardPanel = new DefaultMapPanel(player.game);
		
		beachCards = CardFactory.createBeachCards(player.numPlayers);
		siteCards = CardFactory.createSiteCards();
		terrainCards = CardFactory.createTerrainCards();
		paintCards = CardFactory.createPaintCards();
		usedPaintCards = new ArrayList<>(paintCards.size());
		selectedCards = new ArrayList<>();
		
		playBtn = new GameButton("  Play!  ");
		playBtn.setEnabled(false);
		
		newCardsPanel();
		newInfoPanel();
		newMiscPanel();
		
		stage = Stage.PROJECT;
		
		cardAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				tapCard((CardPane) e.getComponent());
			}
		};
		
		Stream.concat(
			Stream.concat(beachCards.stream(), siteCards.stream()),
			Stream.concat(terrainCards.stream(), paintCards.stream())
		).forEach(card -> card.addMouseListener(cardAdapter));
		
		
		projectAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enterRevealStage();
			}
		};
		revealAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enterTikiStage();
			}
		};
		tikiAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitTikiStage();
			}
		};
		

		(new Thread(new Runnable() {
			public void run() {
				player.game();
			}
		}, "Player-Game")).start();
		

		showGame();
	}

	
	
	////////////////////////// GUI //////////////////////////
	
	void showGame() {
		double mapScaleY = 0.85, scaleX = 0.95;
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;		
		
		JPanel leftSide = new JPanel(new GridBagLayout());
		leftSide.setOpaque(false);
		
		c.weighty = mapScaleY;
		c.gridy = 0;
		leftSide.add(boardPanel, c);

		c.weighty = 1 - mapScaleY;
		c.gridy = 1;
		leftSide.add(cardsPanel, c);

		
		JPanel rigthSide = new JPanel(new GridBagLayout());
		rigthSide.setOpaque(false);

		c.weighty = 0;
		
		c.gridy = 0;
		rigthSide.add(infoPanel, c);
		
		c.weighty = 1;
		c.gridy = 1;
		rigthSide.add(miscPanel, c);
		

		c.weighty = 1;
		c.gridy = 0;
		
		c.weightx = scaleX;
		c.gridx = 0;
		add(leftSide, c);

		c.weightx = 1 - scaleX;
		c.gridx = 1;
		add(rigthSide, c);
	}
	
	void newCardsPanel() {
		cardsPanel = new JPanel(new GridBagLayout());
		cardsPanel.setOpaque(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		c.weightx = 1;
		c.gridx = 0;
		
		Insets defaultInsets = new Insets(5,0,0,5);
		Insets inbetweenInsets = new Insets(5,0,0,20);
		Insets lastInsets = new Insets(5,0,0,0);
		
		c.insets = defaultInsets;
		for (int i = 0 ; i < beachCards.size() ; i++) {
			c.gridx++;
			if (i == beachCards.size() - 1)
				c.insets = inbetweenInsets;
			cardsPanel.add(beachCards.get(i), c);
		}
		
		c.insets = defaultInsets;
		for (int i = 0 ; i < siteCards.size() ; i++) {
			c.gridx++;
			if (i == siteCards.size() - 1)
				c.insets = inbetweenInsets;
			cardsPanel.add(siteCards.get(i), c);
		}
		
		c.insets = defaultInsets;
		for (int i = 0 ; i < terrainCards.size() ; i++) {
			c.gridx++;
			if (i == terrainCards.size() - 1)
				c.insets = lastInsets;
			cardsPanel.add(terrainCards.get(i), c);
		}
	}
	
	void newInfoPanel() {
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setOpaque(false);
		
		fillEmptySlots(GameCoordinator.MAX_PLAYERS);
	}
	
	void updateInfoPanel() {
		infoPanel.removeAll();
		
		player.getPlayers().forEach(p -> infoPanel.add(playerInfo(p)));
		fillEmptySlots(GameCoordinator.MAX_PLAYERS - player.numPlayers);
		
		infoPanel.revalidate();
	}
	
	void fillEmptySlots(int n) {
		IntStream.rangeClosed(1, n).forEach(i -> infoPanel.add(playerInfo(new GamePlayer())));		
	}
	
	JPanel playerInfo(GamePlayer p) {
		String displayName = p.name;
		if (displayName.length() >= 16) 
			displayName = displayName.substring(0, 12) + "...";

		JLabel info = new JLabel(displayName + " ");
		info.setHorizontalAlignment(JLabel.RIGHT);
		info.setFont(GUI.altFont1);
		info.setForeground(p.color);

		JLabel stats = new JLabel(String.format(INFO_TEMPLATE, p.huts, p.paints, p.points));
		stats.setHorizontalAlignment(JLabel.RIGHT);
		stats.setFont(GUI.altFont1);
		
		if (p.name.isEmpty()) {
			info.setVisible(false);
			stats.setVisible(false);
		} else if (player.equals(p)) {
			stats.setBackground(new Color(245, 245, 245, 200));
			stats.setForeground(Color.LIGHT_GRAY.darker().darker());
			stats.setOpaque(true);
		} else {
			stats.setForeground(Color.LIGHT_GRAY);
		}

		PanelWithBackground panel = new PanelWithBackground(new GridLayout(2, 1));
		panel.setOpaque(false);
		panel.add(info);
		panel.add(stats);
		if (p.revealCard != null)
			panel.setBackground(Config.getInstance().getRevealImage(p.revealCard, p.paint));
		
		return panel;
	}
	
	void newMiscPanel() {
		miscPanel = new JPanel(new GridBagLayout());
		miscPanel.setOpaque(false);
		
		JPanel paint = new JPanel(new GridBagLayout());
		paint.setOpaque(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		c.weightx = 1;
		c.insets = new Insets(10,5,0,5);
		
		c.gridx = 0;
		paintCards.forEach(card -> {
			paint.add(card, c);
			c.gridx++;
		});
		
		c.gridy = 1;
		c.gridx = 0;
		miscPanel.add(paint, c);
		
		c.gridy = 0;
		c.insets = new Insets(40,10,10,10);
		miscPanel.add(playBtn, c);
	}
	
	
	void lock() {
		//playBtn.setEnabled(false);
		beachCards.forEach(card -> card.lock());
		siteCards.forEach(card -> card.lock());
		terrainCards.forEach(card -> card.lock());
		paintCards.forEach(card -> card.lock());
	}

	void unlock() {
		//playBtn.setEnabled(true);
		beachCards.forEach(card -> card.unlock());
		siteCards.forEach(card -> card.unlock());
		terrainCards.forEach(card -> card.unlock());
		paintCards.forEach(card -> card.unlock());
	}
	
	void showNotification(String msg, Level level, Position position) {
		lock();

		if (window == null)
			window = (JFrame) SwingUtilities.getWindowAncestor(this);

		JPanel notification = new Notification(msg, level, position);
	    window.setGlassPane(notification);
	    window.getGlassPane().setVisible(true);
	}
	
	void showNotification(String msg) {
		showNotification(msg, Level.MESSAGE, Position.CENTER);
	}
	
	void showNotification(String msg, Level level) {
		showNotification(msg, level, Position.CENTER);
	}
	
	void showNotificationBottom(String msg) {
		showNotification(msg, Level.MESSAGE, Position.BOTTOM);
	}

	void showNotificationAndHide(String msg, Level level, int seconds) {
		showNotification(msg, level, Position.CENTER);

		(new Thread(() -> {
			try {
				Thread.sleep(seconds * 1000);
			} catch (InterruptedException e) {
				Log.err(this, "InterruptedException");
			}
			
			SwingUtilities.invokeLater(() -> {
				window.getGlassPane().setVisible(false);
				unlock();
			});
		})).start();
	}
	
	void showNotificationAndHide(String msg) {
		showNotificationAndHide(msg, Level.MESSAGE, 2);
	}
	
	void showNotificationAndHide(String msg, Level level) {
		showNotificationAndHide(msg, level, 2);
	}
	
	////////////////////////// GUI //////////////////////////
	
	

	////////////////////////// Listeners //////////////////////////
	
	void tapCard(CardPane card) {
		if (!card.tap()) return;
		
		List<CardPane> cardSet = null;
		if (stage == Stage.PROJECT || stage == Stage.TIKI) {
			if (beachCards.contains(card))
				cardSet = beachCards;
			else if (siteCards.contains(card))
				cardSet = siteCards;
			else if (terrainCards.contains(card))
				cardSet = terrainCards;
			else if (paintCards.contains(card))
				cardSet = paintCards;
			
			if (card.isSelected()) {
				cardSet.stream()
					.filter(c -> c.isSelected() && !c.equals(card))
					.forEach(c -> {
						c.tap();
						selectedCards.remove(c);
					});					
				selectedCards.add(card);
			} else {
				selectedCards.remove(card);					
			}
		} else {
			cardSet =
				Stream.concat(
					Stream.concat(beachCards.stream(), siteCards.stream()),
					terrainCards.stream()
				).collect(Collectors.toList());
			
			if (card.isSelected()) {
				cardSet.stream()
					.filter(c -> c.isSelected() && !c.equals(card))
					.forEach(c -> c.tap());
				revealCard = card;
			} else {
				revealCard = null;					
			}
		}

		updatePlayBtn();	
	}
	
	void updatePlayBtn() {
		boolean btnIsReady = false;
		
		if (stage == Stage.PROJECT || stage == Stage.TIKI) {
			if (beachCards.stream().filter(card -> card.isSelected()).count() > 0 &&
				siteCards.stream().filter(card -> card.isSelected()).count() > 0 &&
				terrainCards.stream().filter(card -> card.isSelected()).count() > 0)
				btnIsReady = true;
		} else {
			if (revealCard != null)
				btnIsReady = true;			
		}
	
		if (btnIsReady)
			playBtn.setEnabled(true);
		else
			playBtn.setEnabled(false);
		
		playBtn.revalidate();
	}
	
	
	void enterProjectStage() {
		stage = Stage.PROJECT;

		beachCards.forEach(card -> card.reset());
		siteCards.forEach(card -> card.reset());
		terrainCards.forEach(card -> card.reset());
		paintCards.stream().filter(card -> !usedPaintCards.contains(card)).forEach(card -> card.reset());
		selectedCards.clear();
		revealCard = null;
		
		playBtn.removeActionListener(tikiAction);
		playBtn.addActionListener(projectAction);
		playBtn.setEnabled(false);
	}
	
	void enterRevealStage() {
		stage = Stage.REVEAL;
		
		beachCards.forEach(card -> card.doDisable());
		siteCards.forEach(card -> card.doDisable());
		terrainCards.forEach(card -> card.doDisable());
		paintCards.forEach(card -> card.doDisable());
		
		selectedCards.forEach(card -> {
			card.doEnable();
			if (paintCards.contains(card))
				card.lock();
			else
				card.doUnselect();
		});
		
		playBtn.removeActionListener(projectAction);
		playBtn.addActionListener(revealAction);
		playBtn.setEnabled(false);
		

		showNotificationAndHide("Reveal a card!");
	}
	
	void enterTikiStage() {
		stage = Stage.TIKI;
		
		String reveal = revealCard.name;
		List<CardPane> cardsToRemove = selectedCards.stream()
				.filter(card -> paintCards.contains(card))
				.collect(Collectors.toList());
		assert(cardsToRemove.size() <= 1);
		boolean paint = cardsToRemove.size() > 0;
		cardsToRemove.forEach(card -> {
			usedPaintCards.add(card);
			selectedCards.remove(card);
		});

		List<String> cards = selectedCards.stream()
			.map(card -> card.name)
			.collect(Collectors.toList());

		player.commitProjectAndReveal(cards, paint, reveal);
		selectedCards.clear();
		
		
		showNotificationBottom("Please wait others to play");
	}
	
	void exitTikiStage() {
		List<String> cards = selectedCards.stream()
				.map(card -> card.name)
				.collect(Collectors.toList());
		
		player.commitTiki(cards);
		
		
		showNotificationBottom("Please wait others to play");
	}
	
	////////////////////////// Listeners //////////////////////////
	

	@Override
	public void update(String msg) {
		switch (msg) {
		case "FINAL_TURN":
			SwingUtilities.invokeLater(() -> {
				boardPanel.revalidate();
				updateInfoPanel();
				enterProjectStage();
				
				
				showNotificationAndHide("Select your project! (Final Turn)", Level.ERROR);
			});
			break;
		case "TURN":
			SwingUtilities.invokeLater(() -> {
				boardPanel.revalidate();
				updateInfoPanel();
				enterProjectStage();
				
				
				showNotificationAndHide("Select your project!");
			});
			break;
		case "TIKI":
			SwingUtilities.invokeLater(() -> {
				boardPanel.revalidate();
				updateInfoPanel();
				
				beachCards.forEach(card -> card.reset());
				siteCards.forEach(card -> card.reset());
				terrainCards.forEach(card -> card.reset());
				paintCards.forEach(card -> card.doDisable());
				
				playBtn.removeActionListener(revealAction);
				playBtn.addActionListener(tikiAction);
				playBtn.setEnabled(false);

				
				showNotificationAndHide("Place your Tiki!");
			});
			break;
		case "GAME_OVER":
			SwingUtilities.invokeLater(() -> {
				updateInfoPanel();
				enterProjectStage();
				
				if (player.opponentWinner == null)
					showNotification("You Won!", Level.SUCCESS);
				else
					showNotification(player.opponentWinner + " Won! (Game Over)", Level.ERROR);
			});
			break;
		}
	}
}