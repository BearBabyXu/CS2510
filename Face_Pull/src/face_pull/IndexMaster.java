
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Egan
 */
public class IndexMaster extends Thread{
    private ArrayList<MapperHelper> mapperHelperList;
    private ArrayList<ReducerHelper> reducerHelperList;
    private Socket socket;
    private static LinkedList<IndexRequest> indexJobQueue;
    private ArrayList<String> serverList;
    private static int currentServer=1;
    private int mapperPort=9000;
    private int reducerPort=9001;
    
    
    
    public IndexMaster(Socket socket){
        
        String partIp="136.142.227.";
        
        for(int i=7;i<16;i++){
            String tempIp=partIp.concat(String.valueOf(i));
            serverList.add(tempIp);
        }
        
      
        this.socket=socket;
    }
    
    public boolean jobInitialization(IndexRequest request){
        
        String dir=request.getFileDirectory();
        
        File folder=new File(dir);
        
        Long totalLength=0L;
        
        for(File e:folder.listFiles()){
            Long tempLength=e.length();
            totalLength+=tempLength;
            
            int numMapperSiblings=(int)(tempLength/(1024*1024*2)+1);
            
            for(int i=1;i<=numMapperSiblings;i++){
                
                MapperHelper tempMaperHelper=new MapperHelper(e.getAbsolutePath(),i,e.getName(),serverList.get(currentServer % 9),9000);
                currentServer++;
                mapperHelperList.add(tempMaperHelper);
                
            }
            
        
        }
        
        
        int numReducerSiblings=(int)(totalLength/(1024*1024*3)+1);
        for(int i=1;i<=numReducerSiblings;i++){
            ReducerHelper tempReducerHelper=new ReducerHelper(i,serverList.get(currentServer % 9),reducerPort);
            currentServer++;
            reducerHelperList.add(tempReducerHelper);
        }
        
        
    
    return true;
    }
    
}
