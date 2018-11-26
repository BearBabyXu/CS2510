
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Egan
 */
public class IndexMaster extends Thread{
    private ArrayList<MapperHelper> mapperHelperList=new ArrayList<>();
    private ArrayList<ReducerHelper> reducerHelperList=new ArrayList<>();
    private Socket socket;
    private static LinkedList<IndexRequest> indexJobQueue;
    private ArrayList<String> serverList=new ArrayList<String>();
    private ArrayList<ReducerDes> reducerList=new ArrayList<ReducerDes>();
    private static int currentServer=1;
    private int WorkerServerPort=9000;
    private int WorkerServerPort2=9000;
    private String masterIp="136.142.227.6";
    private int resultPort=9002;
    private final int numServer=1;
    private ObjectInputStream input;
    
    
    
    public IndexMaster(Socket socket){
        
       // String partIp="136.142.227.";
        String partIp="127.0.0.";
      //  for(int i=7;i<i+numServer;i++){
        //    String tempIp=partIp.concat(String.valueOf(i));
          //  serverList.add(tempIp);
        //}
        
        for(int i=1;i<2;i++){
            String tempIp=partIp.concat(String.valueOf(i));
            serverList.add(tempIp);
        }
        
        
       
        System.out.println("IndexMaster initialization ServerList:");
        for(String e:serverList){
            System.out.println(e);
        }
        
        
      
        this.socket=socket;
    }
    
    public void run(){
        try {
            
            input=new ObjectInputStream(socket.getInputStream());
            
            
            IndexRequest request=(IndexRequest)input.readObject();
            System.out.println("IndexRequst received:"+request.toString());
            jobInitialization(request);
            
        } catch (IOException ex) {
            Logger.getLogger(IndexMaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IndexMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
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
                
                MapperHelper tempMaperHelper=new MapperHelper(e.getAbsolutePath(),i,e.getName(),serverList.get(currentServer % numServer),WorkerServerPort,numMappers);
                currentServer++;
                mapperHelperList.add(tempMaperHelper);
                
            }
            
        
        }
        
        
        int numReducers=(int)(totalLength/(1024*1024*3)+1);
        for(int i=1;i<=numReducers;i++){
            ReducerHelper tempReducerHelper=new ReducerHelper(i,serverList.get(currentServer % numServer),WorkerServerPort2,totalMappers,masterIp, resultPort);
            currentServer++;
            reducerHelperList.add(tempReducerHelper);
        }
        
        for(ReducerHelper rh:reducerHelperList){
        
            rh.askReducerPort();
            
        }
        
        for(ReducerHelper rh:reducerHelperList){
            reducerList.add(new ReducerDes(rh.getIp(),rh.getReducerPort()));
        }
        
        
        System.out.println("MapperList:");
        for(MapperHelper e:mapperHelperList){
            System.out.println(e.toString());
        }
      
        
        System.out.println("reducerList:");
        for(ReducerHelper e:reducerHelperList){
            System.out.println(e.toString());
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
