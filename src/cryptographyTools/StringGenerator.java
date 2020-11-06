package cryptographyTools;

import java.security.SecureRandom;

public class StringGenerator {
	private static SecureRandom random = new SecureRandom();
	
	public static String generateRandomAlphanumericString(int length){
		StringBuilder result = new StringBuilder(64);
		int randomDigit = 0;
		int randomUpperCase = 0;
		int randomLowerCase = 0;
		int choice = 0;
		for(int i = 0; i < length; i++){
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
