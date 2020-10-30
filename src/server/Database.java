package server;

import org.bson.Document;

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
	
	public static MongoDatabase getDatabase(){
		return mongoClient.getDatabase("ToDoLists");
	}
	
	public static MongoCollection<Document> getToDoLists(){
		MongoDatabase db = getDatabase();
		return db.getCollection("ToDoLists");
		
	}
}
