package problems;


public class SubtractionProblem implements QuestionAnswerPair{

	private int pointValue;
	private int a;
	private int b;
	private int c;
	public SubtractionProblem(int min, int max, int pointValue) {
		this.pointValue = pointValue;
		a = min + (int)(Math.random()*(max+1-min));
		b = min + (int)(Math.random()*(max+1-min));
		c = a+b;
	}
	
	public SubtractionProblem(int min1, int max1, int min2, int max2, int pointValue) {
		this.pointValue = pointValue;
		a = min1 + (int)(Math.random()*(max1+1-min1));
		b = min2 + (int)(Math.random()*(max2+1-min2));
		c = a+b;
	}
	
	@Override
	public double getAnswer() {
		return a;
	}

	@Override
	public String getProblem() {
		return c+ " - " + b + " = ?";
	}

	@Override
	public int getPointValue() {
		return pointValue;
	}


}
