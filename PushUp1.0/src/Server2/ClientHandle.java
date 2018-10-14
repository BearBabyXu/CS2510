/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class ClientHandle extends Thread {
    
    private Socket socket;  
    private Socket peerSocket;
    private final int peerPort=9999;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;

    public ClientHandle(Socket socket) throws IOException {
        this.socket = socket;
        peerSocket = new Socket("127.0.0.1", peerPort);

    }

    public void run() {
        try {
            //Communication with client
            OutputStream clientOutput = null;
            InputStream clientInput = null;
            clientInput = socket.getInputStream();
            clientOutput = socket.getOutputStream();
            
            //Communication with peer server
            OutputStream peerOutput=null;
            InputStream peerInput=null;
            peerInput=peerSocket.getInputStream();
            peerOutput=peerSocket.getOutputStream();
            
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            
            
            //receive message from client
            BufferedReader clientbf = new BufferedReader(new InputStreamReader(clientInput));
            System.out.println(clientbf.readLine());
            
            
            
            socket.close();
            
            
            //Send request to peer
            PrintWriter peerPW = new PrintWriter(peerOutput);
            peerPW.println("Message From Server2");
            peerPW.flush();
            peerSocket.close();

              

        } catch (IOException ex) {
            Logger.getLogger(ClientHandle.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
