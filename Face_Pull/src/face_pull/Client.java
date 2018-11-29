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
    
    public static void main(String args[]) throws ClassNotFoundException {
        ObjectOutputStream out = null;
        
        Request request = null;
        
        try {
            System.out.print("WTF");
            Socket socket = new Socket("127.0.0.1", 8888);
              System.out.print("WTF2");
            Scanner read = new Scanner(System.in);
            out = new ObjectOutputStream(socket.getOutputStream());
           
            
            while(true) {
                System.out.print("Type >>> ");
                //String req = read.nextLine();
                String req = "search";
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
                    String[] keywords=keyword.split(" ");
                    System.out.print("Keywords:");
                    
                    for(int i=0; i<keywords.length;i++){
                        System.out.print(keywords[i]+" ");
                    }
                    
                    int length=keywords.length;
                   int count=0;
                   request = new Request(1);
                    request.addQuery(keyword);
                    out.writeObject(request);
                  
                     ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
                    
                    while(count<length){
                       
                        SearchResult temp=(SearchResult) in.readObject();
                        System.out.println(temp.toString());
                        count++;
                    }
                    
                    
                    
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
