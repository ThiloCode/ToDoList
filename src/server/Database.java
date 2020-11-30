package server;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {
	private static MongoClient mongoClient;
	
	public static void initialize() throws DatabaseNotFoundException{
		System.out.println("Setting up database connection...");
		File config = new File("DatabaseConfiguration.txt");
		try {
			if(!config.createNewFile()){
				FileReader reader = new FileReader(config);
				BufferedReader input = new BufferedReader(reader);
				
				String connectionString = input.readLine();
				if(connectionString == null){
					throw new DatabaseNotFoundException("used because it is empty");
				}else{
					Database.mongoClient = MongoClients.create(connectionString);
					System.out.println("Database setup successfully!");
				}
			}else{
				//if the config file was created then that means it wasn't present initially
				//we don't want an empty config file, so we must remove it
				config.delete();
				throw new DatabaseNotFoundException("found");
			}
		} catch (IOException e) {
			throw new DatabaseNotFoundException("read");
		}
	}
	
	public static void showDBNames(){
		for(String name : mongoClient.listDatabaseNames()){
			System.out.println(name);
		}
	}
	
	private static MongoDatabase getDatabase(){
		return mongoClient.getDatabase("ToDoLists");
	}
	
	private static MongoCollection<Document> getUsers(){
		MongoDatabase db = getDatabase();
		return db.getCollection("Users");
	}
	
	private static MongoCollection<Document> getSessions(){
		MongoDatabase db = getDatabase();
		return db.getCollection("Sessions");
	}
	
	private static MongoCollection<Document> getToDoLists(){
		MongoDatabase db = getDatabase();
		return db.getCollection("ToDoLists");
		
	}
	
	public static void deleteDuplicateSessions(String sessionID){
		MongoCollection<Document> sessions = getSessions();
		sessions.deleteMany(new Document().append("sessionID", sessionID));
	}
	
	public static void deleteExpiredSessions(){
		MongoCollection<Document> sessions = getSessions();
		LocalDate oneWeekAgo = LocalDate.now().minusDays(7L);
	}
	
	public static Document getSession(String sessionID) throws DuplicateSessionException{
		MongoCollection<Document> sessions = getSessions();
		
		FindIterable<Document> query = sessions.find(new Document().append("sessionID", sessionID));
		int count = 0;
		Document session = null;
		for(Document doc : query){
			count++;
			session = doc;
		}
		
		if(count == 1){
			return session;
		}else if(count > 1){
			throw new DuplicateSessionException(sessionID);
		}else{
			return null;
		}
	}
	
	public static Document getUserAccount(String userID) throws DuplicateUserException{
		MongoCollection<Document> users = getUsers();
		
		FindIterable<Document> query = users.find(new Document().append("userID", userID));
		int count = 0;
		Document userAccount = null;
		for(Document doc : query){
			count++;
			userAccount = doc;
		}
		
		if(count == 1){
			return userAccount;
		}else if(count > 1){
			throw new DuplicateUserException(userID);
		}else{
			return null;
		}
	}
	
	public static Document getUserToDoList(String userID) throws DuplicateUserException{
		MongoCollection<Document> toDoLists = getToDoLists();
		
		FindIterable<Document> query = toDoLists.find(new Document().append("userID", userID));
		int count = 0;
		Document toDoListDoc = null;
		for(Document doc : query){
			count++;
			toDoListDoc = doc;
		}
		
		if(count == 1){
			return toDoListDoc.get("toDoList", Document.class);
		}else if(count > 1){
			throw new DuplicateUserException(userID);
		}else{
			return null;
		}
	}
	
	public static void addNewUserSession(Session userSession) throws DuplicateSessionException{
		Document session = getSession(userSession.getSessionID());
		if(session != null){
			throw new DuplicateSessionException(userSession.getSessionID());
		}else{
			MongoCollection<Document> sessions = getSessions();
			Document sessionDocument = new Document();
					 sessionDocument.append("sessionID", userSession.getSessionID());
					 sessionDocument.append("userID", userSession.getUserID());
					 sessionDocument.append("day", LocalDate.now().getDayOfMonth());
					 sessionDocument.append("month", LocalDate.now().getMonthValue());
					 sessionDocument.append("year", LocalDate.now().getYear());
			sessions.insertOne(sessionDocument);
		}
	}
}
