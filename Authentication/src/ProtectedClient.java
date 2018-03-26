import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;

public class ProtectedClient {
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException
	{
		DataOutputStream out = new DataOutputStream(outStream);
		
		Date date = new Date();
		long ts1 = date.getTime();
		long ts2 = date.getTime();
				
		double r1 = Math.random();
		double r2 = Math.random();
		
		byte[] digest1 = Protection.makeDigest(user, password, ts1, r1);
		byte[] digest2 = Protection.makeDigest(digest1, ts2, r2);
				
		out.writeUTF(user); 
		out.writeLong(ts1);
		out.writeLong(ts2);
		out.writeDouble(r1);
		out.writeDouble(r2);
		out.writeInt(digest1.length); 
		out.write(digest2);
		
		out.flush();
	}
	
	public static void main(String[] args) throws Exception
	{
		String host = "127.0.0.1";
		int port = 7999;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);
		
		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());
		
		s.close();
	}

}
