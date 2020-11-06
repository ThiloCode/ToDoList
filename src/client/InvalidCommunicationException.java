package client;

public class InvalidCommunicationException extends Exception{
	public InvalidCommunicationException(String message, String expected){
		super("Server sent improper message: " + message + ". Expected: " + expected);
	}
}
