package server;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.SSLSocket;

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
				checkSession(in, out);
			}else if(line.equals("LOGIN")){
				checkLogin(in, out);
			}
		}catch(IOException e){
			System.out.println(e);
		}finally{
			
		}
	}
	
	public boolean checkSession(BufferedReader in, PrintWriter out){
		try{
			String sessionID = in.readLine();
			System.out.println("SessionID: " + sessionID);
		}catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean checkLogin(BufferedReader in, PrintWriter out){
		return false;
	}
}
