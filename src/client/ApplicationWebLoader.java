package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ApplicationWebLoader {
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
	
	public ToDoList download(String sessionID){
		try {
			SSLSocket connection = obtainServerConnection();
			if(connection != null){
				connection.startHandshake();
				
				PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
				BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				output.println("SESSION");
				output.println(sessionID);
			}
		}catch (IOException e) {
			System.out.println(e);
			System.out.println("Server Connection Failed! Loading from file...");
		}
		
		return null;
	}
}
