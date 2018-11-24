/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import mapreduce.MapperConfig;

/**
 *
 * @author Egan
 */
public class Client {
    
    public static void main(String args[]) {
        
        try {
            Socket sock = new Socket("127.0.0.1", 9999);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            MapperConfig MC = new MapperConfig("File.txt", 1, 1, 0);
            out.writeObject(MC);
            
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
