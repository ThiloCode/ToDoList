package client;

public class AuthenticationFailureException extends Exception{
	public AuthenticationFailureException(){
		super("Authentication failed!");
	}
}
