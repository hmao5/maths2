package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	@Required
	public String name;
	
	@ManyToOne
	public GameInstance game;
	
	@OneToMany(mappedBy="player", cascade=CascadeType.ALL)
	public List<Guess> guesses;
	
	public int[] score;
	
	public boolean ready;
	
	public boolean alive;
	
	public int position;
	
	public long lastHeartbeat;
	
	public User(String name, GameInstance game, int position) {
		this.name = name;
		this.game = game;
		this.score = new int[game.totalRounds];
		this.ready = false;
		this.alive = true;
		this.position = position;
		this.lastHeartbeat = System.currentTimeMillis();
	}
	@Override
	public String toString() {
		return name;
	}
}
