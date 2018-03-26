import java.io.*;
import java.net.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Scanner;

import javax.crypto.*;

public class X509Client {

	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);

		ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
		InputStream in = new FileInputStream("sharanya.cer"); // certificate file
		CertificateFactory certificateFactory = CertificateFactory
				.getInstance("X.509");
		X509Certificate x509Certificate = (X509Certificate) certificateFactory
				.generateCertificate(in);

		Date date = x509Certificate.getNotAfter();//checking the expiration date
		if (date.after(new Date())) {
			System.out.println("The certificate is new and valid from "
					+ x509Certificate.getNotBefore() + " to "
					+ x509Certificate.getNotAfter());
		} else {
			System.out.println("Certificate is expired.");
		}

		try {
			x509Certificate.checkValidity(); //verifying the signature of the server
			System.out.println("Certificate is valid.");
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Content of the Certificate:");// Printing certificate contents
		System.out.println(x509Certificate.toString());
		//The below code will be executed only if the certificate is not expired and signature is verified 
		//Allowing client and server to exchange messages
		System.out.println("Enter the message you want to send to the server:");
		Scanner msg = new Scanner(System.in);
		String message = msg.nextLine();
		//Getting the public key from the server
		RSAPublicKey server_publicKey = (RSAPublicKey) x509Certificate.getPublicKey();
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, server_publicKey);//Enciphering using server public key
		byte[] cipherText = cipher.doFinal(message.getBytes());
		System.out.println("Ciphertext is: " + cipherText);
		out.writeObject(cipherText);
		out.flush();
		out.close();
		in.close();
		s.close();
		msg.close();
	}

}
