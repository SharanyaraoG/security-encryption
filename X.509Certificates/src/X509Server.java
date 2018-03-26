import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class X509Server {

	public static void main(String[] args) throws Exception 
	{   
		
		String aliasName="sharanya";	//short name of the key pair in the key store
        char[] password="sharanya".toCharArray();	//password to the content of key store and key pair
		
        int port = 7999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		ObjectInputStream in = new ObjectInputStream(s.getInputStream());
		
        KeyStore ks = KeyStore.getInstance("jks");
        ks.load(new FileInputStream("keystore.jks"), password);//key store file 
        PrivateKey Server_PrivateKey = (PrivateKey)ks.getKey(aliasName, password);//Getting the server's private key from keystore 
       
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] in1 = (byte[]) in.readObject();
		cipher.init(Cipher.DECRYPT_MODE, Server_PrivateKey);//Decrypting the message using servers private key
		byte[] plaintText = cipher.doFinal(in1);
		System.out.println("Plaintext After Deciphering is(Message received from client): " + new String(plaintText));
		server.close();
	}

}
