/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

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
    
    // element for sending process
    private final String masterIP;
    private final int masterPort;
    private int reducerID;
    
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
        
    }
    
    public void run() {
        
        try {
            final ServerSocket serverSocket = new ServerSocket(reducerPort);
            System.out.printf("Reducer on %s:%d is on and runing \n", serverSocket.getInetAddress(), serverSocket.getLocalPort());
            
            // Listener for income package from Mapper
            ReducerListener reducerListener = new ReducerListener(serverSocket, this);
            reducerListener.start();        
            
        } catch (IOException ex) {
            Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    public int getMapperCount() {
        return mapperCount;
    }
    
    public void addTask(ReducerPackage pack) {
        tasks.add(pack);
    }
    
    public ReducerPackage getTask() {
        return tasks.poll();
    }
    
    public boolean checkKey(String word) {
        return result.containsKey(word);
    }
    
    public LinkedList getKeyValue(String word) {
        return result.get(word);
    }
    
    public void addPosting(String key, LinkedList value) {
        result.put(key, value);
    }
    
    public int getMasterPort() {
        return masterPort;
    }
    
    public String getMasterIP() {
        return masterIP;
    }
    
    public HashMap<String, LinkedList<Posting>> getResult() {
        return result;
    }
    
    public void printResult() {
        for(String word: result.keySet()) {
            for(Posting posting: result.get(word)) {
                System.out.printf("%s: %d - %s", word, posting.getOccurence(), posting.getFileSource());
            }
        }
    }
    
    public boolean checkTasksDone() {
        return tasksDone;
    }
    
    public void finishReducing() {
        finishedReducer += 1;
        if(finishedReducer == mapperCount)
            tasksDone = true;
    }
    
    public HashMap<String, LinkedList<Posting>> getOldMap(String letter) {
        File f = new File("index/" + letter + ".bin");
        HashMap<String, LinkedList<Posting>> map = new HashMap<>();
        try {
            ObjectInputStream out = new ObjectInputStream(new FileInputStream(f));
            map = (HashMap<String, LinkedList<Posting>>) out.readObject();
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reducer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }
    
    public void writeToFile(ArrayList<String> startLetters) throws IOException {
        
        // 1. ========== Save as .txt ========
        // tranverse every element in HashMap<Word, LinkerList<File, occurence>>
        for(String start: startLetters) {
            File f = new File("index/" + start + ".txt");
            BufferedWriter br = new BufferedWriter(new FileWriter(f, true));
            for(String word: result.keySet()) {
                if(word.substring(0, 1).equals(start)) {
                    br.write(word + "-");
                    for(Posting post: result.get(word)) {
                        br.write(post.getOccurence() + "," + post.getFileSource() + ";");
                    }
                    br.write("\n");
                }
            }
            br.close();        
        }
        
        /*********** Another Saving Method **********/
        /*
        // 2. ========== Save as HashMap ========
        for(String start: startLetters) {
            HashMap<String, LinkedList<Posting>> outMap = getOldMap(start);
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
            File f = new File("index/" + start + ".bin");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(outMap); 
            out.close();
        }
        */

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
        int packCount = 0;
        int mapperCount = reducer.getMapperCount();
        ReducerThread[] threads = new ReducerThread[mapperCount];
        
        // thread initialization      
        for(int id = 0; id < threads.length; id++) {
            threads[id] = new ReducerThread(reducer, id);
        }
        
        // loop until receive matching numbers of mapper count
        while(packCount < mapperCount) {
            try {
                // wait for coming package
                socket = serverSocket.accept();
                in = new ObjectInputStream(socket.getInputStream());
                
                pack = (ReducerPackage) in.readObject();
                reducer.addTask(pack);
                System.out.printf("Reducer on %s:%d ", socket.getInetAddress(), socket.getLocalPort());
                System.out.printf("received %d/%d packages from Mapper\n", packCount, mapperCount);
               
                threads[packCount++].start();
                
            } catch (IOException ex) {
                Logger.getLogger(ReducerListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ReducerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println("Reducer Finish Receiving.");
             
        
        while(!reducer.checkTasksDone()) {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.print(".");               
            } catch (InterruptedException ex) {
                Logger.getLogger(ReducerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
              
        System.err.println("Saving Inverting Index");   
        try {
            reducer.writeToFile(pack.getStartLetters());
        } catch (IOException ex) {
            Logger.getLogger(ReducerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.interrupt();
    }
}

class ReducerThread extends Thread {
    private Reducer reducer;
    private ReducerPackage pack;
    private String filePath;
    private int id;
    
    public ReducerThread(Reducer reducer, int num) {
        this.reducer = reducer;
        this.id = num;
    }
    
    public void run() {
        HashMap<String, Integer> Table = new HashMap<>();      
        LinkedList<Posting> tempList = new LinkedList<>();
        Posting posting = null;
        this.pack = reducer.getTask();
        this.filePath = pack.getFileDirectory();
        
        Table = pack.getTable();
        
        int oldvalue = 0;
        int newvalue = 0;
        for(String word: Table.keySet()) {
            newvalue = Table.get(word);
            oldvalue = 0;
            
            // Key already exist
            int index = -1;
            if(reducer.checkKey(word)) {
                tempList = reducer.getKeyValue(word);
                // If already exist in same file
                for(Posting p: tempList) {
                    if(p.getFileSource().equals(filePath)) {
                        // retrieve old value
                        oldvalue = p.getOccurence();
                        // retrive that key index
                        index = tempList.indexOf(p);
                        break;
                    }
                }           
            }
            posting = new Posting(filePath, newvalue+oldvalue);
            // from different file
            if(index != -1)
                tempList.set(index, posting);
            // from existing file
            else
                tempList.add(posting);             
            reducer.addPosting(word, tempList);
        }
        
        System.out.println("Reducer Thread Finished");
        reducer.finishReducing();       
        this.interrupt();
    }
}
