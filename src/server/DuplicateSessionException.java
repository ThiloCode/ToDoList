package server;

public class DuplicateSessionException extends Exception{
	public DuplicateSessionException(String sessionID){
		super("There are multiple sessions stored in the database with ID: " + sessionID);
	}
}
