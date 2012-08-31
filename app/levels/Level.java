package levels;
import problems.QuestionAnswerPair;


public interface Level {
	public int getLevelNumber();
	public int getTimeLimit();
	public String getLevelDescription();
	public QuestionAnswerPair getNextProblem();
	public int getPointsToContinue(); 
	public int getWrongAnswerPenalty();
}
