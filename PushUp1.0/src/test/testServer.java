/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import Request.ClientRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class testServer{
    
    final static int port = 8888;
    
    public static void main(String args[]) throws ClassNotFoundException {
        
        try {
            final ServerSocket serverSock = new ServerSocket(port);
            ObjectInputStream in = null;
            ObjectOutputStream out;
            Socket sock;
            sock = serverSock.accept();
            System.out.println("Server okay");
            
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
          
            ClientRequest request = null;
            String response = null;
            do {
                request = (ClientRequest) in.readObject();
                System.out.println("Get Request from client: " + request.getTarget());
                
                response = "Get " + request;
                out.writeObject(response);
                
            } while(true);
            
        } catch (IOException ex) {
            Logger.getLogger(testServer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
        
    }

    
}
