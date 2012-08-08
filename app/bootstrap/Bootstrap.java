package bootstrap;

import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {
	public void doJob() {
		Fixtures.deleteDatabase();
		
		new User("gaylord", null, 0).save();
	}
}
