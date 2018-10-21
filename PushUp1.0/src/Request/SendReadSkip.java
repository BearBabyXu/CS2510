/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class SendReadSkip extends Thread{
    private Socket peerSocket;
    
    public SendReadSkip(Socket socket){
        peerSocket=socket;
    }
    public void run(){
       ObjectOutputStream output;
        try {
            output = new ObjectOutputStream(peerSocket.getOutputStream());
            output.writeObject(Activity.createReadSkip());
            peerSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SendReadRelease.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
}