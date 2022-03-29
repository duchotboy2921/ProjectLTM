package Server;
import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

public class ServerThread extends Thread {

    Socket ClientSock;
    ObjectInputStream ISR = null;

    public ServerThread(Socket clientSock) {
        this.ClientSock = clientSock;  //constructor stores client sock information from server
    }

    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(ClientSock.getInputStream());
            cRequest request = (cRequest) ois.readObject();
            RequestType requestType = request.getRequestType();

            if (requestType == RequestType.Register) //clients Register its information to server
            {
                //Inserting request into Array
                boolean repeatRequest = false;
                for (int i = 0; i < Server.peerList.size(); i++) {
                    cRequest print = Server.peerList.get(i);
                    if (print.getPort() == request.getPort()) {
                        repeatRequest = true;
                        break;
                    }
                }
                if (repeatRequest) // if client is already registered with server
                {
                    cResponse response = new cResponse();
                    response.setSuccess(false);
                    if (!response.isSuccess()) {
                        System.out.println("Host :- " + request.getHostName() + " IP:" + request.getIpAddr() + " Port:" + request.getPort() + "    Already Registered!");
                        response.setMessage("Peer này đã được đăng kí");
                    }
                    response.setPeerList(null);
                    sendObject(response, true);
                } else // if client is not registered with client it register client info with all filename to server
                {
                    Server.peerList.add(request);
                    cResponse response = new cResponse();
                    response.setSuccess(true);
                    if (response.isSuccess()) {
                        System.out.println("Host :- " + request.getHostName() + " IP:" + request.getIpAddr() + " Port:" + request.getPort() + "  Successfully Registered!");
                        response.setMessage("Đăng kí thông tin thành công!");
                    } else {
                        response.setMessage("Đăng kí thất bại, thử lại");
                    }
                    response.setPeerList(null);
                    sendObject(response, true);
                }
            } else if (requestType == RequestType.RequestPeerList) // request all peer list who contains requested file
            {
                System.out.println("Host :- " + request.getHostName() + " IP:" + request.getIpAddr() + " Port:" + request.getPort() + "  File cRequest :" + request.getFileName());
                ArrayList<cRequest> peerList1 = new ArrayList<cRequest>();
                peerList1.clear();
                System.out.println("Name :-" + request.getFileName());
                for (int i = 0; i < Server.peerList.size(); i++) {
                    cRequest print = Server.peerList.get(i);
                    for (int j = 0; j < print.getFilenames().size(); j++) {
                        int flag = 0;
                        if (request.getFileName().equals(print.getFilenames().get(j))) {
                            flag = 1;
                        }
                        if (flag == 1) {
                            peerList1.add(Server.peerList.get(i));
                            break;
                        }
                    }
                }
                cResponse response = new cResponse();
                response.setMessage("PeerList Received");
                response.setSuccess(true);
                System.out.println(peerList1.size());
                response.setPeerList(peerList1);
                sendObject(response, true);
            } else if (requestType == RequestType.Unregister) // unregister from server database
            {
                cResponse response = new cResponse();
                response.setSuccess(unregisterClient(request.getPort()));
                if (response.isSuccess()) {
                    System.out.println("Host :- " + request.getHostName() + " IP:" + request.getIpAddr() + " Port:" + request.getPort() + "   Unregistered Successfully");
                    response.setMessage("Hủy đăng kí thành công!");
                } else {
                    response.setMessage("Bạn chưa đăng kí thông tin!");
                }
                sendObject(response, true);
            } else if (requestType == RequestType.RequestUpdate) {
                unregisterClient(request.getPort());
                Server.peerList.add(request);
                for (int j = 0; j < request.getFilenames().size(); j++) {
                    System.out.println(request.getFilenames().get(j));
                }
                System.out.println("Host :- " + request.getHostName() + " IP:" + request.getIpAddr() + " Port:" + request.getPort() + "  updated Successfully");
            } else if (requestType == RequestType.RequestAllFile) {
                System.out.println("Host :- " + request.getHostName() + " IP:" + request.getIpAddr() + " Port:" + request.getPort() + "   All File cRequest ");
                cRequest request1 = new cRequest();
                Server.filenames.removeAll(Server.filenames);    // clear Server filenames 
                for (int i = 0; i < Server.peerList.size(); i++) {
                    cRequest print = Server.peerList.get(i);
                    for (int j = 0; j < print.getFilenames().size(); j++) {
                        Server.filenames.add((String) print.getFilenames().get(j));
                    }
                }
                Set<String> hs = new HashSet<>();    // sort and remove duplicate files from list using hash class
                hs.addAll(Server.filenames);
                Server.filenames.clear();
                Server.filenames.addAll(hs);
                request1.setfilenames(Server.filenames);
                ObjectOutputStream oos = new ObjectOutputStream(ClientSock.getOutputStream());
                oos.writeObject(request1);
                oos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Unregister client with unique port ID
    private boolean unregisterClient(int clientPort) {
        if (Server.peerList.isEmpty()) {
            return false;
        } else {
            for (int i = 0; i < Server.peerList.size(); i++) {
                cRequest client = Server.peerList.get(i);
                if (client.getPort() == clientPort) {
                    Server.peerList.remove(i);
                    return true;
                } else if (i == Server.peerList.size()) {
                    return false;
                }
            }
        }
        return false;
    }

    private void sendObject(cResponse response, boolean fromServer) // added boolean response true for server
    {
        try {
            if (fromServer) {
                ObjectOutputStream oos = new ObjectOutputStream(ClientSock.getOutputStream());
                oos.writeObject(response);
                System.out.println(ClientSock);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
