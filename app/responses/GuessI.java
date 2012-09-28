package responses;

import models.Guess;

public class GuessI {

	public double answer;
	public long id;
	public long answeredProblemId;
	public long playerId;
	
	public GuessI(Guess g) {
		this.answer = g.answer;
		this.id = g.id;
		this.answeredProblemId = g.problemAnswered!=null?g.problemAnswered.id:-1;
		this.playerId = g.player.id;
	}
}
