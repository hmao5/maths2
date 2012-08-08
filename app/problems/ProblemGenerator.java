package problems;

public class ProblemGenerator {
	public static QuestionAnswerPair generate(int round) {
		
		int a = 1+(int)(Math.random()*(Math.pow(10, round)-1));
		int b = 1+(int)(Math.random()*(Math.pow(10, round)-1));
		
		String question = a+"+"+b;
		double answer = a+b;
		
		return new QuestionAnswerPair(question, answer);
	}
}
