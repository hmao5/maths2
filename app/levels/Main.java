package levels;
import java.util.*;

import problems.QuestionAnswerPair;

import levels.Level;

public class Main {
	public static void main(String args[]) {
		new Main().mainMenu();
	}
	
	public void mainMenu() {
		boolean ragingUnlocked = false;
		while(true) {
			System.out.println("MATHS!: THE MENTAL MATH TRAINER\n");
			System.out.println("Instructions: Answer the questions as quickly and accurately as you can.\n" + 
					"You will get points for correct answers and be penalized for incorrect answers.\n" + 
					"All answers are nonnegative integers.\n" + 
					"Each level has a time limit, in which you must get a certain number of points.\n" +
					"There are ten levels of training, which you will complete in sequence.\n");
			int difficultyChoice = 0;
			do {
				System.out.print("Choose your difficulty mode: \n" +
					"  1) Casual     (0.5x multiplier, 3x slower)\n" + 
					"  2) Practice   (5x slower, fail upon incorrect answer)\n" + 
					"  3) Warmup     (2x slower, no incorrect penalty)\n" + 
					"  4) Easy       (0.5x multiplier)\n" +
					"  5) Normal     \n" + 
					"  6) Hard       (1.5x multiplier)\n" + 
					"  7) Lightning  (2.0x faster, no incorrect penalty)\n" + 
					"  8) Pro        (5.0x multiplier, 2x slower)\n" + 
					"  9) Insane     (3.0x multiplier, fail upon incorrect answer)\n" +
					((ragingUnlocked) ? " 10) Raging     (25.0x multiplier, 6x slower, fail upon incorrect answer)\n" : "") );
				difficultyChoice = getUserAnswer();
			} while(difficultyChoice<0||difficultyChoice>10||(difficultyChoice==10&&!ragingUnlocked));
			System.out.println();
			int score = playGame(difficultyChoice);
			System.out.println();
			if(difficultyChoice==8 || difficultyChoice==9 && score>=0)
				ragingUnlocked = true;
		}
	}
	
	public int playGame(int difficultyChoice) {
		double levelThresholdModifier = (new double[] 	{0.0, 	0.5, 	1.0, 	1.0, 	0.5, 	1.0, 	1.5, 	1.0, 	5.0,	3.0,	25.0})[difficultyChoice];
		double timeLimitModifier = (new double[] 		{1.0, 	3.0, 	5.0, 	2.0, 	1.0, 	1.0, 	1.0, 	0.5,	2.0,	1.0,	6.0}) [difficultyChoice];
		boolean failUponIncorrect = (new boolean[] 		{false, false, 	true, 	false, 	false, 	false, 	false, 	false,	false,	true, 	true}) [difficultyChoice];
		boolean incorrectPenalty = (new boolean[] 		{true, true, 	true, 	false, 	true, 	true, 	true, 	false,	true,	true,	true}) [difficultyChoice];
		
		LevelMap levels = new LevelMap();
		int totalScore = 0;
		int totalAttempted = 0;
		int totalCorrect = 0;
		int totalContinues = 0;
		
		for(int level=1; level<=levels.getNumLevels();level++) {
			Level curLevel = levels.getLevel(level);
			long timeLeft = (int)(curLevel.getTimeLimit()*1000*timeLimitModifier);
			System.out.println("Level " + level+": "+curLevel.getLevelDescription()+".");
			System.out.println("Time limit is "+timeLeft/1000+" seconds.");
			System.out.println((int)(curLevel.getPointsToContinue()*levelThresholdModifier)+" points to pass.");
			if(failUponIncorrect) System.out.println("Must answer all questions correctly.");
			else if(incorrectPenalty) System.out.println("-"+curLevel.getWrongAnswerPenalty()+" points per incorrect answer.");
			else System.out.println("No penalty for incorrect answers.");
			System.out.println("Press enter to begin.");
			getUserConfirm();
			
			int levelScore = 0;
			int levelAttempted = 0;
			int levelCorrect = 0;
			boolean failed = false;
			while(timeLeft > 0) {
				QuestionAnswerPair prob = curLevel.getNextProblem();
				System.out.println(prob.getProblem()+"  ("+prob.getPointValue()+" points)");
				long startTime = System.currentTimeMillis();
				int ans = getUserAnswer();
				long time = (System.currentTimeMillis()-startTime);
				if(ans==-5) {
					break;
				}
				double scoreChange = prob.getPointValue();
				if(timeLeft<time && time>0) scoreChange = scoreChange*timeLeft/time;
				timeLeft-=time;
				if(ans==prob.getAnswer()) {
					levelCorrect++;
					levelAttempted++;
					System.out.println("Correct");
					levelScore+=(int)scoreChange;
				}
				else {
					levelAttempted++;
					System.out.println("Incorrect, the answer was "+prob.getAnswer());
					if(incorrectPenalty)
						levelScore-=curLevel.getWrongAnswerPenalty();
					if(failUponIncorrect) {
						failed = true;
						break;
					}
				}
				
			}
			
			totalAttempted+=levelAttempted;
			totalCorrect+=levelCorrect;
			if(failed) System.out.println("You got an incorrect answer! Your current score for this level: " + levelScore);
			else System.out.println("Time's up! Your score for this level: " + levelScore);
			int levelAccuracy = 0;
			if(levelAttempted>0) levelAccuracy = levelCorrect*100/levelAttempted;
			System.out.println("You attempted " + levelAttempted + " problems and solved " + levelCorrect + " of them. " +
					"Accuracy: " + levelAccuracy + "%");
			if(!failed && levelScore>=(int)(curLevel.getPointsToContinue()*levelThresholdModifier)) {
				totalScore+=levelScore;
				System.out.println("You passed level "+curLevel.getLevelNumber()+".");
				System.out.println();
			} else {
				System.out.println("You failed level "+curLevel.getLevelNumber()+". Press enter to continue...");
				getUserConfirm();
				totalContinues++;
				level--;
			}
		}
		
		
		
		System.out.println("Congratulations! You passed all the levels!");
		if(totalContinues>0) System.out.println("You used a total of "+totalContinues+" continues.");
		int totalAccuracy = 0;
		if(totalAttempted>0) totalAccuracy = totalCorrect*100/totalAttempted;
		System.out.println("You attempted a total of " + totalAttempted + " problems and got " + 
				totalCorrect + " of them right. ");
		System.out.println("Total accuracy: " + totalAccuracy + "%");
		totalScore = (int)(totalScore * Math.pow(0.95, ((double)totalContinues)/2 ) * ((double)totalCorrect)/totalAttempted );
		System.out.println("Total score: "+totalScore);
		System.out.println("Try to do better next time! Press enter to return to main menu.");
		getUserConfirm();
		return totalScore;
	}
	
	public int getUserAnswer() {
		Scanner sc = new Scanner(System.in);
		int ret = -1;
		try {
			String s = sc.nextLine();
			ret = Integer.parseInt(s);
		} catch (Exception e) {}
		return ret;
	}
	public void getUserConfirm() {
		new Scanner(System.in).nextLine();
	}
}
