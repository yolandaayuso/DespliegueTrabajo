package edu.uclm.esi.ds.Games.domain;

import java.util.concurrent.ConcurrentHashMap;

import edu.uclm.esi.ds.Games.entities.User;

public class WaitingRoom {
	
	private ConcurrentHashMap<String,Match> matches;
	
	public WaitingRoom() {
		this.matches = new ConcurrentHashMap<>();
	}

	public Match findPaidMatch(String juego, User player) {
		MultiMatch match = (MultiMatch) this.matches.get(juego);
		if(match == null) {
			match = new MultiMatch();
			((MultiMatch) match).addPlayer(player);
			this.matches.put(juego, match);
		}else {
			((MultiMatch) match).addPlayer(player);
			((MultiMatch) match).setReady(true);
			((MultiMatch) match).notifyStart();
			this.matches.remove(juego);
		}
		return match;
	}
}