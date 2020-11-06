package client;

public class NoConsoleException extends Exception{
	public NoConsoleException(){
		super("No console was available, login not possible!");
	}
}
