package edu.uclm.esi.ds.Games.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
@Entity

public class MovementEntity {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	
	    @ManyToOne
	    private MultiMatchEntity multiMatch;

	    @ManyToOne
	    private User player;

	    @Column(name = "movement")
	    private String movement;

		public MovementEntity() {
			super();
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public MultiMatchEntity getMultiMatch() {
			return multiMatch;
		}

		public void setMultiMatch(MultiMatchEntity existingMultimatch) {
			this.multiMatch = existingMultimatch;
		}

		public User getPlayer() {
			return player;
		}

		public void setPlayer(User player) {
			this.player = player;
		}

		public String getMovement() {
			return movement;
		}

		public void setMovement(String movement) {
			this.movement = movement;
		}

	    


	}


