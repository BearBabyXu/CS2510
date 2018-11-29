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
    private Socket socket;
    
    
    public SearcherSender(SearcherHelper sh, Socket socket){
        
        this.sh=sh;
        this.socket=socket;
        
    }
    
    public void run(){
        try {
            launch();
        } catch (IOException ex) {
            Logger.getLogger(SearcherSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SearcherSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   public boolean launch() throws IOException, ClassNotFoundException{
         Socket workerSocket=new Socket(sh.getIp(),sh.getPort());
         
        ObjectOutputStream workerOutput=new ObjectOutputStream(workerSocket.getOutputStream());
        ObjectInputStream workerInput=new ObjectInputStream(workerSocket.getInputStream());
        workerOutput.writeObject(sh.createConfig());
        SearchResult result=(SearchResult) workerInput.readObject();
        
       ObjectOutputStream clientOutput=new ObjectOutputStream(socket.getOutputStream());
       clientOutput.writeObject(result);
        return true;
   
   }
    
}
