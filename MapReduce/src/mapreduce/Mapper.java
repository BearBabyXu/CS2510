/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import mapreduce.ReducerConfig;
import mapreduce.MapperConfig;


/**
 *
 * @author Rycemond
 */
public class Mapper {
    
    private final int PORT;
    private static int ID;
    private static Stack tasks;
    private static Stack sends;
    
    public Mapper(int _PORT) {
        this.PORT = _PORT;
        this.ID = 9999;
        tasks = new Stack();
        sends = new Stack();
    }
    
    void start() {
        
        Socket socket = null;
        Thread thread = null;
        
        try {			
            final ServerSocket serverSock = new ServerSocket(PORT);
            System.out.printf("Mapper is up and running\n");
	
            TaskListener taskListener = new TaskListener(serverSock, this);
            taskListener.start();
            
            MappingThread mappingThread = new MappingThread(this);
            mappingThread.start();
            
            ReducerChannel channelThread = new ReducerChannel(this);
            channelThread.start();
                    
	} catch(Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    } 
    
    public int getMapperPort() {
        return PORT;
    }
    
    public int getMapperID() {
        return ID;
    }
    
    public void addTask(MapperConfig MC) {
        this.tasks.push(MC);
    }
    
    public boolean checkTask() {
        return tasks.empty();
    }
    
    public MapperConfig nextTask() {
        return (MapperConfig) tasks.pop();
    }
    
    /***
     * @readContent() which is used to read responsible content
     * and return ArrayList 'content' which needs to be processed with method @WordCount()
     * 
     * @param String File_Location;
     * @param int Reducer_Count;
     * @param int ID;
     * 
     * @return ArrayList<String> content;
    ***/
    public ArrayList<String> readContent(MapperConfig MC) {
        String File_Location = MC.getFileDirectory();
        int Mapper_Count = MC.getMapperCount();
        int File_Index = MC.getFileIndex();
        
        //System.out.printf("Mapper #%d, readContent Processing... ", ID);
        File inFile = new File("src/mapreduce/" + File_Location);
        // Read assigned line in file
        ArrayList<String> content = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
            String line;
            int count = 1;
            while ((line = br.readLine()) != null) {
                // read only lines which are responsible to this reducer
                if( (count++) % Mapper_Count == File_Index)
                    content.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        //System.err.println(" DONE!");
        return content;
    }
       
    public HashMap Mapping(ArrayList<String> contents) {
        //System.out.print("Mapping Processing...");
        
        HashMap<String, Integer> table = new HashMap<>();
        // mapping index into table <word, occurence>
        for(String line: contents) {
            for(String word: line.split(" ")) {
                int value = 1;
                if(table.containsKey(word))
                    value += table.get(word);
                table.put(word, value);
            }
        }
        
        //System.err.println(" DONE!");
        return table;
    }
    
    
    public boolean checkSend() {
        return sends.empty();
    }
    
    public void addSend(ReducerConfig RC) {
        sends.add(RC);
    }
    
    public ReducerConfig nextSend() {
        return (ReducerConfig) sends.pop();
    }
}

class TaskListener extends Thread {
    private final ServerSocket serverSock;
    private final Mapper mapper;
    
    public TaskListener(ServerSocket serverSock, Mapper mapper) {
        this.serverSock = serverSock;
        this.mapper = mapper;
    }
    
    public void run() {
        Socket socket = null;
        Thread thread = null;
        
        while(true) {
            try {
                socket = serverSock.accept();
                thread = new MapperThread(socket, mapper);
                thread.start();
                
            } catch (IOException ex) {
                Logger.getLogger(TaskListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class MappingThread extends Thread {
    private Mapper mapper;

    public MappingThread(Mapper mapper) {
        this.mapper = mapper;
    }
    
    public void run() {
        ArrayList<String> contents = null;
        HashMap<String, Integer> table = null;
        MapperConfig MC = null;
        ReducerConfig RC = null;
        
        while(true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                // if tasks is not empty
                if(!mapper.checkTask()) {
                    System.err.println("Task detected!!");
                    // Read Content of next task
                    //contents = mapper.readContent(mapper.nextTask());
                    //TimeUnit.SECONDS.sleep(1);
                    MC = mapper.nextTask();
                    table = mapper.Mapping(mapper.readContent(MC));
                    print(table);
                    RC = new ReducerConfig(MC.getFileDirectory(), table);
                    mapper.addSend(RC);
                    System.err.println("Finish Task!!");
                }
                
            } catch (InterruptedException ex) {
                Logger.getLogger(MappingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void print(HashMap<String, Integer> table) {
        for(String word: table.keySet()) {
            System.out.println(word + ": " + table.get(word));
        }
    }
}

class ReducerChannel extends Thread {
    private Mapper mapper;
    
    public ReducerChannel(Mapper mapper) {
        this.mapper = mapper;
    }
    
    public void run() {
        Socket sock = null;
        ObjectOutputStream out = null;
        
        while(true) {
        try {
            sock = new Socket("127.0.0.1", 1111);
            out = new ObjectOutputStream(sock.getOutputStream());
        } catch(Exception e) {
            
        }
        
        while(true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                //System.out.println("Waiting...");
                if(!mapper.checkSend()) {
                    System.out.println("Sending... ");
                    ReducerConfig temp = mapper.nextSend();
                    out.writeObject(temp);
                    TimeUnit.SECONDS.sleep(5);
                    out.writeObject(temp);
                    
                }
            } catch(Exception e) {
                
            }
        }
        }
    }
}
