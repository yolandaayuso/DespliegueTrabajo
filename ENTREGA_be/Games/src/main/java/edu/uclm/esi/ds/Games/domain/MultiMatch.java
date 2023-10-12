package edu.uclm.esi.ds.Games.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import edu.uclm.esi.ds.Games.entities.User;
import edu.uclm.esi.ds.Games.ws.HWSession;
import edu.uclm.esi.ds.Games.ws.Manager;

public class MultiMatch extends Match {

	private List<User> players;
	private HashMap<String, Board> boards;
	private User winner;

	public MultiMatch() {
		this.players = new ArrayList<>();
		this.boards = new HashMap<>();
		this.winner = null;
	}

	public User getWinner() {
		return winner;
	}

	public void setWinner(User winner) {
		this.winner = winner;
	}

	public List<User> getPlayers() {
		return players;
	}

	public void setPlayers(User player) {
		this.players.add(player);
		if (this.players.size() == 2) {
			this.buildBoards();
		}
	}

	public ArrayList<Board> getBoards() {
		return new ArrayList<Board>(this.boards.values());
	}

	private void buildBoards() {
		Board board = new Board();
		this.boards.put(this.players.get(0).getId(), board);
		this.boards.put(this.players.get(1).getId(), board.copy());
	}

	public Board matchNumbers(User player, int x1, int y1, int x2, int y2) throws Exception {
		this.boards.get(player.getId()).matchNumbers(x1, y1, x2, y2);
		notifyOwnMovement(selectPlayer(player), player.getId());
		if (this.boards.get(player.getId()).isEnd()) {
			this.setWinner(player);
			notifyWinner(selectPlayer(player));
		}else if(this.boards.get(player.getId()).isLost()) {
			if(player.getId().equals(this.players.get(0).getId())) {
				this.setWinner(this.players.get(1));
				notifyWinner(this.players.get(0).getId());
			}else {
				this.setWinner(this.players.get(0));
				notifyWinner(this.players.get(1).getId());
			}
		}
		return this.boards.get(player.getId());
	}

	public Board addRows(User player) {
		this.boards.get(player.getId()).addRows();
		notifyOwnMovement(selectPlayer(player), player.getId());
		return this.boards.get(player.getId());
	}

	protected void addPlayer(User player) {
		this.setPlayers(player);
	}

	public String selectPlayer(User player) {
		String idPlayer = this.players.get(0).getId();
		if (idPlayer.equals(player.getId())) {
			idPlayer = this.players.get(1).getId();
		}
		return idPlayer;
	}

	public void notifyStart() {
		for (int i = 0; i < this.players.size(); i++) {
			HWSession hwSession = Manager.get().getSessionByUserId(players.get(i).getId());
			WebSocketSession wsSession = hwSession.getWebsocketSession();
			JSONObject jso = new JSONObject()
					.put("id", super.getId())
					.put("ready", true)
					.put("players", this.getPlayers())
					.put("boards", this.getBoards());
			TextMessage message = new TextMessage(jso.toString());
			try {
				wsSession.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void notifyOwnMovement(String enemyPlayer, String ownPlayer) {
		HWSession hwSession = Manager.get().getSessionByUserId(enemyPlayer);
		WebSocketSession wsSession = hwSession.getWebsocketSession();
		JSONObject jso = new JSONObject()
				.put("id", super.getId())
				.put("digits",this.boards.get(ownPlayer).getDigits());
		TextMessage message = new TextMessage(jso.toString());
		try {
			wsSession.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void notifyWinner(String enemyPlayer) {
		for (int i = 0; i < this.players.size(); i++) {
			HWSession hwSession = Manager.get().getSessionByUserId(players.get(i).getId());
			WebSocketSession wsSession = hwSession.getWebsocketSession();
			JSONObject jso = new JSONObject()
				.put("id", super.getId())
				.put("winner", this.getWinner().getName());
			TextMessage message = new TextMessage(jso.toString());
			try {
				wsSession.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void notifyEnemyLeave() {
		for (int i = 0; i < this.players.size(); i++) {
			HWSession hwSession = Manager.get().getSessionByUserId(players.get(i).getId());
			WebSocketSession wsSession = hwSession.getWebsocketSession();
			JSONObject jso = new JSONObject()
					.put("winner", "Oponent left the game")
					.put("leave", "Oponent left the game");
			TextMessage message = new TextMessage(jso.toString());
			try {
				wsSession.sendMessage(message);
			} catch (IOException e) { }
		}
	}
}