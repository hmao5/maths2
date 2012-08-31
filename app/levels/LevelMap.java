package levels;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class LevelMap {

	ArrayList<Level> levels;
	public LevelMap() {
		levels = new ArrayList<Level>();
		levels.add(new Level1()); // add a dummy level 0
		try {
			int n = 1;
			while(true) {
				Class c = Class.forName("levels.Level"+n);
				Constructor cons = c.getConstructor();
				Level l = (Level)cons.newInstance();
				levels.add(l);
				n++;
			}
		} catch (ClassNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Level getLevel(int n) {
		return levels.get(n);
	}
	public int getNumLevels() {
		return levels.size()-1;
	}
}
