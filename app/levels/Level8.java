package levels;
import problems.AdditionProblem;
import problems.DivisionProblem;
import problems.MultiplicationProblem;
import problems.QuestionAnswerPair;
import problems.SubtractionProblem;


public class Level8 implements Level{
	
	private static int levelNumber = 8;
	private static String description = "Large number arithmetic";
	private static int timeLimit = 90;
	private static int pointsToContinue = 70;
	private static int wrongAnswerPenalty = 5;

	@Override
	public QuestionAnswerPair getNextProblem() {
		double x = Math.random();
		if(x<0.15)
			return new AdditionProblem(1000, 49999, 12);
		else if(x<0.3)
			return new SubtractionProblem(1000, 49999, 14);
		else if(x<0.65)
			return new MultiplicationProblem(51, 199, 51, 119, 29);
		else
			return new DivisionProblem(51, 199, 23);
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
