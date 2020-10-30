package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import javax.net.ssl.*;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;

import client.Application;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;
public class ToDoListServer{
	private final int PORT_NUMBER = 8080;
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
		System.setProperty("javax.net.ssl.keyStore", "C:/Users/Thilo/workspace/To Do List/ToDoList.keystore");
		System.setProperty("javax.net.ssl.keyStorePassword", "password");

		ToDoListServer server = new ToDoListServer();
		server.start();
	}	
	
	public void start() throws IOException, NoSuchAlgorithmException{
		System.out.println("Listening for connections on port: " + PORT_NUMBER);
		
		SSLServerSocket serverSocket = null;
		try{
			serverSocket = (SSLServerSocket)SSLServerSocketFactory.getDefault().createServerSocket(PORT_NUMBER);
			while(true){
				SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
				System.out.println("Connection accepted from " + clientSocket.getInetAddress());
				
				ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket);
				Thread connectionThread = new Thread(connectionHandler);
				connectionThread.start();
			}	
		}catch(IOException e){
			System.out.println(e);
		}finally{
			if(serverSocket != null){
				serverSocket.close();
			}
		}
		
	}
}
