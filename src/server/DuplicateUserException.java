package server;

public class DuplicateUserException extends Exception{
	public DuplicateUserException(String userID){
		super("There are multiple users stored in the database with ID: " + userID);
	}
}
