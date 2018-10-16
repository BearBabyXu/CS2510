/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server3;



import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class PeerServer extends Thread {
    
    private final int peerPort = 9999;

    public void run() {
        
        try {
            
            ServerSocket serverSocket = new ServerSocket(peerPort);
            while (true) {
                new PeerHandle(serverSocket.accept()).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
