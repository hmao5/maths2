package responses;

import models.User;


public class PlayerI {
	
	public boolean ready;
	public boolean alive;
	public String name;
	public int[] score;
	public long id;
	
	public PlayerI(User pl) {
		ready = pl.ready;
		alive = pl.alive;
		name = pl.name;
		score = new int[pl.score.length];
		System.arraycopy(pl.score, 0, score, 0, pl.score.length);
		id = pl.id;
	}
}
