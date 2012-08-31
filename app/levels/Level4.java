package levels;
import problems.AdditionProblem;
import problems.DivisionProblem;
import problems.MultiplicationProblem;
import problems.QuestionAnswerPair;
import problems.SubtractionProblem;


public class Level4 implements Level{
	
	private static int levelNumber = 4;
	private static String description = "Arithmetic with bigger numbers";
	private static int timeLimit = 50;
	private static int pointsToContinue = 55;
	private static int wrongAnswerPenalty = 5;

	@Override
	public QuestionAnswerPair getNextProblem() {
		double x = Math.random();
		if(x<0.3)
			return new AdditionProblem(100, 4999, 13);
		else if(x<0.6)
			return new SubtractionProblem(100, 4999, 14);
		else if(x<0.8)
			return new MultiplicationProblem(30, 99, 30);
		else
			return new DivisionProblem(30, 99, 25);
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