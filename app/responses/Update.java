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
	
	
	public Update(GameInstance game) {
		allProblems = new HashMap<String, ProblemI>();
		activeProblems = new ProblemI[game.problemsAtOnce];
		for(Problem pr: game.problems) {
			ProblemI t = new ProblemI(pr);
			allProblems.put(t.id+"", t);
			if(pr.answeredBy!=null) {
				activeProblems[pr.position] = t;
			}
		}
		players = new PlayerI[game.maxPlayers];
		for(User pl: game.players) {
			players[pl.position] = new PlayerI(pl);
		}
	}
	
}