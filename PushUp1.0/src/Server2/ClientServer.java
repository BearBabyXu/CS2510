/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server2;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class ClientServer extends Thread {

    private final int clientPort = 8889;

    public void run() {

        try {
            
            ServerSocket serverSocket = new ServerSocket(clientPort);
            
            while (true) {
                
                new ClientHandle(serverSocket.accept()).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
