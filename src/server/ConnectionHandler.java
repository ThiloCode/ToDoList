package server;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.SSLSocket;
import java.time.LocalDate;

import org.bson.Document;

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
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			String line = in.readLine();
			if(line.equals("SESSION")){
				System.out.println("Session Authorization");
				
				String userID = checkSession(in, out);
				if(userID != null){
					serveToDoList(in, out, userID);
				}else{
					
				}
			}else if(line.equals("LOGIN")){
				checkLogin(in, out);
			}
		}catch(IOException e){
			System.out.println(e);
		}finally{
			
		}
	}
	
	public void serveToDoList(BufferedReader in, PrintWriter out, String userID){
		try {
			Document ToDoList = Database.getUserToDoList(userID);
			out.println("LIST");
			out.println(ToDoList.toJson());
			
			System.out.println("Serving List: " + ToDoList.toJson());
		} catch (DuplicateUserException e) {
			e.printStackTrace();
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
	
	public String checkSession(BufferedReader in, PrintWriter out){
		String sessionID = null;
		try{
			sessionID = in.readLine();
			System.out.println("SessionID: " + sessionID);
			
			Document session = Database.getSession(sessionID);
			if(session == null){
				return null;
			}else if(checkSessionExpired(session)){
				return null;
			}
			
			System.out.println("UserID: " + session.getString("userID"));
			return session.getString("userID");
			
		}catch (IOException e) {
			e.printStackTrace();
		}catch(DuplicateSessionException e){
			System.out.println(e);
			if(sessionID != null){
				Database.deleteDuplicateSessions(sessionID);
			}
		}
		return null;
	}
	
	public boolean checkLogin(BufferedReader in, PrintWriter out){
		return false;
	}
}
