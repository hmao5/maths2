package controllers;

import play.mvc.Controller;

public class Ajax extends Controller {

	public static void getNextQuestion() {
		renderText("1+1=2");
	}
}
