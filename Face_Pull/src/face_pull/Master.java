
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
public class Master extends Thread{
    private ArrayList<MapperHelper> mapperHelperList=new ArrayList<>();
    private ArrayList<ReducerHelper> reducerHelperList=new ArrayList<>();
     private ArrayList<SearcherHelper> searcherHelperList=new ArrayList<>();
     
    private Socket socket;
    private static LinkedList<Request> indexJobQueue;
    private ArrayList<String> serverList=new ArrayList<String>();
    private ArrayList<ReducerDes> reducerList=new ArrayList<ReducerDes>();
    private static int currentServer=1;
    private int WorkerServerPort=9000;
    private int WorkerServerPort2=9000;
    private int WorkerServerPort3=9000;
    private String masterIp="136.142.227.6";
    private int resultPort=9002;
    private final int numServer=1;
    private ObjectInputStream input;
    private int numSearcher;
    
    
    
    
    public Master(Socket socket){
        
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
        
        
       
        System.out.println("Master initialization ServerList:");
        for(String e:serverList){
            System.out.println(e);
        }
        
        
      
        this.socket=socket;
    }
    
    public void run(){
        try {
            
            input=new ObjectInputStream(socket.getInputStream());
            

            Request request=(Request)input.readObject();
            
            if(request.getType()==0){
            
            
            System.out.println("IndexRequst received:"+request.toString());
            indexJobInitialization(request);
            }else if(request.getType()==1){
                
                searchJobInitialization(request);
                
              

            }
            
        } catch (IOException ex) {
            Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }
    
    public boolean searchJobInitialization(Request request){
      
    String[] keyWords=request.getQuery().split(" ");
    
    numSearcher=keyWords.length;
    for(int i=0;i<numSearcher;i++){
        SearcherHelper tempSearcherHelper=new SearcherHelper(keyWords[i],i,serverList.get(currentServer % numServer),WorkerServerPort3);
        currentServer++;
        new SearcherSender(tempSearcherHelper, socket);
        searcherHelperList.add(tempSearcherHelper);
    }
    
    return true;
    }
    
    
   public boolean indexJobInitialization(Request request) throws IOException{
        
        String dir=request.getFileDirectory();
        
        File folder=new File(dir);
        
        Long totalLength=0L;
        
        int totalMappers=0;
        
        for(File e:folder.listFiles()){
            if(!e.getAbsolutePath().endsWith("txt")){
                continue;
            }
            Long tempLength=e.length();
            totalLength+=tempLength;
            
            int numMappers=(int)(tempLength/(1024*1024*2)+1);
            totalMappers+=numMappers;
            
            for(int i=0;i<numMappers;i++){
                
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