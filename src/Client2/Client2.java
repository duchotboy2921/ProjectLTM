package Client2;
import Server.cRequest;
import Server.cResponse;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Client2 {
	protected static int ServerPort;
	// Can be changes to the actual Server IP Address.
	protected static String ServerIpAddress;
	protected static int ClientPort;
	protected static String ClientName;
	public final static ArrayList<String> filenames = new ArrayList<String>( );  //holds all information 	
	static ServerSocket ClientServerSock;
	static Socket requestClientSoc;
	Socket socket;
	cResponse serverResponse;
	cResponse peerResponse;
	// made request and response public 
	cRequest request;
	cRequest otherPeer;
	
	//boolean peerConversation=false;
	
	public static void main(String[] args) throws IOException
	{
		Client2.ClientPort = 44444;
		Client2.ClientName = "Client2";
		Client2.ServerIpAddress = "Localhost";
		Client2.ServerPort = 45678;
		try 
		{
			peerClientThread2 requestThread = new peerClientThread2();
			new updateThread2(1);
                        requestThread.start();
			
			ClientServerSock = new ServerSocket(ClientPort);
			System.out.println("\n Client listening on "+ClientPort);
			while( true )
			{ //wait for connection
				requestClientSoc=ClientServerSock.accept();
				peerServerThread2 thread=new peerServerThread2(requestClientSoc, ClientName);
				thread.start();
			}
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
		
	public void run() throws IOException 
	{
	}
}
