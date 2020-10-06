import java.util.ArrayList;

public class ScreenManager {
	
	public static void clearScreen(){
		try{
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		}catch(Exception e){
			System.out.println("Huge Problem!");
		}
	}
	
	public static void displayItems(ArrayList<Item> itemList){
		
	}
	
	public static void displayStatusMessage(String status){
		System.out.println("status");
	}
	
	public static void resetScreen(ArrayList<Item> itemList, String status){
		clearScreen();
		displayItems(itemList);
		displayStatusMessage(status);
	}
}
