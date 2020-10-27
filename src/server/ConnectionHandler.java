package server;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.SSLSocket;
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
			
			String line = in.readLine();
			System.out.println("Read: " + line);
			while(line != null){
				System.out.println(line);
				line = in.readLine();
			}
		}catch(IOException e){
			System.out.println(e);
		}finally{
			
		}
	}
}
