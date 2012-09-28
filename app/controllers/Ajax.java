package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.GameInstance;
import models.Problem;
import models.User;
import play.Logger;
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
	
	public static void connect(String playerName, int desiredPlayers, int roundDuration) {
		List<GameInstance> games = GameInstance.all().fetch();
		GameInstance game = null;
		int position = 0;
		for(GameInstance g: games) {
      if (g.maxPlayers != desiredPlayers) continue;
			if(!g.started && g.players.size()<g.maxPlayers) {
				game = g;
				position = g.players.size();
				break;
			}
		}
		if(game==null) {
			game = new GameInstance(desiredPlayers, roundDuration).save();
		}
		User u = new User(playerName, game, position).save();
		if(position==game.maxPlayers-1) {
			game.start();
		}
		session.put("id", u.id);
		Logger.info(playerName+" connected as player "+u.id);
		Map map = new HashMap();
		map.put("id", u.id);
		map.put("maxPlayers", game.maxPlayers);
		renderJSON(map);
	}
	
	public static void ready(int round) {
		User user = getMyUser();
		GameInstance game = user.game;
		if(game.ended) error("game is already over");
		if(game.round!=round) error("out of sync - game not on round "+round);
		if(game.inRound) error("round already started");
		user.ready = true;
		user.save();
		Logger.info(user.name + " ready for round "+round);
		boolean flag = true;
		for(User p: game.players) 
			if(!p.ready || !p.alive)
				flag = false;
		if(flag) {
			Logger.info("all players ready, starting round "+round);
			game.startRound();
		}
		renderSuccess();
	}
	public static void unready(int round) {
		User user = getMyUser();
		GameInstance game = user.game;
		if(game.ended) error("game is already over");
		if(game.round!=round) error("out of sync - game not on round "+round);
		if(game.inRound) error("round already started");
		user.ready = false;
		user.save();
		Logger.info(user.name + " no longer ready for round "+round);
		renderSuccess();
	}
	
	public static void answer(String ans) {
		if(ans==null) error("null ans");
		User user = getMyUser();
		GameInstance game = user.game;
		if(!game.started) error("game has not started");
		if(!game.inRound) error("not in round yet");
		if(game.ended) error("game has already ended");
		double d = -1;
		try { 
			d = Double.parseDouble(ans.trim()); 
		} catch(Exception e) { error("cannot parse "+ans+" into a double"); }
		Problem problemAnswered = null;
		for(Problem pr: game.problems) {
			if(pr.answeredBy!=null) 
				continue;
			if(pr.answer == d) {
				if(problemAnswered!=null) {
					Logger.error("player's answer answered more than one question - this should never happen");
					error("player's answer answered more than one question - this should never happen");
				}
				pr.answeredBy = user;
				pr.save();
				user.score[game.round-1] += 1;
				user.save();
				problemAnswered = pr;
			}
		}
		Logger.info(user.name + " answered "+ans+(problemAnswered!=null?
				" and got #"+problemAnswered.id:""));
		if(problemAnswered!=null) {
			game.newProblem(problemAnswered.position);
		}
		game.checkRoundEnd();
		Map map = new HashMap();
		map.put("correctAndFirst", problemAnswered!=null);
		if(problemAnswered!=null) 
			map.put("answeredProblemID", problemAnswered.id);
		renderJSON(map);
	}
	
	public static void getUpdate() {
		long time = System.currentTimeMillis();
		User user = getMyUser();
		GameInstance game = user.game;
		for(User pl: game.players) {
			if(pl.alive && pl.lastHeartbeat < time - 5000) {
				pl.alive = false;
				pl.save();
				Logger.info(pl.name + " disconnected");
			}
		}
		if(user.alive) {
			user.lastHeartbeat = time;
			user.save();
		}
		game.checkRoundTime();
		renderJSON(new Update(game));
	}
}
