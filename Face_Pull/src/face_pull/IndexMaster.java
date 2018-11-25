
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.File;
import java.io.IOException;
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
    private ArrayList<ReducerDes> reducerList;
    private static int currentServer=1;
    private int WorkerServerPort=9000;
    private int reducerPort=9000;
    private String masterIp="136.142.227.5";
    private int resultPort=9002;
    
    
    
    public IndexMaster(Socket socket){
        
        String partIp="136.142.227.";
        
        for(int i=7;i<16;i++){
            String tempIp=partIp.concat(String.valueOf(i));
            serverList.add(tempIp);
        }
        
      
        this.socket=socket;
    }
    
    public boolean jobInitialization(IndexRequest request) throws IOException{
        
        String dir=request.getFileDirectory();
        
        File folder=new File(dir);
        
        Long totalLength=0L;
        
        int totalMappers=0;
        
        for(File e:folder.listFiles()){
            Long tempLength=e.length();
            totalLength+=tempLength;
            
            int numMappers=(int)(tempLength/(1024*1024*2)+1);
            totalMappers+=numMappers;
            
            for(int i=1;i<=numMappers;i++){
                
                MapperHelper tempMaperHelper=new MapperHelper(e.getAbsolutePath(),i,e.getName(),serverList.get(currentServer % 9),WorkerServerPort,numMappers);
                currentServer++;
                mapperHelperList.add(tempMaperHelper);
                
            }
            
        
        }
        
        
        int numReducers=(int)(totalLength/(1024*1024*3)+1);
        for(int i=1;i<=numReducers;i++){
            ReducerHelper tempReducerHelper=new ReducerHelper(i,serverList.get(currentServer % 9),reducerPort,totalMappers,masterIp, resultPort);
            currentServer++;
            reducerHelperList.add(tempReducerHelper);
        }
        
        for(ReducerHelper rh:reducerHelperList){
        
            rh.askReducerPort();
        }
        
        for(ReducerHelper rh:reducerHelperList){
            reducerList.add(new ReducerDes(rh.getIp(),rh.getReducerPort()));
        }
        
        
        
         for(ReducerHelper rh:reducerHelperList){
        
            rh.initialize();
        }
        

        for(MapperHelper mh:mapperHelperList){
            mh.initialize(numReducers, reducerList);
        }
        
       
        
        
        
        
        
    
    return true;
    }
    
}
