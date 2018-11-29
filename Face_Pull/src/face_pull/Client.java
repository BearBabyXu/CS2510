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
        Request request = null;
        
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            Scanner read = new Scanner(System.in);
            out = new ObjectOutputStream(socket.getOutputStream());
            
            while(true) {
                System.out.print("Type >>> ");
                //String req = read.nextLine();
                String req = "index";
                if(req.toLowerCase().equals("exit"))
                    break;
                if(req.toLowerCase().equals("index")) {
                    System.out.print("Requ >>> ");
                    //String file = read.nextLine();
                    String file = "/Users/brantxu/Documents/GitHub/CS2510/Face_Pull/files";
                   request = new Request(0);
                   request.addFileDirectory(file);
                    //request.addFileDirectory(file);
                    out.writeObject(request);
                } else if (req.toLowerCase().equals("search")) {
                    System.out.print("Requ >>> ");
                    String keyword = read.nextLine();
                   request = new Request(1);
                    request.addQuery(keyword);
                    out.writeObject(request);
                } else {
                    System.err.println("Unknown Request");
                }   
                break;
            }       
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
