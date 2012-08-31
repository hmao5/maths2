package problems;


public class ChooseProblem implements QuestionAnswerPair{

	private int pointValue;
	private int a;
	private int b;
	private int c;
	public ChooseProblem(int min, int max, int pointValue) {
		this.pointValue = pointValue;
		a = min + (int)(Math.random()*(max+1-min));
		b = (int)randDist(a+1);
		double t = 1;
		for(int i=0; i<b; i++) {
			t*=(a-i);
			t/=(i+1);
		}
		c = (int)(t+0.5);
	}
	
	private static double randDist(double max) {
		double pow = 2.0; // increase to get more centered values, decrease to get more uniform values
		double t = Math.random()*max-max/2;
		double abs = Math.abs(t);
		double sign = t/abs;
		abs = Math.pow(abs, pow);
		return abs/Math.pow(max/2, pow-1)*sign+max/2;
	}
	
	@Override
	public double getAnswer() {
		return c;
	}

	@Override
	public String getProblem() {
		return "("+a+ " choose " + b + ") = ?";
	}

	@Override
	public int getPointValue() {
		return pointValue;
	}
	
	public static void main(String args[]) {
		int s = 11;
		int ar[] = new int[s];
		for(int n=0; n<10000; n++) 
			ar[(int)randDist(s)]++;
		for(int n=0; n<s; n++) 
			System.out.println(n+": "+ar[n]);
	}

}