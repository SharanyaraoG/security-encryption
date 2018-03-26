import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Hashing {
	private String hashString(String message, String algorithm)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance(algorithm);
		byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));
		return convertByteArrayToHexString(hashedBytes);
	}

	private static String convertByteArrayToHexString(byte[] arrayBytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < arrayBytes.length; i++) {
			stringBuffer.append(Integer.toString(
					(arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return stringBuffer.toString();
	}

	public static void main(String args[]) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		Hashing hashing = new Hashing();
		String s;
		Scanner input = null;
		try {
			input = new Scanner(System.in);
			System.out.println("Enter a string: ");
			s = input.nextLine();
			String md5Hash = hashing.hashString(s, "MD5");
			System.out.println("MD5 hash of input string is: " + md5Hash);

			String sha1Hash = hashing.hashString(s, "SHA-1");
			System.out.println("SHA-1 hash of input string is: " + sha1Hash);

			String sha256Hash = hashing.hashString(s, "SHA-256");
			System.out.println("SHA-256 of input string is: " + sha256Hash);
		} finally {
			if (input != null)
				input.close();
		}

	}
}
