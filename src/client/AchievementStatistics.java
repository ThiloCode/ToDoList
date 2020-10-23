package client;
import java.io.Serializable;

public class AchievementStatistics  implements Serializable{
	private static final long serialVersionUID = -4274712607646188673L;
	
	public int tasksCreated = 0;
	public int achievedToday = 0;
	public int achievedAllTime = 0;
	
	public AchievementStatistics(int created, int today, int allTime){
		tasksCreated = created;
		achievedToday = today;
		achievedAllTime = allTime;
	}
}
