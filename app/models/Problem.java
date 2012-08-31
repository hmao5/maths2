package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;
import problems.ProblemGenerator;
import problems.QuestionAnswerPair;

@Entity
public class Problem extends Model {

	@ManyToOne
	public GameInstance game;
	public String question;
	public double answer;
	@ManyToOne
	public User answeredBy;
	public int position;
	
	public Problem(GameInstance game, int position, int round) {
		this.game = game;
		QuestionAnswerPair pair = ProblemGenerator.generate(round);
		this.question = pair.getProblem();
		this.answer = pair.getAnswer();
		this.answeredBy = null;
		this.position = position;
	}
}
