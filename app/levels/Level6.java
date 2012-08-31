package levels;
import problems.ManyNumberAdditionProblem;
import problems.QuestionAnswerPair;


public class Level6 implements Level{
	
	private static int levelNumber = 6;
	private static String description = "Rapid fire addition";
	private static int timeLimit = 50;
	private static int pointsToContinue = 60;
	private static int wrongAnswerPenalty = 3;

	@Override
	public QuestionAnswerPair getNextProblem() {
		double x = Math.random();
		if(x<0.05)
			return new ManyNumberAdditionProblem(1, 2, 8);
		else if(x<0.3)
			return new ManyNumberAdditionProblem(1, 9, 15);
		else if(x<0.7)
			return new ManyNumberAdditionProblem(1, 99, 30);
		else if(x<0.9)
			return new ManyNumberAdditionProblem(10, 199, 35);
		else
			return new ManyNumberAdditionProblem(10, 999, 50);
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
