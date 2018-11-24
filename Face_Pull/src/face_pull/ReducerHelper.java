/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.IOException;
import java.io.ObjectInputStream;
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
public class ReducerHelper {
    
    private final int PORT;
    private final String IP;
    private static int ID;
    private static Queue TASKS;
    private static Queue SENDS;
    
    public ReducerHelper(int _PORT, String _IP, int _ID) {
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
            
            ReducerSender reducerSender = new ReducerSender(this);
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
    
    public MapperConfig getNextTask() {
        return (MapperConfig) TASKS.poll();
    }
    
    public void addTask(ReducerConfig RC) {
        TASKS.add(RC);
    }
}

class ReducerThread extends Thread {
    private ReducerHelper reducer;
    
    public ReducerThread(ReducerHelper _reducer) {
        this.reducer = _reducer;
    }
    
    public void run() {
        MapperConfig MC = null;
        ReducerConfig RC = null;
        ArrayList<String> contents = null;
        HashMap<String, Integer> table = null;
        
        while(true) {
            try {
                // Check Task queue periodically 
                TimeUnit.SECONDS.sleep(1);
                if(!reducer.taskIsEmpty()) {
                    System.err.println("Task Detected!");
                    MC = reducer.getNextTask();
                    
                    
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
    private ReducerHelper reducer;
    
    public ReducerListener(ServerSocket _serverSocket, ReducerHelper _reducer) {
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
    private ReducerHelper reducer;
    
    public ReducerSender(ReducerHelper _reducer) {
        this.reducer = _reducer;
    }
    
    public void run() {
        
    }
}


