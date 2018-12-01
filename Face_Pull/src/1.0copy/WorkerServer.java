/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package face_pull;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class WorkerServer {
   
    /**
     *
     * @param args
     */
    public static void main(String[] args) throws IOException{
       
       
            int port = 9000;
            ServerSocket serverSocket = new ServerSocket(port);
            
            while(true){
            
                new WorkerAssigner(serverSocket.accept()).start();
            }
            
          
       
       
    }
   }

    

