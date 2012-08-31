package levels;
import problems.AdditionProblem;
import problems.DivisionProblem;
import problems.MultiplicationProblem;
import problems.QuestionAnswerPair;
import problems.SubtractionProblem;


public class Level2 implements Level{

	private static int levelNumber = 2;
	private static String description = "More arithmetic";
	private static int timeLimit = 30;
	private static int pointsToContinue = 50;
	private static int wrongAnswerPenalty = 5;

	@Override
	public QuestionAnswerPair getNextProblem() {
		double x = Math.random();
		if(x<0.4)
			return new AdditionProblem(100, 499, 10);
		else if(x<0.6)
			return new SubtractionProblem(100, 499, 13);
		else if(x<0.85)
			return new MultiplicationProblem(16, 60, 27);
		else
			return new DivisionProblem(20, 60, 23);
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
