/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class PeerHandle extends Thread {

    private Socket socket;
    public PeerHandle(Socket socket) throws IOException {
        this.socket = socket;

    }

    public void run() {
        try {
            
            //Communication with peer
         
         ActivityHandler.PHandle(socket);
         
        } catch (IOException ex) {
            Logger.getLogger(PeerHandle.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PeerHandle.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
