package client;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import commands.Command;
import handlers.BaseSelectionHandler;
import handlers.NoRuleException;

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
		save(toDoList);
	}
	
	public static void save(ToDoList toDoList){
		try{
			FileOutputStream outputStream = new FileOutputStream("Data.txt");
			ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
			
			objectStream.writeObject(toDoList.getItemList());
			objectStream.writeObject(toDoList.generateAchievementStatistics());
			objectStream.writeObject(toDoList.getInitializationDate());
			
			objectStream.close();
		}catch(IOException e){
			System.out.println("Saving failed!");
		}
	}
	
	public ToDoList load(){
		try{
			//if the file exists already then we have to load it
			//if not we have to create it for saving later
			File data = new File("Data.txt");
			if(!data.createNewFile()){
				FileInputStream inputStream = new FileInputStream("Data.txt");
				ObjectInputStream objectStream = new ObjectInputStream(inputStream);
				
				ArrayList<Item> itemList = (ArrayList<Item>)objectStream.readObject();
				AchievementStatistics statistics = (AchievementStatistics)objectStream.readObject();
				LocalDate initializationDate = (LocalDate)objectStream.readObject();
				
				objectStream.close();
				
				return new ToDoList(itemList, statistics, initializationDate);
			}else{
				System.out.println("No To-Do-List Found! Creating a new one...");
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
		toDoList = load();
		
		if(toDoList == null){
			toDoList = new ToDoList();
		}
		
		Thread newDayChecker = new NewDayChecker(toDoList);
		newDayChecker.start();
		
		while(!quit){
			//newDayChecker.notify();
			ScreenManager.resetScreen(toDoList.getItemList(), "", toDoList.generateAchievementStatistics());
			handleBaseSelection();
		};
		
		newDayChecker.interrupt();
	}
}
