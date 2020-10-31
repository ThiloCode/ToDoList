package server;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.SSLSocket;
import java.time.LocalDate;

import org.bson.Document;
import com.mongodb.client.MongoDatabase;

public class ConnectionHandler implements Runnable{
	private SSLSocket clientSocket;
	
	public ConnectionHandler(SSLSocket clientSocket){
		this.clientSocket = clientSocket;
	}
	
	public void run(){
		BufferedReader in = null;
		PrintWriter out = null;
		
		try{
			InputStreamReader reader = new InputStreamReader(clientSocket.getInputStream());
			in = new BufferedReader(reader);
			
			out = new PrintWriter(clientSocket.getOutputStream());
			
			/*
			MongoDatabase database = Database.getDatabase();
			for(String s : database.listCollectionNames()){
				System.out.println("Thread: " + Thread.currentThread().getId() + " " + s);
			}
			*/
			
			String line = in.readLine();
			line.equals(null);
			if(line.equals("SESSION")){
				System.out.println("Session Authorization");
				//checkSession(in, out);
			}else if(line.equals("LOGIN")){
				checkLogin(in, out);
			}
		}catch(IOException e){
			System.out.println(e);
		}finally{
			
		}
	}
	
	public boolean checkSessionExpired(Document session){
		LocalDate today = LocalDate.now();
		LocalDate sessionCreationDate = LocalDate.of(session.getInteger("year"), session.getInteger("month"), session.getInteger("day"));
		
		if(today.minusDays(7L).isBefore(sessionCreationDate)){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean checkSession(BufferedReader in, PrintWriter out){
		String sessionID = null;
		try{
			sessionID = in.readLine();
			System.out.println("SessionID: " + sessionID);
			
			Document session = Database.getSession(sessionID);
			if(session == null){
				return false;
			}else if(checkSessionExpired(session)){
				return false;
			}
			
			System.out.println("UserID: " + session.getString("userID"));
			
			
		}catch (IOException e) {
			e.printStackTrace();
		}catch(DuplicateSessionException e){
			System.out.println(e);
			if(sessionID != null){
				Database.deleteDuplicateSessions(sessionID);
			}
		}
		return false;
	}
	
	public boolean checkLogin(BufferedReader in, PrintWriter out){
		return false;
	}
}
