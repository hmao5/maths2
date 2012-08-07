package responses;

import models.User;


public class PlayerI {
	
	public boolean ready;
	public int score;
	public long id;
	
	public PlayerI(User pl) {
		ready = pl.ready;
		score = pl.score;
		id = pl.id;
	}
}
