import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class CipherServer
{
    public static void main(String[] args) throws Exception 
    {
        int port = 7999;
        ServerSocket server = new ServerSocket(port);
        Socket s = server.accept();
        
        //Reading the key from file, which is generated and stored by client
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("Key.txt"));
        Key desKey = (Key)in.readObject();
        
        // Decrypting the encrypted message sent by client
        Cipher cipherText = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipherText.init(Cipher.DECRYPT_MODE, desKey);	//deciphering using DES key 
		CipherInputStream cin = new CipherInputStream(s.getInputStream(),cipherText);
		int readData = cin.read();
		StringBuilder plainText = new StringBuilder();
		while (readData != -1) {
			plainText.append((char) readData);
			readData = cin.read();
		}
		System.out.println("plaintext after decyprting is: " + plainText.toString());// printing the deciphered text which is plaintext
		in.close();
		cin.close();
		server.close();
	}
}