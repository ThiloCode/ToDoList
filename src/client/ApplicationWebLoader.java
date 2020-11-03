package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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
	
	public void login(){
		System.out.println("trying to login!");
	}
	
	public boolean download(String sessionID){
		SSLSocket connection = null;
		try {
			connection = obtainServerConnection();
			if(connection != null){
				//connection.startHandshake();
				
				PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
				BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
				output.println("SESSION");
				output.println(sessionID);
				
				System.out.println("Getting ToDoList using session...");
				
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
