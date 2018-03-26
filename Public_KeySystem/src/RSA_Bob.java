import java.io.*;
//import java.math.BigInteger;
import java.net.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;
public class RSA_Bob {
	public static void main(String[] args) throws Exception {

	    int port = 7999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		
		ObjectOutputStream send_PublicKey = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream receive_PublicKey = new ObjectInputStream(s.getInputStream());
		
		//Generating RSA public and private keys for Bob
		KeyPairGenerator Bob_KeyPair = KeyPairGenerator.getInstance("RSA");
		Bob_KeyPair.initialize(1024, new SecureRandom()); 
	    KeyPair Bob_keys = Bob_KeyPair.genKeyPair();
	    RSAPublicKey Bob_PublicKey = (RSAPublicKey) Bob_keys.getPublic();
	    RSAPrivateKey Bob_PrivateKey = (RSAPrivateKey)Bob_keys.getPrivate();
	    
	    //sending bob's public key to Alice
	    send_PublicKey.writeObject(Bob_PublicKey);
	    
	    //Getting Alice's public key required to decipher the enciphered data
	    RSAPublicKey Alice_PublicKey = (RSAPublicKey) receive_PublicKey.readObject();
	    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	  
	   //Demonstrating RSA-public key system to exchange messages that achieve confidentiality
				  byte[] conf_receivedMessage = (byte[]) receive_PublicKey.readObject();
				  cipher.init(Cipher.DECRYPT_MODE, Bob_PrivateKey);	//Decipher using Bob's private key
   			      byte[] conf_plaintText = cipher.doFinal(conf_receivedMessage);
   			      System.out.println("RSA-Public Key system, Confidentiality Demonstration:");
				  System.out.println("Enciphered using Bob's public key and Deciphered using Bob's private key");
				  System.out.println("	The plaintext is: " + new String(conf_plaintText));
				  
		//Demonstrating RSA-public key system to exchange messages that achieve Integrity/Authentication
				  byte[] auth_receivedMessage = (byte[]) receive_PublicKey.readObject();
				  cipher.init(Cipher.DECRYPT_MODE, Alice_PublicKey);	//Decipher using Alice's private Key
   			      byte[] auth_plaintText = cipher.doFinal(auth_receivedMessage);
   			      System.out.println("RSA-Public Key system, Integrity/Authentication Demonstration:");
				  System.out.println("Enciphered using Alice's private key and Deciphered using Alice's public key");
				  System.out.println("	The plaintext is: " + new String(auth_plaintText));
				  
		//Demonstrating RSA-public key system to exchange messages that achieve both confidentiality and Integrity/Authentication 
				  byte[] temp_auth_receivedMessage = (byte[]) receive_PublicKey.readObject();
				  cipher.init(Cipher.DECRYPT_MODE, Bob_PrivateKey);  	//Authentication
				  cipher.init(Cipher.DECRYPT_MODE, Alice_PublicKey);	//Confidentiality
   			      byte[] conf_auth_plaintText = cipher.doFinal(temp_auth_receivedMessage);
   			      System.out.println("RSA-Public Key system, confidentiality and Integrity/Authentication Demonstration:");
				  System.out.println("Message both enciphered and authenticated using Alice's private key and Bob's public key");
				  System.out.println("Message deciphered using Bob's private key and Alice's public key");
				  System.out.println("	The plaintext is: " + new String(conf_auth_plaintText));
		 server.close();
		}
		
	}
 


