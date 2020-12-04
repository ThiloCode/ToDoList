package server;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDate;
public class SessionManager {
	private ConcurrentHashMap<String, LocalDate> activeSessions;
	
	public SessionManager(){
		activeSessions = new ConcurrentHashMap<String, LocalDate>();
	}
	
	public void addNewSession(Session userSession) throws DuplicateSessionException{
		if(activeSessions.containsKey(userSession.getSessionID())){
			throw new DuplicateSessionException(userSession.getSessionID());
		}else{
			Database.addNewUserSession(userSession);
		}
	}
}
