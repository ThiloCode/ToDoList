package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Console;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import cryptographyTools.*;

public class ApplicationWebLoader {
	private ToDoList downloadedList = null;
	
	public SSLSocket obtainServerConnection(){
		SSLSocket clientSocket = null;
		try {
			clientSocket = (SSLSocket)SSLSocketFactory.getDefault().createSocket("localhost", 8080);
		}catch(IOException e) {
			ScreenManager.displayStatusMessage("Connection to server failed!");
		}
		
		if(clientSocket != null){
			return clientSocket;
		}else{
			return null;
		}
		
	}
	
	public String getUserName(Console console){
		ScreenManager.displayInlinePrompt("Username: ");
		String userName = console.readLine();
		System.out.println();
		return userName;
	}
	
	public String getPassword(Console console){
		ScreenManager.displayInlinePrompt("Password: ");
		String password = new String(console.readPassword());
		System.out.println();
		return password;
	}
	
	public boolean checkSuccess(BufferedReader input){
		String success;
		try {
			success = receiveMessage(input);
			if(success.equals("VALID")){
				return true;
			}else if(success.equals("FAIL")){
				return false;
			}else{
				throw new InvalidCommunicationException(success, "VALID/FAIL");
			}
		}catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidCommunicationException e) {
			e.printStackTrace();
			System.out.println("Server Error, try another time.");
		}
		return false;
	}
	
	public boolean checkValidUserName(BufferedReader input){
		String success;
		try {
			success = receiveMessage(input);
			if(success.equals("VALID")){
				return true;
			}else if(success.equals("FAIL")){
				return false;
			}else{
				throw new InvalidCommunicationException(success, "VALID/FAIL");
			}
		}catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidCommunicationException e) {
			e.printStackTrace();
			System.out.println("Server Error, try another time.");
		}
		return false;
	}
	
	public String sendSalt(PrintWriter output){
		String mySalt = StringGenerator.generateRandomAlphanumericString(64);
		output.println(mySalt);
		
		return mySalt;
	}
	
	public String receiveSalt(BufferedReader input) throws IOException, InvalidCommunicationException{
		String salt = receiveMessage(input);
		return salt;
	}
	
	public String getHashedPassword(Console console, BufferedReader input, PrintWriter output) throws IOException, InvalidCommunicationException, NoSuchAlgorithmException{
		String serverSalt = receiveSalt(input);
		String userSalt = sendSalt(output);
		String password = getPassword(console);
		String hashedPassword = Hasher.sha256HashHex(password + serverSalt);
			   hashedPassword = Hasher.sha256HashHex(hashedPassword + userSalt);
		return hashedPassword;
	}
	
	public boolean authenticateUsernameAndPassword(Console console, BufferedReader input, PrintWriter output) throws NoSuchAlgorithmException, IOException, InvalidCommunicationException{
		String userName = getUserName(console);
		output.println(userName);
		
		if(!checkSuccess(input)){
			ScreenManager.displayStatusMessage("Invalid Username");
			return false;
		}
		
		String hashedPassword = getHashedPassword(console, input, output);
		output.println(hashedPassword);
		
		if(!checkSuccess(input)){
			ScreenManager.displayStatusMessage("Incorrect Password");
			return false;
		}
		
		return true;
	}
	
	public String receiveMessage(BufferedReader input) throws IOException, InvalidCommunicationException{
		String received = input.readLine();
		if(received == null){
			throw new InvalidCommunicationException("Null", "Non-Null value");
		}else{
			return received;
		}
	}
	
	public void receiveToDoList(BufferedReader input) throws IOException, InvalidCommunicationException{
		String JSON = input.readLine();
		if(JSON.equals("LIST")){
			JSON = input.readLine();
			if(JSON != null){
				System.out.println(JSON);
				downloadedList = new ToDoList(JSON);
			}
		}else{
			throw new InvalidCommunicationException(JSON, "LIST");
		}
	}
	
	public String loginWithUsernameAndPassword() throws NoConsoleException{
		SSLSocket connection = null;
		try {
			connection = obtainServerConnection();
			if(connection == null){
				return "";
			}
					
			PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			Console console = System.console();
			if(console == null){
				throw new NoConsoleException();
			}
			
			output.println("LOGIN");
			
			if(!authenticateUsernameAndPassword(console, input, output)){
				return "";
			}
			
			String sessionID = receiveMessage(input);
			
			receiveToDoList(input);
			
			return sessionID;
		}catch (IOException e) {
			System.out.println(e);
			System.out.println("Server Connection Failed! Loading from file...");
		}catch(InvalidCommunicationException e){
			e.printStackTrace();
			System.out.println("Server Error: try another time.");
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			System.out.println("Cannot connect as hashing algorithm is not implemented!");
		}
		finally{
			if(connection != null){
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Unable to close connection with the server!");
				}
			}
		}
		return "";
	}
	
	public boolean loginWithSession(String sessionID){
		SSLSocket connection = null;
		try {
			connection = obtainServerConnection();
			if(connection == null){
				return false;
			}
			
			PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			output.println("SESSION");
			output.println(sessionID);
			
			receiveToDoList(input);
			return true;
		}catch (IOException e) {
			System.out.println(e);
			System.out.println("Server Connection Failed! Loading from file...");
		}catch(InvalidCommunicationException e){
			System.out.println(e);
			System.out.println("Server Error, try another time.");
		}finally{
			if(connection != null){
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Unable to close connection with the server!");
				}
			}
		}
		return false;
	}
	
	public ToDoList getDownloadedList(){
		return downloadedList;
	}
}
