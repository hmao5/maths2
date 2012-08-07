package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.GameInstance;
import models.Problem;
import models.User;
import play.mvc.Controller;
import responses.PlayerI;
import responses.ProblemI;
import responses.Update;

public class Ajax extends Controller {

	static void renderSuccess() {
		renderText("SUCCESS");
	}
	static User getMyUser() {
		String str = session.get("id");
		if(str==null) error("not connected");
		Long l = Long.parseLong(str);
		User u = User.findById(l);
		if(u==null) error("invalid session id");
		return u;
	}
	
	public static void connect(String playerName) {
		List<GameInstance> games = GameInstance.all().fetch();
		GameInstance game = null;
		int position = 0;
		for(GameInstance g: games) {
			if(!g.started && g.players.size()<g.maxPlayers) {
				game = g;
				position = g.players.size();
				break;
			}
		}
		if(game==null) {
			game = new GameInstance().save();
		}
		User u = new User(playerName, game, position).save();
		if(position==game.maxPlayers-1) {
			game.start();
		}
		session.put("id", u.id);
		Map map = new HashMap();
		map.put("id", u.id);
		renderJSON(map);
	}
	
	public static void ready(int level) {
		User user = getMyUser();
		GameInstance game = user.game;
		if(game.level!=level) error("out of sync - game not on level "+level);
		if(game.inRound) error("round already started");
		user.ready = true;
		user.save();
		boolean flag = true;
		for(User p: game.players) 
			if(!p.ready || !p.alive)
				flag = false;
		if(flag)
			game.startRound();
		renderSuccess();
	}
	public static void unready(int level) {
		User user = getMyUser();
		GameInstance game = user.game;
		if(game.level!=level) error("out of sync - game not on level "+level);
		if(game.inRound) error("round already started");
		user.ready = false;
		user.save();
		renderSuccess();
	}
	
	public static void answer(String ans) {
		if(ans==null) error("null ans");
		User user = getMyUser();
		GameInstance game = user.game;
		Double d = Double.parseDouble(ans.trim());
		boolean right = false;
		for(Problem pr: game.problems) {
			if(pr.answeredBy!=null) 
				continue;
			if(pr.answer == d) {
				pr.answeredBy = user;
				pr.save();
				game.newProblem(pr.position);
				user.score += 1;
				user.save();
				right = true;
			}
		}
		Map map = new HashMap();
		map.put("answerStatus", (right?"CORRECT_AND_FIRST":"NOT_CORRECT_AND_FIRST"));
		renderJSON(map);
	}
	
	public static void getUpdate() {
		User user = getMyUser();
		GameInstance game = user.game;
		
		renderJSON(new Update(game));
	}
}
