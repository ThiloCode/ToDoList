package server;

public class DatabaseNotFoundException extends Exception{
	public DatabaseNotFoundException(String action){
		super("No database configuration file could not be " + action + "!");
	}
}
