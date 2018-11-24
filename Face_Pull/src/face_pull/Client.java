/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Egan
 */
public class Client {
    
    public static void main(String args[]) {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        
        try {
            Socket socket = new Socket("127.0.0.1", 1111);
            Scanner read = new Scanner(System.in);
            
            while(true) {
                System.out.print(">>> ");
                String req = read.nextLine();
                if(req.toLowerCase().equals("exit"))
                    break;
                if(req.toLowerCase().equals("index")) {
                    System.out.print(">>> ");
                    String file = read.nextLine();
                    
                    
                } else if (req.toLowerCase().equals("search")) {
                    System.out.print(">>> ");
                    String keyword = read.nextLine();
                } else {
                    System.err.println("Unknown Request");
                }                 
            }       
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
