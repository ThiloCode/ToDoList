package client;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import commands.Command;
import handlers.BaseSelectionHandler;
import handlers.NoRuleException;

public class Application {
	public static void main(String[] args){
		System.setProperty("javax.net.ssl.trustStore", "ToDoList.keystore");
				
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
	
	public SSLSocket obtainServerConnection(){
		SSLSocket clientSocket = null;
		try {
			clientSocket = (SSLSocket)SSLSocketFactory.getDefault().createSocket("localhost", 8080);
		}catch(IOException e) {
			System.out.println(e);
			System.out.println("Connection to server failed!");
		}
		
		if(clientSocket != null){
			return clientSocket;
		}else{
			return null;
		}
		
	}
	
	public ToDoList download(){
		try {
			SSLSocket connection = obtainServerConnection();
			System.out.print(connection.toString());
			if(connection != null){
				PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
				BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
				output.println("Some text");
			}
		}catch (IOException e) {
			System.out.println(e);
			System.out.println("Server Connection Failed:");
		}
		
		return null;
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
		download();
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
