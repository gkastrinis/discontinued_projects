package com.ifeed.gameboard.makabana.view.panels;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.ifeed.gameboard.Coordinator;
import com.ifeed.gameboard.makabana.Config;
import com.ifeed.gameboard.makabana.GameCoordinator;
import com.ifeed.gameboard.makabana.GamePlayer;
import com.ifeed.gameboard.makabana.IUpdatable;
import com.ifeed.gameboard.makabana.controller.TextFieldHint;
import com.ifeed.gameboard.makabana.view.GameButton;
import com.ifeed.gameboard.makabana.view.GameLabel;
import com.ifeed.gameboard.makabana.view.GameTextField;
import com.ifeed.gameboard.makabana.view.PanelWithBackground;

@SuppressWarnings("serial")
public class Panel_02_PreGame extends PanelWithBackground implements IUpdatable {
	
	static final String hostBtnText = "Host Game";
	static final String cancelHostingText = "Cancel Hosting";
	static final String connectBtnText = "Connect to Game";
	static final String connectBtnAltText = "Connect to Local Game";
	static final String cancelConnectionText = "Cancel Connection";
	static final String hintText = "Enter host address";
	static final String hostingStatus = "Hosting...";
	static final String hostingFailed = "Hosting Failed!";
	static final String connectedStatus = "Connected...";
	static final String connectionFailed = "Connection Failed!";
	static final String localConnectionFailed = "Local Connection Failed!";
	static final String invalidGame = "Invalid Game!";

	List<GameButton> playerBtns;
	
	GameButton hostBtn;
	GameTextField localIPFld;
	GameLabel hostStatusLbl;

	GameButton connectBtn;
	GameTextField remoteIPFld;
	GameLabel connectStatusLbl;
	
	String playerName;
	int port;

	int numPlayers;	
	GameCoordinator coordinator;
	GamePlayer player;
	
	
	public Panel_02_PreGame(String playerName) {
		super(new GridLayout(2, 1, 0, GUI.PADDING), Config.getInstance().getImage("pregameBg"));
		setBorder(new EmptyBorder(GUI.PADDING, GUI.PADDING, GUI.PADDING, GUI.PADDING));
		
		this.playerName = playerName;
		this.port = Integer.parseInt(Config.getInstance().get("port"));
		
		int maxNumPlayers = GameCoordinator.MAX_PLAYERS - GameCoordinator.MIN_PLAYERS + 1;
		playerBtns = new ArrayList<>(maxNumPlayers);
		for (int i = GameCoordinator.MIN_PLAYERS ; i <= GameCoordinator.MAX_PLAYERS ; i++) {
			GameButton btn = new GameButton(i + " Players");
			btn.addActionListener(new SelectPlayersBtnListener());
			playerBtns.add(btn);
		}
		
		hostBtn = new GameButton(hostBtnText);
		hostBtn.setEnabled(false);
		hostBtn.addActionListener(new HostBtnListener());
		
		localIPFld = new GameTextField(Coordinator.getLocalIP());
		localIPFld.toOnlyText();
		localIPFld.setEditable(false);
		localIPFld.setVisible(false);

		hostStatusLbl = new GameLabel();
		hostStatusLbl.setVisible(false);

		connectBtn = new GameButton(connectBtnText);
		connectBtn.setEnabled(false);
		connectBtn.addActionListener(new ConnectBtnListener());
		
		remoteIPFld = new GameTextField(hintText);
		remoteIPFld.setEnabled(false);
		remoteIPFld.addFocusListener(new TextFieldHint(hintText));

		connectStatusLbl = new GameLabel();
		connectStatusLbl.setVisible(false);


		JPanel top = new JPanel(new GridLayout(1, maxNumPlayers, GUI.PADDING, GUI.PADDING));
		top.setOpaque(false);
		playerBtns.forEach(btn -> top.add(btn));
		
		JPanel bottom = new JPanel(new GridLayout(3, 2, GUI.PADDING, GUI.PADDING));
		bottom.setOpaque(false);
		bottom.add(hostBtn);
		bottom.add(connectBtn);
		bottom.add(localIPFld);
		bottom.add(remoteIPFld);
		bottom.add(hostStatusLbl);
		bottom.add(connectStatusLbl);

		add(top);
		add(bottom);
	}
	
	
	boolean remoteConnection = true;
	
	void setupForLocalConnection() {
		connectBtn.setText(connectBtnAltText);
		remoteIPFld.setText("localhost");
		remoteIPFld.setEnabled(false);
		remoteIPFld.setVisible(false);
		remoteConnection = false;
	}
	
	void setupForRemoteConnection() {
		cancelConnectionIfAny();
		remoteIPFld.setText(hintText);
		remoteIPFld.setVisible(true);
		remoteConnection = true;
	}

	void lockPlayersSelection() {
		playerBtns.forEach(btn -> btn.setEnabled(false));
	}
	
