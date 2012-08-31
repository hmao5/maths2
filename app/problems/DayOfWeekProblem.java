package problems;

import java.util.*;



public class DayOfWeekProblem implements QuestionAnswerPair{
	private int pointValue;
	private int year;
	private int month1;
	private int month2;
	private int date1;
	private int date2;
	private int day1;
	private int day2;
	private static String[] dayNames = new String[] {"BLEARGH", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	public DayOfWeekProblem(int min, int max, int pointValue) {
		this.pointValue = pointValue;
		if(max>11) max = 11;
		if(min<0) min = 0;
		if(min>max || min>11 || max<0) { min = 0; max = 11; }
		year = 1973+(int)(Math.random()*25);
		do {
		month1 = (int)(Math.random()*12);
		month2 = month1+((int)(Math.random()*2)*2-1)*(min+(int)(Math.random()*(max+1-min)));
		} while(month2<0||month2>11);
		date1 = (int)(Math.random()*28)+1;
		date2 = (int)(Math.random()*28)+1;
		Calendar c1 = new GregorianCalendar(year, month1, date1);
		Calendar c2 = new GregorianCalendar(year, month2, date2);
		day1 = c1.get(Calendar.DAY_OF_WEEK);
		day2 = c2.get(Calendar.DAY_OF_WEEK);
		month1++;
		month2++;
		year%=100;
	}
	

	
	@Override
	public double getAnswer() {
		return day2-1;
	}

	@Override
	public String getProblem() {
		return "If "+month1+"/"+date1+"/"+year+" was a " +dayNames[day1]+", then which day was "+month2+"/"+date2+"/"+year+"?";
	}

	@Override
	public int getPointValue() {
		return pointValue;
	}

	public static void main(String args[]) {
		DayOfWeekProblem p = new DayOfWeekProblem(0,2, 5);
		System.out.println(p.getProblem());
		System.out.println(p.getAnswer());
	}
}
