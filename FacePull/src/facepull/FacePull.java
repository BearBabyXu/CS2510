/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facepull;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author brantxu
 */
public class FacePull {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        int port=8888;
         System.out.println("TinyGoogle");
         
        ServerSocket serverSocket = new ServerSocket(8888);
        
        while(true){
            new IndexMaster(serverSocket.accept()).start();
        }
       
    }
    
}
