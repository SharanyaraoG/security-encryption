import java.io.*;
import java.net.*;

import javax.crypto.*;

public class CipherClient
{
    public static void main(String[] args) throws Exception 
    {
    	//Generating DES key
    	KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
        SecretKey desKey = keygenerator.generateKey();

        //storing the key in a file key.txt
        ObjectOutputStream writeFile = new ObjectOutputStream(new FileOutputStream("Key.txt"));
        writeFile.writeObject(desKey);
        
        //Given code
        String message = "The quick brown fox jumps over the lazy dog.";
		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		
        Cipher cipherText = Cipher.getInstance("DES/ECB/PKCS5Padding");//sending key to server
		CipherOutputStream cout = new CipherOutputStream(s.getOutputStream(), cipherText);
		cipherText.init(Cipher.ENCRYPT_MODE, desKey);//setting cipher object to encrypt mode
		byte plainText[] = message.getBytes();
		System.out.println("plaintext is(Before encryption):" + message);
		System.out.println("ciphertext is(After encryption):" + plainText);
		cout.write(plainText);
		cout.close();
		s.close();
		writeFile.close();
    }
}