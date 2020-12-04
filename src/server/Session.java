package server;

public class Session {
	private boolean valid;
	private String userID;
	private String sessionID;
	
	public Session(String userID, String sessionID){
		valid = true;
		this.userID = userID;
		this.sessionID = sessionID;
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
	
	public String getSessionID(){
		return sessionID;
	}
	
	public void resetSessionID(){
		sessionID = cryptographyTools.StringGenerator.generateRandomAlphanumericString(64);
	}
}
