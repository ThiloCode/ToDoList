package server;

public class Session {
	private boolean valid;
	private String userID;
	private String sessionKey;
	
	public Session(String userID, String sessionKey){
		valid = true;
		this.userID = userID;
		this.sessionKey = sessionKey;
	}
	
	public static Session buildInvalidSession(){
		Session invalidSession = new Session("", "");
				invalidSession.invalidateSession();
		return invalidSession;
	}
	
	public boolean isValid(){
		return valid;
	}
	
	public void invalidateSession(){
		valid = false;
	}
	
	public String getUserID(){
		return userID;
	}
	
	public String getSessionKey(){
		return sessionKey;
	}
}
