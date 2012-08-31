package levels;
import problems.AdditionProblem;
import problems.DivisionProblem;
import problems.MultiplicationProblem;
import problems.QuestionAnswerPair;
import problems.SubtractionProblem;


public class Level1 implements Level{

	private static int levelNumber = 1;
	private static String description = "Basic arithmetic";
	private static int timeLimit = 20;
	private static int pointsToContinue = 50;
	private static int wrongAnswerPenalty = 5;

	@Override
	public QuestionAnswerPair getNextProblem() {
		double x = Math.random();
		if(x<0.4)
			return new AdditionProblem(10, 50, 8);
		else if(x<0.6)
			return new SubtractionProblem(10, 50, 9);
		else if(x<0.85)
			return new MultiplicationProblem(2, 20, 10);
		else
			return new DivisionProblem(2, 20, 10);
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
