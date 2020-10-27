package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import javax.net.ssl.*;
public class ToDoListServer{
	private final int PORT_NUMBER = 8080;
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
		System.setProperty("javax.net.ssl.trustStore", "ToDoList.keystore");
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
