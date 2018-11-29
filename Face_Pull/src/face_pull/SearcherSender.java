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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class SearcherSender extends Thread{
    private SearcherHelper sh;
    private  Socket socket;
   private ObjectOutputStream clientOutput;
    
    
    
    public SearcherSender(SearcherHelper sh, Socket socket,ObjectOutputStream clientOutput ){
        
        this.sh=sh;
        this.socket=socket;
        this.clientOutput=clientOutput;
        
    }
    
    public void run(){
        try {
            Socket workerSocket=new Socket(sh.getIp(),sh.getPort());
            
            ObjectOutputStream workerOutput=new ObjectOutputStream(workerSocket.getOutputStream());
            
            workerOutput.writeObject(sh.createConfig());
            ObjectInputStream workerInput=new ObjectInputStream(workerSocket.getInputStream());
            SearchResult result=(SearchResult) workerInput.readObject();
            System.out.println(result.toString());
       //     ObjectOutputStream clientOutput=new ObjectOutputStream(socket.getOutputStream());
         
            clientOutput.writeObject(result);
          //  clientOutput.reset();
           
            
        } catch (IOException ex) {
            Logger.getLogger(SearcherSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SearcherSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
}
