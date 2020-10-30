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
	
	private static int sessionID = -1;
	
	public static void main(String[] args){
		System.setProperty("javax.net.ssl.trustStore", "ToDoList.truststore");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");
		
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
		
		Application.sessionID = 100;
		
		ApplicationLoader appLoader = new ApplicationLoader();
		appLoader.save(toDoList, Application.sessionID);
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
		ApplicationLoader appLoader = new ApplicationLoader();
		Application.sessionID = appLoader.readSessionID();
		
		ApplicationWebLoader webLoader = new ApplicationWebLoader();
		if(Application.sessionID == -1){
			webLoader.login();
		}else{
			webLoader.download(Application.sessionID);
		}
		
		Application.sessionID = 100;
		toDoList = appLoader.load();
		
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
