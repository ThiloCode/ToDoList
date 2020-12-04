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
	
	private static String sessionID = null;
	
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
	
	public ToDoList chooseMostRecentList(ToDoList local, ToDoList remote){
		if(local == null && remote == null){
			return null;
		}else if(local == null && remote != null){
			return remote;
		}else if(local != null && remote == null){
			return local;
		}else if(local != null && remote != null){
			if(local.getInitializationDateTime().isAfter(remote.getInitializationDateTime()) || local.getInitializationDateTime().isEqual(remote.getInitializationDateTime())){
				return local;
			}else if(local.getInitializationDateTime().isBefore(remote.getInitializationDateTime())){
				return remote;
			}
		}
		return null;
	}
	
	public void start(){
		ApplicationLoader appLoader = new ApplicationLoader();
		Application.sessionID = appLoader.readSessionID();
		
		ToDoList localToDoList = null;
		ToDoList remoteToDoList = null;
		
		ApplicationWebLoader webLoader = new ApplicationWebLoader();
		if(Application.sessionID.equals("")){
			try {
				Application.sessionID = webLoader.loginWithUsernameAndPassword();
			} catch (NoConsoleException e) {
				e.printStackTrace();
			}
		}else{
			webLoader.loginWithSession(Application.sessionID);
		}
		
		localToDoList = appLoader.load();
		
		toDoList = chooseMostRecentList(localToDoList, remoteToDoList);
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
