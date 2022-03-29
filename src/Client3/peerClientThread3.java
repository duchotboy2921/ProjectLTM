package Client3;
import Server.RequestType;
import Server.cRequest;
import Server.cResponse;
import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.text.DecimalFormat;
import java.util.*;

public class peerClientThread3 extends Thread {

    static ServerSocket ClientServerSock;
    static Socket requestClientSoc;
    Socket socket;
    cResponse serverResponse;
    cResponse peerResponse;
    cRequest request;
    cRequest otherPeer;
    boolean isRegistered = false;
    Path currentRelativePath = Paths.get("");  // Get current path
    String cpath = currentRelativePath.toAbsolutePath().toString();

    public peerClientThread3() {

    }
    //This function accept the client request and call the respective functions

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if(!isRegistered){
                try {
                    isRegistered = true;
                    Register();  //This function register the files in the client-side at the server-side.
                }catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("\n\n1. Lấy danh sách file");
            System.out.println("2. Tìm kiếm file");
            System.out.println("3. Thoát");

            
            int input = scanner.nextInt();
            if (input == 1) {
                RequestFilelist();   //This function access all the file at the server.   
            } else if (input == 2) {
                RequestPeerList();  // This function access all the peer names and information associated with it from the server.
            } else if (input == 3) {
                System.out.println("Thoát khỏi hệ thống!");
                System.exit(0);
                break;		//This function unregister the files of the particular client at the server-side.
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } // closing server and client socket here, so that I can use same socket for other peer
        }
    }
    //This function register the files in the client-side at the server-side.

    private void Register() throws UnknownHostException {
        System.out.println("*********   Đăng kí thông tin   **************");
        request = new cRequest();
        try {
            request.setIpAddr(InetAddress.getLocalHost());
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setPort(Client3.ClientPort);
        request.setHostName(Client3.ClientName);
        request.setRequestType(RequestType.Register);
        System.out.println("Send Object");

        File folder = new File("C:\\Users\\ducke\\Documents\\NetBeansProjects\\TestLTM\\src\\Client3\\input_files");
//                System.out.println(cpath);
//                File folder = new File(cpath+"\\input_files");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Client3.filenames.add((String) listOfFiles[i].getName());
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        request.setfilenames(Client3.filenames);
        sendObject2Server(request);
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            serverResponse = (cResponse) ois.readObject();
            System.out.println(serverResponse.getMessage());
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //This function unregister the files of the particular client at the server-side.

    private void UnRegister() {
        System.out.println("************    Hủy đăng kí   ***********");
        request = new cRequest();
        try {
            request.setIpAddr(InetAddress.getLocalHost());
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setRequestType(RequestType.Unregister);
        request.setPort(Client3.ClientPort);
        request.setHostName(Client3.ClientName);
        sendObject2Server(request);
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            serverResponse = (cResponse) ois.readObject();
            System.out.println(serverResponse.getMessage());
            //socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //This function access all the file at the server.

    private void RequestFilelist() {
        System.out.println("************    Lấy danh sách file    ***********");
        request = new cRequest();

        try {
            request.setIpAddr(InetAddress.getLocalHost());
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setHostName(Client3.ClientName);
        request.setRequestType(RequestType.RequestAllFile);
        request.setPort(Client3.ClientPort);
        //	System.out.println("IP: "+InetAddress.getLocalHost()+" Name: "+Client.ClientName+" 
        sendObject2Server(request);
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            cRequest rser = (cRequest) ois.readObject();
            System.out.println("List of all files :-");
            for (int i = 0; i < rser.getFilenames().size(); i++) {
                System.out.println((i + 1) + ". " + rser.getFilenames().get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void RequestPeerList() {
        System.out.println("************    Tìm kiếm file    ***********");

        try {
            request.setIpAddr(InetAddress.getLocalHost());
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setHostName(Client3.ClientName);
        request.setPort(Client3.ClientPort);

        request = new cRequest();
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n Enter File name");
        String input = scanner.next();
        request.setFileName(input);
        request.setRequestType(RequestType.RequestPeerList);
        sendObject2Server(request);
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            serverResponse = (cResponse) ois.readObject();
            System.out.println(serverResponse.getMessage());
            System.out.println("All Peer List :-" + serverResponse.getPeerList().size());
            // if(serverResponse.getPeerList()!=null)
            if (serverResponse.getPeerList().size() == 0) {
                System.out.println("Không có peer nào chứa file cần tìm");
            } else {
                // if(serverResponse.getPeerList()!=null)
                for (int i = 0; i < serverResponse.getPeerList().size(); i++) {
                    System.out.println((i + 1) + ". " + serverResponse.getPeerList().get(i).getHostName());
                }
                //socket.close();
                cRequest request1 = new cRequest();
                System.out.println("Chọn client để download :- ");
                Scanner scanner1 = new Scanner(System.in);
                int scan = scanner1.nextInt();
                request1 = serverResponse.getPeerList().get(scan - 1);
                request1.setRequestType(RequestType.RequestFile);
                request1.setFileName(input);
                //start
                long lStartTime = System.nanoTime();
                receiveFile(request1);
                //end
                long lEndTime = System.nanoTime();

                //time elapsed
                long output = lEndTime - lStartTime;
                File file = new File("C:\\Users\\ducke\\Documents\\NetBeansProjects\\TestLTM\\src\\Client3\\input_files\\" + request.getFileName());
                if (file.exists()) {
                    double bytes = file.length();
                    double kilobytes = (bytes / 1024);
                    double megabytes = (kilobytes / 1024);
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    System.out.println("---------------------------------------------------------------------------------");
                    System.out.println("Transfer Time in milliseconds: " + output / 1000000);

//                if (megabytes < 1) {
//                    System.out.println("\nDownloaded file :" + request.getFileName() + " File size : " + df.format(kilobytes) + "KB");
//                } else {
//                    System.out.println("\nDownloaded file :" + request.getFileName() + " File size : " + df.format(megabytes) + "MB");
//                }
//                System.out.println("\nPerformance speed " + df.format((megabytes * 1000000 * 1000) / output) + "MB/sec");
                    System.out.println("---------------------------------------------------------------------------------");
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendObject2Server(cRequest request) {
        try {
            socket = new Socket(Client3.ServerIpAddress, Client3.ServerPort);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendObject2Client(cRequest request, int port) {
        try {
            socket = new Socket(request.getIpAddr(), request.getPort());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    //@SuppressWarnings("resource")
    //This function receive the file and its content from the client which has the reuested file.	
    private int receiveFile(cRequest request) throws UnknownHostException, IOException {
        request.setDownload(true);
        request.setRequestType(RequestType.RequestFile);
        //peerConversation=true;
        System.out.println("sending request to peer");
        sendObject2Client(request, request.getPort());
        System.out.println("request sent");
        FileOutputStream OS = null;
        InputStream IS = null;
        // have to either get file or file not found
        IS = socket.getInputStream();
        OS = new FileOutputStream("C:\\Users\\ducke\\Documents\\NetBeansProjects\\TestLTM\\src\\Client3\\input_files\\" + request.getFileName());
        int bytesRead = 0;
        byte[] b = new byte[512000];
        while ((bytesRead = IS.read(b, 0, b.length)) != -1) {
            OS.write(b, 0, bytesRead);
        }
        OS.close();
        return 0;
    }
}
