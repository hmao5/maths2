package problems;


public class SquareProblem implements QuestionAnswerPair{
	private int pointValue;
	private int a;
	private int b;
	public SquareProblem(int min, int max, int pointValue) {
		this.pointValue = pointValue;
		a = min + (int)(Math.random()*(max+1-min));
		b = a*a;
	}
	
	@Override
	public double getAnswer() {
		return b;
	}

	@Override
	public String getProblem() {
		return a+ " * " + a + " = ?";
	}

	@Override
	public int getPointValue() {
		return pointValue;
	}

}