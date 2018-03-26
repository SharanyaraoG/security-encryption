import java.io.*;
import java.net.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;

public class RSA_Alice {
	public static void main(String[] args) throws Exception {
		String message = "The quick brown fox jumps over the lazy dog.";
	    String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		
		ObjectOutputStream send_publicKey = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream receive_publickey = new ObjectInputStream(s.getInputStream());
		
		//Generating RSA public and private keys for Alice
		KeyPairGenerator Alice_keypair = KeyPairGenerator.getInstance("RSA");
		Alice_keypair.initialize(1024 ,new SecureRandom());
	    KeyPair Alice_keys = Alice_keypair.genKeyPair();
	    RSAPublicKey Alice_publicKey = (RSAPublicKey) Alice_keys.getPublic();
	    RSAPrivateKey Alice_privateKey = (RSAPrivateKey)Alice_keys.getPrivate();
	    //sending Alice's public key to bob
	    send_publicKey.writeObject(Alice_publicKey);
	    //getting bob's public key, we need it while exchanging messages using confidentiality 
	    RSAPublicKey bob_publicKey = (RSAPublicKey) receive_publickey.readObject();
	    Cipher cipherText = Cipher.getInstance("RSA/ECB/PKCS1Padding");	        

	    //Demonstrating RSA-public key system to exchange messages that achieve confidentiality
	    		  cipherText.init(Cipher.ENCRYPT_MODE, bob_publicKey);	//encipher using Bob's public key
   			      byte[] conf_cipherText = cipherText.doFinal(message.getBytes());
				  System.out.println("The ciphertext is: " + conf_cipherText);
				  send_publicKey.writeObject(conf_cipherText);
		//Demonstrating RSA-public key system to exchange messages that achieve Integrity/Authentication			  
				  cipherText.init(Cipher.ENCRYPT_MODE, Alice_privateKey);	//Encipher using Alice's private key
   			      byte[] auth_cipherText = cipherText.doFinal(message.getBytes());
				  System.out.println("The ciphertext is: " + auth_cipherText);
				  send_publicKey.writeObject(auth_cipherText);
		//Demonstrating RSA-public key system to exchange messages that achieve both confidentiality and Integrity/Authentication			  
				  cipherText.init(Cipher.ENCRYPT_MODE, Alice_privateKey);	//authentication 
				  cipherText.init(Cipher.ENCRYPT_MODE, bob_publicKey);	//Confidentiality
   			      byte[] conf_auth_cipherText = cipherText.doFinal(message.getBytes());
				  System.out.println("The ciphertext is: " + conf_auth_cipherText);
				  send_publicKey.writeObject(auth_cipherText);
				  send_publicKey.flush();
				  send_publicKey.close();
		    s.close();
		}
}
