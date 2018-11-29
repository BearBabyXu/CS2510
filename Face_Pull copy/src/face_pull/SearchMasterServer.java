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
 * @author brantxu
 */
public class SearchMasterServer {
    public static void main(String[] args) throws IOException{
        ServerSocket google=new ServerSocket(8889);
        
        while(true){
             new SearchMaster(google.accept()).start();
        }
    }
}
