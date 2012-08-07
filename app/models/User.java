package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	@Required
	public String name;
	
	@ManyToOne
	public GameInstance game;
	
	public int score;
	
	public boolean ready;
	
	public boolean alive;
	
	public int position;
	
	public long lastHeartbeat;
	
	public User(String name, GameInstance game, int position) {
		this.name = name;
		this.game = game;
		this.score = 0;
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
