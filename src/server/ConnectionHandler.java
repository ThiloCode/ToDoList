package server;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.SSLSocket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import org.bson.Document;

import client.InvalidCommunicationException;
import cryptographyTools.Hasher;
import cryptographyTools.StringGenerator;

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
				
				Session userSession = checkSession(in, out);
				if(userSession.isValid()){
					serveToDoList(in, out, userSession.getUserID());
				}else{
					System.out.println("Session Invalid!");
				}
			}else if(line.equals("LOGIN")){
				System.out.println("Logging in with username and password");
				
				Session userSession = checkLogin(in, out);
				if(userSession.isValid()){
					addNewSession(userSession);
					
					out.println("VALID");
					out.println(userSession.getSessionID());
					serveToDoList(in, out, userSession.getUserID());
				}else{
					out.println("FAIL");
				}
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
	
	public void addNewSession(Session userSession){
		boolean addedSuccessfully = false;
		while(!addedSuccessfully){
			try{
				Database.addNewUserSession(userSession);
				addedSuccessfully = true;
			}catch(DuplicateSessionException e){
				userSession.resetSessionID();
			}
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
	
	public Session checkSession(BufferedReader in, PrintWriter out){
		String sessionID = null;
		try{
			sessionID = in.readLine();
			System.out.println("SessionID: " + sessionID);
			
			Document session = Database.getSession(sessionID);
			if(session == null){
				return Session.buildInvalidSession();
			}else if(checkSessionExpired(session)){
				return Session.buildInvalidSession();
			}
			
			System.out.println("UserID: " + session.getString("userID"));
			
			Session userSession = new Session(session.getString("userID"), sessionID);
			return userSession;
			
		}catch (IOException e) {
			e.printStackTrace();
		}catch(DuplicateSessionException e){
			System.out.println(e);
			if(sessionID != null){
				Database.deleteDuplicateSessions(sessionID);
			}
		}
		return Session.buildInvalidSession();
		
	}
	
	public String receiveMessage(BufferedReader input) throws IOException, InvalidCommunicationException{
		String received = input.readLine();
		if(received == null){
			throw new InvalidCommunicationException("Null", "Non-Null value");
		}else{
			return received;
		}
	}
	
	public Session checkLogin(BufferedReader in, PrintWriter out){
		String userID = null;
		try{
			userID = receiveMessage(in);
			System.out.println("userID: " + userID);
			
			Document userAccount = Database.getUserAccount(userID);
			if(userAccount == null){
				out.println("FAIL");
				return Session.buildInvalidSession();
			}else{
				out.println("VALID");
			}
			
			String hashedPassword = userAccount.getString("hashedPassword");
			String savedSalt = userAccount.getString("salt");
			if(hashedPassword == null || savedSalt == null){
				return Session.buildInvalidSession();
			}
			
			out.println(savedSalt);
			
			String clientSalt = receiveMessage(in);
			System.out.println("Salt used by user: " + clientSalt);
			
			String resultPassword = Hasher.sha256HashHex(hashedPassword + clientSalt);
			String clientPassword = receiveMessage(in);
			System.out.println("Client Password: " + clientPassword);
			System.out.println("Generated Password: " + resultPassword);
			if(resultPassword.equals(clientPassword)){
				Session userSession = new Session(userID, StringGenerator.generateRandomAlphanumericString(64));
				return userSession;
			}else{
				return Session.buildInvalidSession();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}catch(DuplicateUserException e){
			System.out.println(e);
			if(userID != null){
				//Database.deleteDuplicateUsers(userID);
			}
		}catch(InvalidCommunicationException e){
			System.out.println(e);
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("Cannot authenticate logins as hashing algorithm is missing!");
		}
		return Session.buildInvalidSession();
	}
}
