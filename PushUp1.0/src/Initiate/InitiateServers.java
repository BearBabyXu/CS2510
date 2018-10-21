/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Initiate;

import Client.RunReadClient7;
import Client.RunReadClient8;
import Client.RunWriteClient1;
import Client.RunWriteClient2;
import Client.RunWriteClient3;
import Client.RunWriteClient4;
import Server1.ClientServer;
import Server1.PeerServer;
import Server1.Server1;
import Server2.Server2;
import Server3.Server3;
import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author brantxu
 */
public class InitiateServers {
      public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

          Server1.main(args);
          Server2.main(args);
          Server3.main(args);
             
      
}
      
}
