import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
	public static void main(String[] args){
		Application app = new Application();
		app.start();
	}
	
	private boolean quit = false;
	
	private static Scanner userInput = new Scanner(System.in);
	
	public ArrayList<Item> load(){
		try{
			//if the file exists already then we have to load it
			//if not we have to create it for saving later
			File data = new File("Data.txt");
			if(!data.createNewFile()){
				FileInputStream inputStream = new FileInputStream("Data.txt");
				ObjectInputStream objectStream = new ObjectInputStream(inputStream);
				
				ArrayList<Item> itemList = (ArrayList<Item>)objectStream.readObject();
				
				objectStream.close();
				
				return itemList;
			}
		}catch(IOException e){
			System.out.println("Error occurred loading!");
		}catch(ClassNotFoundException e){
			System.out.println("Error occurred loading!");
		}
		
		return null;
	}
	
	public void handleBaseSelection(){
		BaseSelectionHandler handler = new BaseSelectionHandler(this);
		String selection = userInput.nextLine();
		
		try{
			Command handleCommand = handler.handle(selection);
			handleCommand.execute();
		}catch(NoRuleException e){
			//System.out.println(e);
		}
	}
	
	public void start(){
		ArrayList<Item> itemList = load();
		if(itemList == null){
			itemList = new ArrayList<Item>();
		}
		
		ToDoList toDoList = new ToDoList(itemList);
				
		while(!quit){
			ScreenManager.resetScreen(toDoList.getItemList(), "");
			handleBaseSelection();
		};
		
	}
}
