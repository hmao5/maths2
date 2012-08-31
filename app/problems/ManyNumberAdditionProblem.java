package problems;


public class ManyNumberAdditionProblem implements QuestionAnswerPair{

	private int pointValue;
	private int ar[];
	private int sum;
	public ManyNumberAdditionProblem(int min, int max, int pointValue) {
		this.pointValue = pointValue;
		ar = new int[5];
		sum = 0;
		for(int n=0; n<5; n++) {
			ar[n] = min + (int)(Math.random()*(max+1-min));
			sum+=ar[n];
		}
	}
	
	@Override
	public double getAnswer() {
		return sum;
	}

	@Override
	public String getProblem() {
		String s = "";
		for(int n=0; n<ar.length; n++) {
			s += ar[n];
			if(n==ar.length-1) s+=" = ?";
			else s+= " + ";
		}
		return s;
	}

	@Override
	public int getPointValue() {
		return pointValue;
	}

}