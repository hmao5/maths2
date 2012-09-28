package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class GameInstance extends Model {

	@OneToMany(mappedBy="game", cascade=CascadeType.ALL)
	public List<User> players;
	
	@OneToMany(mappedBy="game", cascade=CascadeType.ALL)
	public List<Problem> problems;
	
	@OneToMany(mappedBy="game", cascade=CascadeType.ALL)
	public List<Guess> guesses;
	
	public int round;
	public int problemsAtOnce;
	public int maxPlayers;
	public int pointsToWin;
	public int roundDuration;
	public int totalRounds;
	
	public boolean started;
	public boolean ended;
	public long roundStartMillis;
	public boolean inRound;
	public boolean roundTimeUp;
	
	public GameInstance(int maxPlayers, int roundDuration) {
		this.players = new ArrayList<User>();
		this.problems = new ArrayList<Problem>();
		this.guesses = new ArrayList<Guess>();
		this.round = 0;
		this.started = false;
		this.ended = false;
		this.roundStartMillis = 0L;
		this.inRound = false;
		this.roundTimeUp = false;
		
		this.maxPlayers = maxPlayers;
		// TODO factor these out of this constructor
		this.problemsAtOnce = 3;
		this.pointsToWin = 5;
		this.roundDuration = roundDuration;
		this.totalRounds = 3;
	}
	
	public void start() {
		Logger.info("game started");
		started = true;
		newRound();
		save();
	}
	public void newRound() {
		inRound = false;
		roundTimeUp = false;
		for(User u: players) {
			u.ready = false;
			u.save();
		}
		for(Guess g: guesses) {
			g.delete();
		}
		guesses.clear();
		round++;
		for(Problem p: problems) {
			p.delete();
		}
		problems.clear();
		if(round>totalRounds) {
			end();
		}
		save();
	}
	public void startRound() {
		inRound = true;
		for(int i=0; i<problemsAtOnce; i++) 
			newProblem(i);
		roundStartMillis = System.currentTimeMillis();
		save();
	}
	public Problem newProblem(int position) {
		if(roundTimeUp || !inRound) 
			return null;
		outer: for(int tries=0; tries<1000; tries++) {
			Problem pr = new Problem(this, position, round);
			for(Problem p: problems)
				if(p.answeredBy==null && p.answer==pr.answer)
					continue outer;
			pr.save();
			problems.add(pr);
			return pr;
		}
		Logger.error("problem generation failed (after 1000 tries) to generate problem with a unique answer at round "+round);
		return null;
	}
	public void checkRoundTime() {
		if(!started||ended||!inRound)
			return;
		if(roundDuration*1000-(System.currentTimeMillis()-roundStartMillis)>0)
			return;
		roundTimeUp = true;
		save();
	}
	public void checkRoundEnd() {
		if(!started||ended||!inRound)
			return;
		for(Problem pr: problems)
			if(pr.answeredBy==null)
				return;
		Logger.info("round "+round+" ended");
		newRound();
	}
	public void end() {
		Logger.info("game ended");
		ended = true;
		save();
	}
}