	void unlockPlayersSelection() {
		playerBtns.forEach(btn -> btn.setEnabled(true));
	}
	
	void startHosting() {
		lockPlayersSelection();
		setupForLocalConnection();
		hostBtn.setText(cancelHostingText);
		hostBtn.active();
		localIPFld.setVisible(true);
		hostStatusLbl.setText(hostingStatus);
		hostStatusLbl.toStatus();
		hostStatusLbl.setVisible(true);
	}
	
	void stopHosting() {
		unlockPlayersSelection();
		setupForRemoteConnection();
		hostBtn.setText(hostBtnText);
		hostBtn.inactive();
		localIPFld.setVisible(false);
		hostStatusLbl.setVisible(false);
	}
	
	void errHosting() {
		hostStatusLbl.setText(hostingFailed);
		hostStatusLbl.toError();
	}
	
	void startConnection() {
		if (remoteConnection) {
			lockPlayersSelection();
			hostBtn.setEnabled(false);
			localIPFld.setEnabled(false);
		}
		connectBtn.setText(cancelConnectionText);
		connectBtn.active();
		remoteIPFld.setEnabled(false);
		connectStatusLbl.setText(connectedStatus);
		connectStatusLbl.toStatus();
		connectStatusLbl.setVisible(true);
	}
	
	void stopConnection() {
		if (remoteConnection) {
			unlockPlayersSelection();
			hostBtn.setEnabled(true);
		}
		cancelConnectionIfAny();
	}
	
	void cancelConnectionIfAny() {
		connectBtn.setText(connectBtnText);
		connectBtn.inactive();
		remoteIPFld.setEnabled(true);
		connectStatusLbl.setVisible(false);
	}
	
	void errConnection() {
		connectStatusLbl.setText(connectionFailed);
		connectStatusLbl.toError();
		connectStatusLbl.setVisible(true);
	}
	
	void invalidConnection() {
		connectStatusLbl.setText(invalidGame);
		connectStatusLbl.toError();
		connectStatusLbl.setVisible(true);
	}
	
	void gameStart() {
		JFrame window = (JFrame) SwingUtilities.getWindowAncestor(Panel_02_PreGame.this);
		window.getContentPane().removeAll();
		window.getContentPane().add(new Panel_03_Game(player));
		window.revalidate();
	}


	
	ScheduledExecutorService scheduler;

	@Override
	public void update(String msg) {
		switch (msg) {
		case "HOST_START":
			startHosting();
			scheduler = Executors.newSingleThreadScheduledExecutor();
			scheduler.scheduleAtFixedRate(new Runnable() {
				public void run() {
					String text = String.format("%s (%d/%d)", hostingStatus, coordinator.getPlayers(), numPlayers);
					hostStatusLbl.setText(text);
				}
			}, 0, 5, TimeUnit.SECONDS);
			break;
		case "HOST_STOP":
			stopHosting();
			scheduler.shutdownNow();
			break;
		case "HOST_ERR":
			errHosting();
			break;
		case "HOST_GAME_START":
			scheduler.shutdownNow();
			break;
		
		case "CONNECT_START":
			startConnection();
			break;
		case "CONNECT_STOP":
			stopConnection();
			break;
		case "CONNECT_ERR":
			errConnection();
			break;
		case "CONNECT_INVALID":
			invalidConnection();
			break;
		case "CONNECT_GAME_START":
			gameStart();
			break;
			
		case "INGAME_NAME":
			JFrame window = (JFrame) SwingUtilities.getWindowAncestor(this);
			window.setTitle(window.getTitle().replaceFirst(" - .*$", " - "+ player.name));
			break;
		}
	}
	
	
	
	class SelectPlayersBtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			hostBtn.setEnabled(true);
			connectBtn.setEnabled(true);
			remoteIPFld.setEnabled(true);
			
			playerBtns.forEach(btn -> {
				if (btn.equals(e.getSource())) {
					btn.tap();
					numPlayers = GameCoordinator.MIN_PLAYERS + playerBtns.indexOf(btn);
				} else {
					btn.untap();
				}
			});
		}
	}
	
	
	class HostBtnListener implements ActionListener {
		boolean inactive = true;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (inactive) {
				coordinator = new GameCoordinator(numPlayers, Panel_02_PreGame.this);
				
				(new Thread(new Runnable() {
					public void run() {
						if (!coordinator.start(port)) return;
						coordinator.serve();
					}
				}, "Coordinator")).start();
			} else {
				coordinator.stop();
			}
			inactive = !inactive;
		}
	}
	
	
	class ConnectBtnListener implements ActionListener {
		boolean inactive = true;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (inactive) {
				player = new GamePlayer(playerName, numPlayers, Panel_02_PreGame.this);

				(new Thread(new Runnable() {
					public void run() {
						if (!player.connect(remoteIPFld.getText(), port)) return;
						player.preGame();
					}
				}, "Player-PreGame")).start();
			} else {
				player.disconnect();
			}
			inactive = !inactive;
		}
	}
}