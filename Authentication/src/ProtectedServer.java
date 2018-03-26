import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer {
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException
	{
		DataInputStream in = new DataInputStream(inStream);
		
		String uname = in.readUTF();
		String pwd = lookupPassword(uname);
		
		long ts1 = in.readLong();
		long ts2 = in.readLong();
		
		double r1 = in.readDouble();
		double r2 = in.readDouble();

		int len = in.readInt();
		byte[] rDigest = new byte [len];
		in.readFully(rDigest);
		
		boolean flag = true;
		byte[] Result1 = Protection.makeDigest(uname, pwd, ts1, r1);
		byte[] cDigest = Protection.makeDigest(Result1, ts2, r2);
		
		flag = MessageDigest.isEqual(rDigest, cDigest); 
		return flag;
	}
	
	protected String lookupPassword(String user)
	{
		return "abc123";
	}
	
	public static void main(String[] args) throws Exception
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		
		ProtectedServer server = new ProtectedServer();
		if (server.authenticate(client.getInputStream()))
			
			System.out.println("Client logged in.");
		else
			System.out.println("Client failed to log in.");
		
		s.close();
	}
}
