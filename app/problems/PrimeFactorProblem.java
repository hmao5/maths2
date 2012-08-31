package problems;


public class PrimeFactorProblem implements QuestionAnswerPair{

	private int pointValue;
	private int a;
	private int b;
	public PrimeFactorProblem(int min, int max, int pointValue) {
		this.pointValue = pointValue;
		a = min + (int)(Math.random()*(max+1-min));
		b = a;
		while(true) {
			boolean flag = true;
			for(int i=2; i<=Math.sqrt(b); i++) {
				if(b%i==0) {
					b/=i;
					flag = false;
					break;
				}
			}
			if(flag) break;
		}
	}
	
	@Override
	public double getAnswer() {
		return b;
	}

	@Override
	public String getProblem() {
		return "What is the largest prime factor of "+a+" ?";
	}

	@Override
	public int getPointValue() {
		return pointValue;
	}

}
