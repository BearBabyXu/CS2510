/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package face_pull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    private String srcDir="index/hashmap/";
    
    
    public Searcher(SearcherConfig config,Socket socket){
        this.config=config;
        this.socket=socket;
    
    }
    
    public void run(){
        
        try {
            System.out.println("Searcher received"+config.toString());
            String keyWord=config.getKeyword();
            char init=keyWord.charAt(0);
            String srcPath=srcDir+String.valueOf(init)+".bin";
            File f=new File(srcPath);
            ObjectInputStream input= new ObjectInputStream(new FileInputStream(f));
            this.src=(HashMap<String,LinkedList<Posting>>)input.readObject();
            
           
                
        
          //  LinkedList<Posting> res=this.src.getOrDefault(keyWord, null);          
            
            //load map
            ObjectOutputStream output=new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(search());
            
            
        } catch (IOException ex) {
            Logger.getLogger(Searcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Searcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
    
    public SearchResult search(){
        
        if(src.containsKey(config.getKeyword())){
        
            return new SearchResult(config.getKeyword(),src.get(config.getKeyword()));
            
        }else{
            return new SearchResult(config.getKeyword(), new LinkedList<Posting>());
        }
           
        
    }
}
