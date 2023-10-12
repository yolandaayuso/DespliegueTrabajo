package edu.uclm.esi.ds.Games.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;



@Entity
public class MultiMatchEntity {
	
    @Id
    private String id;

    @OneToOne
    private User player1;

    @OneToOne
    private User player2;

    @OneToOne
    private User winner;
    
    @OneToOne
    private User loser;
    

	public MultiMatchEntity() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public User getPlayer1() {
		return player1;
	}

	public void setPlayer1(User player1) {
		this.player1 = player1;
	}


	public User getPlayer2() {
		return player2;
	}

	public void setPlayer2(User player2) {
		this.player2 = player2;
	}

	public User getWinner() {
		return winner;
	}

	public void setWinner(User winner) {
		this.winner = winner;
	}

	public User getLoser() {
		return loser;
	}

	public void setLoser(User loser) {
		this.loser = loser;
	}
}