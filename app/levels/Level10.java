package levels;
import problems.AdditionProblem;
import problems.ChooseProblem;
import problems.DivisionProblem;
import problems.ManyNumberAdditionProblem;
import problems.MultiplicationProblem;
import problems.PrimeFactorProblem;
import problems.QuestionAnswerPair;
import problems.SquareProblem;
import problems.SubtractionProblem;


public class Level10 implements Level{
	
	private static int levelNumber = 10;
	private static String description = "Smorgasbord";
	private static int timeLimit = 180;
	private static int pointsToContinue = 100;
	private static int wrongAnswerPenalty = 2;

	@Override
	public QuestionAnswerPair getNextProblem() {
		double x = Math.random();
		if(x<0.04)
			return new AdditionProblem(0, 99, 3);
		else if(x<0.08)
			return new AdditionProblem(100, 999, 4);
		else if(x<0.12)
			return new AdditionProblem(1000, 9999, 6);
		else if(x<0.16)
			return new AdditionProblem(10000, 49999, 7);
		else if(x<0.20)
			return new SubtractionProblem(10, 99, 3);
		else if(x<0.24)
			return new SubtractionProblem(100, 999, 4);
		else if(x<0.28)
			return new SubtractionProblem(1000, 9999, 6);
		else if(x<0.32)
			return new SubtractionProblem(10000, 49999, 8);
		else if(x<0.36)
			return new MultiplicationProblem(0, 15, 3);
		else if(x<0.40)
			return new MultiplicationProblem(6, 50, 7);
		else if(x<0.43)
			return new MultiplicationProblem(26, 99, 13);
		else if(x<0.46)
			return new MultiplicationProblem(51, 199, 51, 99, 19);
		else if(x<0.50)
			return new DivisionProblem(1, 15, 3);
		else if(x<0.54)
			return new DivisionProblem(6, 50, 6);
		else if(x<0.57)
			return new DivisionProblem(26, 99, 11);
		else if(x<0.60)
			return new DivisionProblem(51, 199, 17);
		else if(x<0.64)
			return new SquareProblem(1, 25, 3);
		else if(x<0.68)
			return new SquareProblem(26, 49, 6);
		else if(x<0.72)
			return new ChooseProblem(4, 7, 4);
		else if(x<0.76)
			return new ChooseProblem(8, 12, 8);
		else if(x<0.80)
			return new PrimeFactorProblem(200, 999, 10);
		else if(x<0.84)
			return new PrimeFactorProblem(1000, 1999, 16);
		else if(x<0.88)
			return new ManyNumberAdditionProblem(0, 99, 9);
		else if(x<0.92)
			return new ManyNumberAdditionProblem(21, 299, 14);
		else if(x<0.96)
			return new DivisionProblem(1000, 2999, 1, 9, 8);
		else 
			return new DivisionProblem(3000, 9999, 1, 9, 9);
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