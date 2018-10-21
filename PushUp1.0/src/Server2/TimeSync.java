/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server2;

import Request.Activity;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class TimeSync extends Thread{
    private Socket peerSocket;
    private Activity activity;
    
    public TimeSync(Socket socket, Activity activity){
        this.peerSocket=socket;
        this.activity=activity;
    
    }
     public void run(){
      try {
            ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
              DataInputStream input = new DataInputStream(peerSocket.getInputStream());
       
            output.writeObject(activity);
            
            ActivityHandler.updateTimeCounter(input.readInt());
            peerSocket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(TimeSync.class.getName()).log(Level.SEVERE, null, ex);
        }

          
     }
    
}
