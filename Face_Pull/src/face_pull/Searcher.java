/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class Searcher extends Thread{
    private HashMap<String, LinkedList<Posting>> src;
    private SearcherConfig config;
    private Socket socket;
    
    
    public Searcher(SearcherConfig config,Socket socket){
        this.config=config;
        this.socket=socket;
    
    }
    
    public void run(){
        
        try {
            
            String keyWord=config.getKeyword();
            char init=keyWord.charAt(0);
            File f=new File();
            //load map
            ObjectOutputStream output=new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(search());
            
        } catch (IOException ex) {
            Logger.getLogger(Searcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
    
    public SearchResult search(){
    
        
        return new SearchResult(config.getKeyword(),src.get(config.getKeyword()));
    }
}
