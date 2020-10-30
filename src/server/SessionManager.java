package server;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDate;
public class SessionManager {
	private static SecureRandom random = new SecureRandom();
	
	private ConcurrentHashMap<String, LocalDate> activeSessions;
	
	public SessionManager(){
		activeSessions = new ConcurrentHashMap<String, LocalDate>();
	}
	
	public static String generateSession(){
		StringBuilder result = new StringBuilder(64);
		int randomDigit = 0;
		int randomUpperCase = 0;
		int randomLowerCase = 0;
		int choice = 0;
		for(int i = 0; i < 64; i++){
			choice = random.nextInt(3);
			
			switch(choice){
			case 0:
				randomDigit = random.nextInt(10);
				result.append(randomDigit);
				break;
			case 1:
				randomUpperCase = random.nextInt(26) + 65;
				result.append((char)(randomUpperCase));
				break;
			case 2:
				randomLowerCase = random.nextInt(26) + 97;
				result.append((char)(randomLowerCase));
				break;
			}
		}
		return result.toString();
	}
}
