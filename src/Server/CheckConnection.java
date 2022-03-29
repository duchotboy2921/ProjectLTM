/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ducke
 */
public class CheckConnection extends Thread{

    public CheckConnection() {
    }
    
   

    @Override
    public void run() {
        System.out.println("Check connection is running!");
        while (true) {            
            for(int i = 0; i<Server.peerList.size();++i ){
                cRequest peer = Server.peerList.get(i);
                try {
                    Socket socket = new Socket(peer.getIpAddr(), peer.getPort());
                    cRequest requestCheck = new cRequest();
                    requestCheck.setRequestType(RequestType.IsAlive);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(requestCheck);
                    socket.close();
                } catch (IOException ex) {
                    if(Server.peerList.size()>i&&peer.getHostName().equals(Server.peerList.get(i).getHostName())){
                        System.out.println("Peer "+peer.getHostName()+" đã thoát!");
                        Server.peerList.remove(i);
                    }
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CheckConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
