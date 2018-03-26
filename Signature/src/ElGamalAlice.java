import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalAlice {
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d) {
		BigInteger y = g.modPow(d, p);
		// System.out.println(y);
		return y;
	}

	private static BigInteger computeK(BigInteger p) {
		SecureRandom rnd = new SecureRandom();
		int numBits = 1024;
		BigInteger k = new BigInteger(numBits, rnd);
		// generate BigInteger numBits=lenth, rnd = source of random number
		BigInteger q = p.subtract(BigInteger.ONE);
		// generate p-1

		while (!k.gcd(q).equals(BigInteger.ONE))
		// To check "BigInteger k is relatively prime to BigInteger p-1
		{
			k = new BigInteger(numBits, rnd);
		}
		// loop BigInteger k = new BigInterger (???); check existing code of
		// this type
		// until k is relatively prime to (p-1)

		return k;
	}

	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k) {
		BigInteger a = g.modPow(k, p);
		// a = g^k mod p
		return a;
	}

	private static BigInteger computeB(String message, BigInteger d,
			BigInteger a, BigInteger k, BigInteger p) {
		BigInteger msg = new BigInteger(message.getBytes()); 
		BigInteger q = p.subtract(BigInteger.ONE);
		BigInteger pnew = q;
		BigInteger x0 = BigInteger.ZERO;
		BigInteger x1 = BigInteger.ONE;
		BigInteger x2 = k;
		BigInteger z, z2, z3;

		while (!x2.equals(BigInteger.ZERO)) {
			z = pnew.divide(x2);
			z2 = pnew.subtract(x2.multiply(z));
			pnew = x2;
			x2 = z2;
			z3 = x0.subtract(x1.multiply(z));
			x0 = x1;
			x1 = z3;
		}

		BigInteger b = x0.multiply(msg.subtract(d.multiply(a))).mod(q);
		// b = ((m-da)/k) mod (p-1)
		return b;
	}

	public static void main(String[] args) throws Exception {
		String message = "The quick brown fox j0umps over the lazy dog.";

		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find
		// out what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int mStrength = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically
															// strong
															// pseudo-random
															// number

		// Create a BigInterger with mStrength bit length that is highly likely
		// to be prime.
		// (The '16' determines the probability that p is prime. Refer to
		// BigInteger documentation.)
		p = new BigInteger(mStrength, 16, mSecureRandom);

		// Create a randomly generated BigInteger of length mStrength-1
		g = new BigInteger(mStrength - 1, mSecureRandom);
		d = new BigInteger(mStrength - 1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now
		// compute the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);

		// send signature
		os.writeObject(a);
		os.writeObject(b);

		s.close();
	}
}