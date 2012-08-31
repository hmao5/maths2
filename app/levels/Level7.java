package levels;
import problems.ChooseProblem;
import problems.QuestionAnswerPair;


public class Level7 implements Level{
	
	private static int levelNumber = 7;
	private static String description = "Combinations";
	private static int timeLimit = 40;
	private static int pointsToContinue = 60;
	private static int wrongAnswerPenalty = 7;

	@Override
	public QuestionAnswerPair getNextProblem() {
		double x = Math.random();
		if(x<0.05)
			return new ChooseProblem(3, 4, 7);
		else if(x<0.25)
			return new ChooseProblem(5, 6, 10);
		else if(x<0.75)
			return new ChooseProblem(7, 10, 20);
		else
			return new ChooseProblem(11, 12, 31);
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
