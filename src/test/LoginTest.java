package test;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import client.InvalidCommunicationException;
import client.NoConsoleException;
import client.ToDoList;
import cryptographyTools.Hasher;
import cryptographyTools.StringGenerator;

public class LoginTest {
private ToDoList downloadedList = null;

	public static void main(String[] args){
		System.setProperty("javax.net.ssl.trustStore", "ToDoList.truststore");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");
		
		LoginTest test = new LoginTest();
		if(test.login()){
			System.out.println("Login Success!");
		}
	}
	
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
	
	public boolean login(){
		String userName = "1";
		String password = "password";
		System.out.println("trying to login! Using username: " + userName + " password: " + password);
		
		SSLSocket connection = null;
		try {
			connection = obtainServerConnection();
			if(connection == null){
				return false;
			}
					
			PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			output.println("LOGIN");
			
			
			output.println(userName);
			
			if(!checkSuccess(input)){
				System.out.println("Invalid Username");
				return false;
			}
			
			String serverSalt = receiveSalt(input);
			System.out.println("Server Salt: " + serverSalt);
			String userSalt = sendSalt(output);
			String hashedPassword = Hasher.sha256HashHex(password + serverSalt);
				   hashedPassword=  Hasher.sha256HashHex(hashedPassword + userSalt);
			output.println(hashedPassword);
			System.out.println("Password Generated: " + password);
			
			if(!checkSuccess(input)){
				System.out.println("Incorrect Password");
				return false;
			}
			
			return true;
			
		}catch (IOException e) {
			System.out.println(e);
			System.out.println("Server Connection Failed! Loading from file...");
		}catch(InvalidCommunicationException e){
			e.printStackTrace();
			System.out.println("Server Error: try another time.");
			return false;
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			System.out.println("Cannot connect as hashing algorithm is not implemented!");
			return false;
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
