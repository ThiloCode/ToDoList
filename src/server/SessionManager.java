package server;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDate;
public class SessionManager {
	private ConcurrentHashMap<String, LocalDate> activeSessions;
	
	public SessionManager(){
		activeSessions = new ConcurrentHashMap<String, LocalDate>();
	}
}
