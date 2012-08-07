package responses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.GameInstance;
import models.Problem;

public class ProblemI {

	public String question;
	public double answer;
	public long id;
	public long answeredBy;
	
	public ProblemI(Problem pr) {
		question = pr.question;
		answer = pr.answer;
		id = pr.id;
		if(pr.answeredBy!=null)
			answeredBy = pr.answeredBy.id;
		else
			answeredBy = -1;
	}
}
