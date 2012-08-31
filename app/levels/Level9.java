package levels;
import problems.DivisionProblem;
import problems.QuestionAnswerPair;


public class Level9 implements Level{
	
	private static int levelNumber = 9;
	private static String description = "Long division";
	private static int timeLimit = 40;
	private static int pointsToContinue = 50;
	private static int wrongAnswerPenalty = 5;

	@Override
	public QuestionAnswerPair getNextProblem() {
		double x = Math.random();
		if(x<0.1)
			return new DivisionProblem(100, 199, 1, 9, 7);
		else if(x<0.2)
			return new DivisionProblem(200, 499, 1, 9, 10);
		else if(x<0.4)
			return new DivisionProblem(500, 999, 1, 9, 12);
		else if(x<0.6)
			return new DivisionProblem(1000, 1999, 1, 9, 15);
		else if(x<0.8)
			return new DivisionProblem(2000, 9999, 1, 9, 18);
		else if(x<0.9)
			return new DivisionProblem(10000, 19999, 1, 9, 20);
		else
			return new DivisionProblem(20000, 99999, 1, 9, 25);
	}
	
	@Override
	public String getLevelDescription() {
		return description;
	}

	@Override
	public int getLevelNumber() {
		return levelNumber;
	}

	@Override
	public int getTimeLimit() {
		return timeLimit;
	}

	@Override
	public int getPointsToContinue() {
		return pointsToContinue;
	}
	
	@Override
	public int getWrongAnswerPenalty() {
		return wrongAnswerPenalty;
	}

}