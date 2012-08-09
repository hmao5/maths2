package responses;

import java.util.HashMap;
import java.util.Map;

import models.GameInstance;
import models.Problem;
import models.User;

public class Update {
	public Map<String, ProblemI> allProblems;
	public ProblemI[] activeProblems;
	public PlayerI[] players;
	public int roundNum;
	public boolean roundStarted;
	public int roundTimerMillis;
	public boolean gameStarted;
	public boolean gameEnded;
	
	public Update(GameInstance game) {
		allProblems = new HashMap<String, ProblemI>();
		activeProblems = new ProblemI[game.problemsAtOnce];
		for(Problem pr: game.problems) {
			ProblemI t = new ProblemI(pr);
			allProblems.put(t.id+"", t);
			if(pr.answeredBy==null) {
				activeProblems[pr.position] = t;
			}
		}
		players = new PlayerI[game.maxPlayers];
		for(User pl: game.players) {
			players[pl.position] = new PlayerI(pl);
		}
		roundNum = game.round;
		roundStarted = game.inRound;
		gameStarted = game.started;
		gameEnded = game.ended;
		roundTimerMillis = Math.max(0,(int)(game.roundTimeLimit*1000-
				(System.currentTimeMillis()-game.roundStartMillis)));
	}
	
}
