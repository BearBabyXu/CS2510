/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Egan
 */
public class Reducer {
    
    private final int PORT;
    private final String IP;
    private static int ID;
    private static Queue TASKS;
    private static Queue SENDS;
    private static HashMap<String, LinkedList> Result;
    private boolean ReadyToSendReply = false;
    
    public Reducer(int _PORT, String _IP, int _ID) {
        this.PORT = _PORT;
        this.IP = _IP;
        this.ID = _ID;
        this.TASKS = new LinkedList();
        this.SENDS = new LinkedList();
    }
    
    public void start() {
        try {
            final ServerSocket serverSock = new ServerSocket(PORT);
            System.out.printf("Reducer #%d is up and running\n", ID);
            
            ReducerThread reducerThread = new ReducerThread(this);
            reducerThread.start();
            
            ReducerListener reducerListener = new ReducerListener(serverSock, this);
            reducerListener.start();
            
            ReducerSender reducerSender = new ReducerSender("127.0.0.1", 1111, this);
            reducerSender.start();
            
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public int getReducerID() {
        return ID;
    }
    
    public boolean taskIsEmpty() {
        return TASKS.isEmpty();
    }
    
    public ReducerConfig getNextTask() {
        return (ReducerConfig) TASKS.poll();
    }
    
    public void addTask(ReducerConfig RC) {
        TASKS.add(RC);
    }
    
    public HashMap<String, LinkedList> getTable() {
        return Result;
    }
    
    public void reduce(ReducerConfig RC) {
        HashMap<String, Integer> Table = RC.getTable();
        LinkedList<Posting> tempList = null;
        Posting posting = null;
        
        int oldvalue = 0;
        int newvalue = 0;
        for(String word: Table.keySet()) {
            newvalue = Table.get(word);
            oldvalue = 0;
            
            // Key already exist
            int index = -1;
            if(Result.containsKey(word)) {
                tempList = Result.get(word);
                // If already exist in same file
                for(Posting p: tempList) {
                    if(p.getFileSource().equals(RC.getFileDirectory())) {
                        // retrieve old value
                        oldvalue = p.getOccurence();
                        // retrive that key index
                        index = tempList.indexOf(p);
                        break;
                    }
                }           
            }
            posting = new Posting(RC.getFileDirectory(), newvalue+oldvalue);
            // from different file
            if(index != -1)
                tempList.set(index, posting);
            // from existing file
            else
                tempList.add(posting);             
            Result.put(word, tempList);
        }      
    }
    
    public boolean sendIsEmpty() {
        return SENDS.isEmpty();
    }
    
    public IndexReply getNextSend() {
        return (IndexReply) SENDS.poll();
    }
    
    public void addSend(IndexReply IR) {
        SENDS.add(IR);
    }
    
    public boolean readyToSendReply() {
        return ReadyToSendReply;
    }
}

class ReducerThread extends Thread {
    private Reducer reducer;
    
    public ReducerThread(Reducer _reducer) {
        this.reducer = _reducer;
    }
    
    public void run() {
        ReducerConfig RC = null;
        IndexReply IR = null;
        
        while(true) {
            try {
                // Check Task queue periodically 
                TimeUnit.SECONDS.sleep(1);
                if(!reducer.taskIsEmpty()) {
                    System.err.println("Task Detected!");
                    RC = reducer.getNextTask();
                    reducer.reduce(RC);
                    System.err.println("Finish Task!!");
                }              
            } catch (InterruptedException ex) {
                Logger.getLogger(MapperThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class ReducerListener extends Thread {
    private ServerSocket serverSocket;
    private Reducer reducer;
    
    public ReducerListener(ServerSocket _serverSocket, Reducer _reducer) {
        this.serverSocket = _serverSocket;
        this.reducer = _reducer;
    }
    
    public void run() {
        Socket socket = null;
        ObjectInputStream in = null;
        ReducerConfig RC = null;
        
        while(true) {
            try {
                socket = serverSocket.accept();
                in = new ObjectInputStream(socket.getInputStream());
                RC = (ReducerConfig) in.readObject();
                reducer.addTask(RC);
                
            } catch (IOException ex) {
                Logger.getLogger(MapperListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MapperListener.class.getName()).log(Level.SEVERE, null, ex);
            }        
        }
    }
}

class ReducerSender extends Thread {
    private Reducer reducer;
    private String IndexMasterIP;
    private int IndexMasterPort;
    
    public ReducerSender(String _INDEXMASTERIP, int _INDEXMASTERPORT, Reducer _reducer) {
        this.reducer = _reducer;
        this.IndexMasterIP = _INDEXMASTERIP;
        this.IndexMasterPort = _INDEXMASTERPORT;
    }
    
    public void run() {
        Socket socket = null;
        ObjectOutputStream out = null;
        IndexReply IR = null;
        
        while(true) {
            try {
                socket = new Socket(IndexMasterIP, IndexMasterPort);
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(MapperSender.class.getName()).log(Level.SEVERE, null, ex);
            }
       
            while(true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    // If Send queue is not empty
                    if(reducer.readyToSendReply()) {
                        // send the message sequencially
                        IR = new IndexReply(reducer.getTable());
                        out.writeObject(IR);
                    }
                
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
}


