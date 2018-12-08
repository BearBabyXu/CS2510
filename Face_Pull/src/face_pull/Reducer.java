/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package face_pull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Egan
 */
public class Reducer extends Thread {
    // Reducer Configuration
    private final ReducerConfig config;
    
    // element for reducer process
    private int mapperCount;
    private int reducerPort;
    private LinkedList<ReducerPackage> tasks;
    private HashMap<String, LinkedList<Posting>> result;
    private int finishedReducer;
    private boolean tasksDone;
    private boolean finishReceiving;
    private int nowReceived;
    
    // element for sending process
    private final String masterIP;
    private final int masterPort;
    private int reducerID;
    private ArrayList<String> startLetters;
    
    public Reducer(ReducerConfig config) {
        this.config = config;
        this.reducerPort = config.getPort();
        this.masterIP = config.getMasterIp();
        this.masterPort = config.getMasterPort();
        this.mapperCount = config.getTotalMapper();  
        this.tasksDone = false;
        this.finishedReducer = 0;
        this.tasks = new LinkedList<>();
        this.result = new HashMap<>();
        this.reducerID = config.getId();
        this.finishReceiving = false;
        this.nowReceived = 0;
        this.startLetters = new ArrayList<>();
    }
    
    public void run() {
        Long startTime=System.currentTimeMillis();
        try {
            final ServerSocket serverSocket = new ServerSocket(reducerPort);
            System.out.printf("Reducer on %s:%d is on and runing \n", serverSocket.getInetAddress(), serverSocket.getLocalPort());
            
            // Listener for income package from Mapper
            ReducerListener reducerListener = new ReducerListener(serverSocket, this);
            reducerListener.start();
            
            while(!finishReceiving) {
                // Wait until receiving correct number of packages from mappers
               TimeUnit.SECONDS.sleep(1);
            }
            
            System.out.printf("Reducer #%d, finish collecting packages\n", reducerID);
            for(ReducerPackage pack: tasks) {
                int oldvalue = 0;
                int newvalue = 0;
                HashMap<String, Integer> Table = pack.getTable();
                LinkedList<Posting> tempList = new LinkedList<>();
                String fileName = pack.getFileName();
                Posting posting = null;
                startLetters = pack.getStartLetters();
            
                for(String word: Table.keySet()) {
                    newvalue = Table.get(word);
                    oldvalue = 0;
                    tempList = new LinkedList<>();
                    
                    // Key already exist
                    int index = -1;
                    if(result.containsKey(word)) {
                        tempList = result.get(word);
                        // If already exist in same file
                        for(Posting p: tempList) {
                            if(p.getFileName().equals(fileName)) {
                                // retrieve old value
                                oldvalue = p.getOccurence();
                                // retrive that key index
                                index = tempList.indexOf(p);
                                break;
                            }
                        }
                    }
                
                    posting = new Posting(fileName, newvalue+oldvalue);
                    // from different file
                    if(index != -1) {
                        tempList.set(index, posting);
                    }
                    // from existing file
                    else
                        tempList.add(posting);             
                    result.put(word, tempList);                   
                }
            }  
        
            System.out.printf("Reducer #%d, finish reducing\n", reducerID);
            Long endTime=System.currentTimeMillis();
            System.out.println(endTime-startTime);
                   
        } catch (IOException ex) {
            Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
        try {
            writeToFile();
        } catch (IOException ex) {
            Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.printf("Reducer #%d, Total size:%d\n", reducerID, result.size());
         Long endTime=System.currentTimeMillis();
            System.out.println(endTime-startTime);
        this.interrupt();
    }
    
    public void addTask(ReducerPackage pack) {
        tasks.add(pack);
    }
    
    public boolean checkReceived() {
        if(tasks.size() >= mapperCount)
            finishReceiving = true;
        return finishReceiving;
    }
    
    public void finishReducing() {
        finishedReducer += 1;
        if(finishedReducer == mapperCount)
            tasksDone = true;
    }
    
    public HashMap<String, LinkedList<Posting>> getOldMap(String letter) {
        File f = new File("index/hashmap/" + letter + ".bin");      
        HashMap<String, LinkedList<Posting>> map = new HashMap<>();
        if(!f.exists())
            return map;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
            map = (HashMap<String, LinkedList<Posting>>) in.readObject();
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }
    
    public void writeToFile() throws IOException {
        
        // 1. ========== Save as .txt ========
        // tranverse every element in HashMap<Word, LinkerList<File, occurence>>
//        for(String start: startLetters) {
//            File f = new File("index/" + start + ".txt");
//            if(!f.exists())
//                f.createNewFile();
//            BufferedWriter br = new BufferedWriter(new FileWriter(f, true));
//            for(String word: result.keySet()) {
//                if(word.substring(0, 1).equals(start)) {
//                    br.write(word + "-");
//                    //System.out.println((result.get(word).size()));
//                    for(Posting post: result.get(word)) {
//                        br.write(post.getOccurence() + "," + post.getFileName()+ ";");
//                    }
//                    br.write("\n");
//                }
//            }
//            br.close();
//        }
        
        /*********** Another Saving Method **********/
        // 2. ========== Save as HashMap ========
        
        for(String start: startLetters) {
            File f = new File("index/hashmap/" + start + ".bin");
            System.out.println("Reducer:" + reducerID + " getting bin: " + start);
            HashMap<String, LinkedList<Posting>> outMap = getOldMap(start);
            if(f.exists())
                f.delete();
            for(String word: result.keySet()) {
                // Merge new index with old index
                if(outMap.containsKey(word)) {
                    LinkedList<Posting> tempList = outMap.get(word);
                    for(Posting post: result.get(word)) {
                        tempList.add(post);
                    }
                    outMap.put(word, tempList);
                } else {
                    outMap.put(word, result.get(word));
                }
            }
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(outMap); 
            out.close();
        }
    }
}

class ReducerListener extends Thread {
    private ServerSocket serverSocket;
    private Reducer reducer;
    
    public ReducerListener(ServerSocket serverSocket, Reducer reducer) {
        this.serverSocket = serverSocket;
        this.reducer = reducer;
    }
    
    public void run() {
        Socket socket = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        ReducerPackage pack = null;
        
        try {
            while(!reducer.checkReceived()) {
                socket = serverSocket.accept();
                in = new ObjectInputStream(socket.getInputStream());
            
                pack = (ReducerPackage) in.readObject();
                reducer.addTask(pack);
                System.out.printf("Reducer on %s:%d ", socket.getInetAddress(), socket.getLocalPort());
            }
        } catch (IOException ex) {
            Logger.getLogger(ReducerListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReducerListener.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        this.interrupt();
    }
}