/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facepull;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class IndexMaster extends Thread{
    private ArrayList<MapperHelper> mapperHelperList=new ArrayList<>();
    private ArrayList<ReducerHelper> reducerHelperList=new ArrayList<>();
    private Socket socket;
    private static int mapperPort=12000;
    
    public IndexMaster(Socket socket){
        this.socket=socket;
    }
    public void run(){
        try {
            ObjectInputStream incomingRequest = new ObjectInputStream(socket.getInputStream());
            Request clientRequest=(Request) incomingRequest.readObject();
            switch(clientRequest.getType()){ 
            
            }
            
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(IndexMaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IndexMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public boolean jobInitialization(String dir){
        int mapperNum=0;
        File targetDir=new File(dir);
        for(File f:targetDir.listFiles()){
            
            mapperNum=(int) ((f.length())/(1024*1024))+1;
            
            String abspath=f.getAbsolutePath();
            
                    
            for(int i=0;i<mapperNum;i++){
                MapperHelper temp;
            }
            
            
        
        }
        
        return true;
    
    }
    
    public boolean invertedIndexMerge(){
    
        return true;
    }
}
