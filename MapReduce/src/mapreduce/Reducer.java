/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Egan
 */
public class Reducer {
    
    private final int PORT;
    private static int ID;
    private static Stack tasks;
    private static Stack sends;
    private static HashMap<String, HashMap<String, Integer>> result;
    private static int FINISHED;
    
    public Reducer(int _PORT) {
        this.ID = 1111;
        this.PORT = _PORT;
        tasks = new Stack();
        sends = new Stack();
        result = new HashMap<>();
    }
    
    public void start() {
        
        Socket socket = null;
        Thread thread = null;
        
        try {
            final ServerSocket serverSock = new ServerSocket(PORT);
            System.out.printf("Reducer is up and running\n" + serverSock.getLocalPort());
	
            reducerListener reducerListener = new reducerListener(serverSock, this);
            reducerListener.start();
            
            ReduceThread reduceThread = new ReduceThread(this);
            reduceThread.start();
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void addTask(ReducerConfig RC) {
        tasks.add(RC);
    }
    
    public boolean checkTask() {
        return tasks.empty();
    }
    
    public ReducerConfig nextTask() {
        return (ReducerConfig) tasks.pop();
    }
    
    public void print() {
        HashMap<String, Integer> temp = null;
        for(String word: result.keySet()) {
            temp = result.get(word);
            for(String file: temp.keySet()) {
                System.out.println(word + " - " + temp.get(file) + " : " + file);
            }
        }
    }
    
    public void Merge(ReducerConfig RC) {
        HashMap<String, Integer> table = null;
        table = RC.getIndexTable();
        
        for(String word: table.keySet()) {
            System.out.println(word + ": " + table.get(word));
        }
        
        int oldvalue = 0;
        int newvalue = 0;
        HashMap<String, Integer> tempMap = null;
        for(String word: table.keySet()) {
            tempMap = new HashMap<>();
            //System.out.printf("Combining %s ...\n", word);
            newvalue = table.get(word);
            oldvalue = 0;
            
            // If this key is already contained
            if(result.containsKey(word)) {
                //System.out.println("Key Already Exist");
                tempMap = result.get(word);
                if(tempMap.containsKey(RC.getFileSource())) {
                    //System.out.println("From existing File");         
                    // value = old value + new value
                    oldvalue = tempMap.get(RC.getFileSource());
                }
            }
            
            
            tempMap.put(RC.getFileSource(), oldvalue+newvalue);
            result.put(word, tempMap);         
        }
        
        HashMap<String, Integer> temp = null;
        for(String word: result.keySet()) {
            System.out.print(word + ": ");
            temp = result.get(word);
            for(String file: temp.keySet()) {
                System.out.print(temp.get(file) + " - " + file + "|");
            }
            System.out.println("");
        }
    }
}

class reducerListener extends Thread {
    private final ServerSocket serverSock;
    private final Reducer reducer;
    
    public reducerListener(ServerSocket serverSock, Reducer reducer) {
        this.serverSock = serverSock;
        this.reducer = reducer;
    }
    
    public void run() {
        Socket socket = null;
        Thread thread = null;
        
        while(true) {
            try {
                socket = serverSock.accept();
                thread = new ReducerThread(socket, reducer);
                thread.start();
                
                
            } catch (IOException ex) {
                Logger.getLogger(TaskListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }
}

class ReduceThread extends Thread{
    private final Reducer reducer;
    
    public ReduceThread(Reducer reducer) {
        this.reducer = reducer;
    }
    
    public void run() {
        ReducerConfig RC = null;
        
        while(true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                
                if(!reducer.checkTask()) {
                    System.err.println("Task detected!!");
                    RC = reducer.nextTask();
                    System.out.println("+++" + RC.getFileSource());
                    reducer.Merge(RC);
                    System.err.println("Finish Task!!");
                    //reducer.print();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ReduceThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
    
}
