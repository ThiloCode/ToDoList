import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
	public static void main(String[] args){
		Application app = new Application();
		app.start();
	}
	
	private boolean quit = false;
	
	private static Scanner userInput = new Scanner(System.in);
	
	private ToDoList toDoList;
	
	public static Scanner getScanner(){
		return userInput;
	}
	
	public void close(){
		quit = true;
		ScreenManager.displayStatusMessage("Saving...");
		save(toDoList.getItemList());
	}
	
	public static void save(ArrayList<Item> itemList){
		try{
			FileOutputStream outputStream = new FileOutputStream("Data.txt");
			ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
			
			objectStream.writeObject(itemList);
			objectStream.close();
		}catch(IOException e){
			System.out.println("Saving failed!");
		}
	}
	
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
		BaseSelectionHandler handler = new BaseSelectionHandler(toDoList, this);
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
		
		toDoList = new ToDoList(itemList);
		
		while(!quit){
			ScreenManager.resetScreen(toDoList.getItemList(), "");
			handleBaseSelection();
		};
		
	}
}
