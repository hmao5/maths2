package problems;

import java.lang.reflect.Constructor;

import levels.Level;

public class ProblemGenerator {
	public static QuestionAnswerPair generate(int round) {
		try {
			Class c = Class.forName("levels.Level"+round);
			Constructor cons = c.getConstructor();
			Level l = (Level)cons.newInstance();
			
			return l.getNextProblem();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
