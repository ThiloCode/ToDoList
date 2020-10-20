import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ScreenManager {
	
	public static void clearScreen(){
		try{
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		}catch(Exception e){
			System.out.println("Huge Problem!");
		}
	}
	
	public static void displayStatistics(AchievementStatistics statistics){
		System.out.println("---------------------------------------");
		System.out.println("Tasks Created: " + statistics.tasksCreated + "\t Achieved Today: " + statistics.achievedToday + "\t Achieved All-Time: " + statistics.achievedAllTime);
		System.out.println("---------------------------------------");
	}
	
	
	public static void displayItems(ArrayList<Item> itemList){
		System.out.println("_______________________________________");
		System.out.println("---------------------------------------");
		for(int i = 0; i < itemList.size(); i++){
			System.out.println(itemList.get(i));
			System.out.println("---------------------------------------");
		}
	}
	
	public static void displayStatusMessage(String status){
		if(!(status == null) && !status.equals("")){
			System.out.println();
			System.out.println("_______________________________________");
			System.out.println();
			System.out.println(status);
			System.out.println("_______________________________________");
			System.out.println();
			
			//wait briefly to show the status so that the screen cannot be reset before it is seen
			try {
				TimeUnit.SECONDS.sleep((long)(1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void displayBlock(String block){
		System.out.println(block);
	}
	
	public static void resetScreen(ArrayList<Item> itemList, String status){
		
	}
	
	public static void resetScreen(ArrayList<Item> itemList, String status, AchievementStatistics statistics){
		clearScreen();
		displayStatistics(statistics);
		displayItems(itemList);
		displayStatusMessage(status);
	}
}
