package edu.uclm.esi.ds.Games.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Token {
	
	@Id @Column(length = 36)
	private String id;
	private Long creationTime;
	private Long confirmationTime;
	@ManyToOne
	private User user;
	
	public Token() {
		this.id = UUID.randomUUID().toString();
		this.creationTime = System.currentTimeMillis();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}

	public Long getConfirmationTime() {
		return confirmationTime;
	}

	public void setConfirmationTime(Long confirmationTime) {
		this.confirmationTime = confirmationTime;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
