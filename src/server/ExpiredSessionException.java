package server;

public class ExpiredSessionException extends Exception{
	public ExpiredSessionException(String sessionID){
		super("The session with sessionID: " + sessionID + " has expired.");
	}
}
