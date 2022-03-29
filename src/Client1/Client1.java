package Client1;
import Server.cRequest;
import Server.cResponse;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Client1 {

    protected static int ServerPort;
    // Can be changes to the actual Server IP Address.
    protected static String ServerIpAddress;
    protected static int ClientPort;
    protected static String ClientName;
    public final static ArrayList<String> filenames = new ArrayList<String>();  //holds all information 	
    static ServerSocket ClientServerSock;
    static Socket requestClientSoc;
    Socket socket;
    cResponse serverResponse;
    cResponse peerResponse;
    // made request and response public 
    cRequest request;
    cRequest otherPeer;

    //boolean peerConversation=false;
    public static void main(String[] args) throws IOException {
        System.out.println("Input Format : java Client <Client Port> <Client Name> <Server Ip> <Server port>");
        Client1.ClientPort = 11111;
        Client1.ClientName = "client1";
        Client1.ServerIpAddress = "Localhost";
        Client1.ServerPort = 45678;
        try {
            peerClientThread1 requestThread = new peerClientThread1();
            new updateThread1(1);
            requestThread.start();

            ClientServerSock = new ServerSocket(ClientPort);
            System.out.println("\n Client listening on " + ClientPort);
            while (true) { //wait for connection
                requestClientSoc = ClientServerSock.accept();
                peerServerThread1 thread = new peerServerThread1(requestClientSoc, ClientName);
                thread.start();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void run() throws IOException {
    }
}
