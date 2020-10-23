package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class ToDoListServer{
	private final int PORT_NUMBER = 8080;
	public static void main(String[] args) throws IOException{
		ToDoListServer server = new ToDoListServer();
		server.start();
	}
	
	public void start() throws IOException{
		System.out.println("Listening for connections on port: " + PORT_NUMBER);
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(8080);
			while(true){
				Socket clientSocket = serverSocket.accept();
				
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
