package server;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class ConnectionHandler implements Runnable{
	private Socket clientSocket;
	
	public ConnectionHandler(Socket clientSocket){
		this.clientSocket = clientSocket;
	}
	
	public void run(){
		BufferedReader in = null;
		PrintWriter out = null;
		
		try{
			InputStreamReader reader = new InputStreamReader(clientSocket.getInputStream());
			in = new BufferedReader(reader);
			
			out = new PrintWriter(clientSocket.getOutputStream());
			
			String line = in.readLine();
			while(line != null){
				System.out.println(line);
			}
		}catch(IOException e){
			
		}finally{
			
		}
	}
}
