package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Problem extends Model {

	@ManyToOne
	public GameInstance game;
	public String question;
	public double answer;
	@ManyToOne
	public User answeredBy;
	public int position;
	
	public Problem(GameInstance game, int position) {
		this.game = game;
		int a = (int)(1+Math.random()*9);
		int b = (int)(1+Math.random()*9);
		this.question = a+"+"+b;
		this.answer = a+b;
		this.answeredBy = null;
		this.position = position;
	}
}
