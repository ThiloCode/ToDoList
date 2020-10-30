package client;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import client.AchievementStatistics;
import client.Item;
import client.ToDoList;

public class ApplicationLoader {
	
	public ObjectInputStream loadFile(String filename){
		FileInputStream inputStream = null;
		ObjectInputStream objectStream = null;
		try{
			//if the file exists already then we have to load it
			//if not we have to create it for saving later
			File data = new File(filename);
			if(!data.createNewFile()){
				inputStream = new FileInputStream(filename);
				objectStream = new ObjectInputStream(inputStream);
			}
		}catch(IOException e){
			System.out.println("Error occurred while opening crucial files!");
			System.out.println(e);
			e.printStackTrace();
		}
		return objectStream;
	}
	
	public int readSessionID(){
		ObjectInputStream objectStream = loadFile("session.txt");
		
		int sessionID = -1;
		if(objectStream != null){
			try{
				sessionID = objectStream.readInt();
			}catch(IOException e){
				System.out.println("No session found!");
			}finally{
				try {
					objectStream.close();
				} catch (IOException e) {
					System.out.println("Unable to close input stream!");
				}
			}
		}
		return sessionID;
	}
	
	public void save(ToDoList toDoList, int sessionID){
		try{
			FileOutputStream outputStream = new FileOutputStream("Data.txt");
			ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
			
			objectStream.writeObject(toDoList.getItemList());
			objectStream.writeObject(toDoList.generateAchievementStatistics());
			objectStream.writeObject(toDoList.getInitializationDate());
			
			objectStream.close();
			
			
			outputStream = new FileOutputStream("session.txt");
			objectStream = new ObjectOutputStream(outputStream);
			
			objectStream.writeInt(sessionID);
			
			objectStream.close();
		}catch(IOException e){
			System.out.println("Saving failed!");
		}
	}
	
	public ToDoList load(){
		ObjectInputStream objectStream = loadFile("Data.txt");
		if(objectStream != null){
			try{
				ArrayList<Item> itemList = ((ArrayList<Item>)objectStream.readObject());
				AchievementStatistics statistics = (AchievementStatistics)objectStream.readObject();
				LocalDate initializationDate = (LocalDate)objectStream.readObject();
				
				return new ToDoList(itemList, statistics, initializationDate);
			}catch(IOException e){
				System.out.println("Error occurred loading!");
				return null;
			}catch(ClassNotFoundException e){
				System.out.println("Error occurred loading! File May be Corrupted!");
				return null;
			}finally{
				try {
					objectStream.close();
				} catch (IOException e) {
					System.out.println("Unable to close input stream!");
				}
			}
		}else{
			System.out.println("No To-Do-List Found! Creating a new one...");
			return null;
		}
	}
}