package cryptographyTools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
	
	public static String convertBytesToHexadecimal(byte[] bytes){
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < bytes.length; i++){
			//bytes are in twos compliment, but we need 1111 1111 = 0xFF, however 1111 1111 = -1 in twos compliment
			String hex = Integer.toHexString(Byte.toUnsignedInt(bytes[i]));
			if(hex.length() == 1){
				hex = "0" + hex;
			}
			builder.append(hex);
		}
		return builder.toString();
	}

	public static String sha256HashHex(String stringToHash) throws NoSuchAlgorithmException{
		MessageDigest hashAlg = MessageDigest.getInstance("SHA-256");
		
		byte[] hash = hashAlg.digest(stringToHash.getBytes());
		
		String result = convertBytesToHexadecimal(hash);
		return result;
	}
}
