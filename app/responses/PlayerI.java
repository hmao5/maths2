package responses;

import models.User;


public class PlayerI {
	
	public boolean ready;
	public boolean alive;
	public String name;
	public int score;
	public long id;
	
	public PlayerI(User pl) {
		ready = pl.ready;
		alive = pl.alive;
		name = pl.name;
		score = pl.score;
		id = pl.id;
	}
}
