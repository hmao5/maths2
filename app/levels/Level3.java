package levels;
import problems.QuestionAnswerPair;
import problems.SquareProblem;


public class Level3 implements Level{
	
	private static int levelNumber = 3;
	private static String description = "Squaring";
	private static int timeLimit = 25;
	private static int pointsToContinue = 50;
	private static int wrongAnswerPenalty = 5;

	@Override
	public QuestionAnswerPair getNextProblem() {
		double x = Math.random();
		if(x<0.2)
			return new SquareProblem(1, 10, 5);
		else if(x<0.3)
			return new SquareProblem(11, 16, 8);
		else if(x<0.6)
			return new SquareProblem(17, 30, 12);
		else if(x<0.85)
			return new SquareProblem(31, 50, 20);
		else
			return new SquareProblem(51, 99, 35);
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