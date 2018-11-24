/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Egan
 */
public class Face_Pull {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        int port=8888;
        
        ServerSocket server=new ServerSocket(port);
        while(true){
            new IndexMaster(server.accept()).start();
        }
        
    }
    
}
