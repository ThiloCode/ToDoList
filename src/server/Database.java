package server;

import java.time.LocalDate;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {
	private static MongoClient mongoClient = MongoClients.create("mongodb+srv://" +
	"###USERNAME###" +
	"###PASSWORD###" + 
	"@todolisttestcluster.fo3tk.mongodb.net/?retryWrites=true&w=majority&ssl=true");
	
	public static void showDBNames(){
		for(String name : mongoClient.listDatabaseNames()){
			System.out.println(name);
		}
	}
	
	private static MongoDatabase getDatabase(){
		return mongoClient.getDatabase("ToDoLists");
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
}
