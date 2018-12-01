/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package face_pull;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Egan
 */
public class IndexMasterServer {
    public static void main(String[] args) throws IOException{
        ServerSocket google=new ServerSocket(8888);
        
        while(true){
            
            new Master(google.accept()).start();
            
            
        }
    }
}
