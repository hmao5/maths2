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
		if(str==null) error("not connected - try refreshing page");
		Long l = Long.parseLong(str);
		User u = User.findById(l);
		if(u==null) error("invalid session id - try refreshing page");
		if(!u.alive) error("session timed out - you haven't polled for updates for a few seconds");
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
		if(game.round!=level) error("out of sync - game not on level "+level);
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
		if(game.round!=level) error("out of sync - game not on level "+level);
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
		if(user.score>=game.pointsToWin) {
			game.newRound();
		}
		Map map = new HashMap();
		map.put("answerStatus", (right?"CORRECT_AND_FIRST":"NOT_CORRECT_AND_FIRST"));
		renderJSON(map);
	}
	
	public static void getUpdate() {
		long time = System.currentTimeMillis();
		User user = getMyUser();
		GameInstance game = user.game;
		for(User pl: game.players) {
			if(pl.lastHeartbeat < time - 60000) {
				pl.alive = false;
				pl.save();
			}
		}
		if(user.alive) {
			user.lastHeartbeat = time;
			user.save();
		}
		renderJSON(new Update(game));
	}
}
