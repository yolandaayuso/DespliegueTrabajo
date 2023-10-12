package edu.uclm.esi.ds.Games.domain;

import java.util.UUID;

public class Match {
	
	private String id;
	private boolean ready;

	public Match() {
		this.id = UUID.randomUUID().toString();
		this.ready = false;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public boolean isReady() {
		return this.ready;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
}