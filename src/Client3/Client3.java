package Client3;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;
import Server.cResponse;
import Server.cRequest;

public class Client3 {

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
        Client3.ClientPort = 33333;
        Client3.ClientName = "client3";
        Client3.ServerIpAddress = "Localhost";
        Client3.ServerPort = 45678;
        try {
            peerClientThread3 requestThread = new peerClientThread3();
            new updateThread3(1);
            requestThread.start();

            ClientServerSock = new ServerSocket(ClientPort);
            System.out.println("\n Client listening on " + ClientPort);
            while (true) { //wait for connection
                requestClientSoc = ClientServerSock.accept();
                peerServerThread3 thread = new peerServerThread3(requestClientSoc, ClientName);
                thread.start();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void run() throws IOException {
    }
}
