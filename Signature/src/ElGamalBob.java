import java.io.*;
import java.net.*;
import java.math.BigInteger;

public class ElGamalBob
{
	private static boolean verifySignature(	BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message)
	{
		BigInteger r1 = (y.modPow(a, p).multiply(a.modPow(b, p))).mod(p); //ab mod(n) = [(a mod n)(b mod n)]mod n
		BigInteger msg = new BigInteger(message.getBytes());
		BigInteger r2 = g.modPow(msg, p);
				
		return r1.equals(r2);
	}

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());

		// read public key
		BigInteger y = (BigInteger)is.readObject();
		BigInteger g = (BigInteger)is.readObject();
		BigInteger p = (BigInteger)is.readObject();

		// read message
		String message = (String)is.readObject();

		// read signature
		BigInteger a = (BigInteger)is.readObject();
		BigInteger b = (BigInteger)is.readObject();

		boolean result = verifySignature(y, g, p, a, b, message);

		System.out.println(message);

		if (result == true){
			System.out.println("Signature verified.");}
		else
			System.out.println("Signature verification failed.");

		s.close();
	}
}