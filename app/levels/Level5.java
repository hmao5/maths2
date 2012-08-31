package levels;
import problems.PrimeFactorProblem;
import problems.QuestionAnswerPair;


public class Level5 implements Level{
	
	private static int levelNumber = 5;
	private static String description = "Largest prime factor";
	private static int timeLimit = 75;
	private static int pointsToContinue = 55;
	private static int wrongAnswerPenalty = 25;

	@Override
	public QuestionAnswerPair getNextProblem() {
		double x = Math.random();
		if(x<0.1)
			return new PrimeFactorProblem(50, 99, 10);
		else if(x<0.3)
			return new PrimeFactorProblem(100, 199, 17);
		else if(x<0.6)
			return new PrimeFactorProblem(200, 499, 23);
		else if(x<0.8)
			return new PrimeFactorProblem(500, 999, 30);
		else
			return new PrimeFactorProblem(1000, 1999, 45);
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
