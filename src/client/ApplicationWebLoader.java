package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Console;
import java.util.Scanner;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.bson.Document;

public class ApplicationWebLoader {
	private ToDoList downloadedList = null;
	
	public SSLSocket obtainServerConnection(){
		SSLSocket clientSocket = null;
		try {
			clientSocket = (SSLSocket)SSLSocketFactory.getDefault().createSocket("localhost", 8080);
		}catch(IOException e) {
			System.out.println(e);
			System.out.println("Connection to server failed!");
		}
		
		if(clientSocket != null){
			return clientSocket;
		}else{
			return null;
		}
		
	}
	
	public String convertBytesToHexadecimal(byte[] bytes){
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < bytes.length; i++){
			//bytes are in twos compliment, but we need 1111 1111 = 0xFF, however 1111 1111 = -1 in twos compliment
			String hex = Integer.toHexString(Byte.toUnsignedInt(bytes[i]));
			if(hex.length() == 1){
				hex = "0" + hex;
			}
			builder.append(hex);
		}
		
		return builder.toString();
	}
	
	public String getUserName(Console console){
		System.out.print("Username: ");
		String userName = console.readLine();
		System.out.println();
		return userName;
	}
	
	public String getPassword(Console console){
		System.out.print("Password: ");
		String password = new String(console.readPassword());
		System.out.println();
		return password;
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
		String mySalt = server.SessionManager.generateSession();
		System.out.println("Salt Generated: " + mySalt);
		output.println(mySalt);
		
		return mySalt;
	}
	
	public String receiveSalt(BufferedReader input) throws IOException, InvalidCommunicationException{
		String salt = receiveMessage(input);
		System.out.println("Salt Received: " + salt);
		return salt;
	}
	
	public String receiveMessage(BufferedReader input) throws IOException, InvalidCommunicationException{
		String received = input.readLine();
		if(received == null){
			throw new InvalidCommunicationException("Null", "Non-Null value");
		}else{
			return received;
		}
	}
	
	public String constructHashedPassword(String userSalt, String serverSalt, String password) throws NoSuchAlgorithmException{
		MessageDigest hashAlg = MessageDigest.getInstance("SHA-256");
		
		byte[] hashedPassword = hashAlg.digest((password + serverSalt).getBytes());
		
		String hashedPasswordHex = convertBytesToHexadecimal(hashedPassword);
			   hashedPasswordHex = hashedPasswordHex + userSalt;
		
		hashedPassword = new byte[hashedPasswordHex.length()];
		for(int i = 0; i < hashedPasswordHex.length() - 2; i++){
			//max unsigned byte value = 0xFF, need to parse two characters at a time
			hashedPassword[i] = (byte)Integer.parseInt(hashedPasswordHex.substring(i, i + 2), 16);
		}
		
		hashedPasswordHex = convertBytesToHexadecimal(hashedPassword);
		return hashedPasswordHex;
	}
	
	public boolean login() throws NoConsoleException{
		System.out.println("trying to login!");
		
		SSLSocket connection = null;
		try {
			connection = obtainServerConnection();
			if(connection == null){
				return false;
			}
					
			PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			Console console = System.console();
			if(console == null){
				throw new NoConsoleException();
			}
			
			output.println("LOGIN");
			
			String userName = getUserName(console);
			output.println(userName);
			
			if(!checkValidUserName(input)){
				System.out.println("Invalid Username");
				return false;
			}
			
			String serverSalt;
			try{
				serverSalt = receiveSalt(input);
			}catch(InvalidCommunicationException e){
				e.printStackTrace();
				System.out.println("Server Error: try another time.");
				return false;
			}catch(IOException e){
				e.printStackTrace();
				System.out.println("Server Error: try another time.");
				return false;
			}
			
			String userSalt = sendSalt(output);
			
			String password = getPassword(console);
			try{
				password = constructHashedPassword(userSalt, serverSalt, password);
			}catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				System.out.println("Cannot connect as hashing algorithm is not implemented!");
			}
			output.println(password);
			
			return true;
			
		}catch (IOException e) {
			System.out.println(e);
			System.out.println("Server Connection Failed! Loading from file...");
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
	
	public boolean download(String sessionID){
		SSLSocket connection = null;
		try {
			connection = obtainServerConnection();
			if(connection != null){
				PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
				BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
				output.println("SESSION");
				output.println(sessionID);
				
				String JSON = input.readLine();
				if(JSON.equals("LIST")){
					JSON = input.readLine();
					if(JSON != null){
						System.out.println(JSON);
						downloadedList = new ToDoList(JSON);
						return true;
					}
				}
			}
		}catch (IOException e) {
			System.out.println(e);
			System.out.println("Server Connection Failed! Loading from file...");
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
