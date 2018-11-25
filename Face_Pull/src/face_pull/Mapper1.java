/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
public class Mapper1 {
    
    private final int PORT;
    private static int ID;
    private static Queue TASKS;
    private static Queue SENDS;
    
    public Mapper1(int _ID, int _PORT) {
        this.ID = _ID;
        this.PORT = _PORT;
        this.TASKS = new LinkedList();
        this.SENDS = new LinkedList();
    }
    
    void start() {
        
        try {
            final ServerSocket serverSock = new ServerSocket(PORT);
            System.out.printf("Mapper #%d is up and running\n", ID);
            
            // Listener for incoming request
            MapperListener mapperListener = new MapperListener(serverSock, this);
            mapperListener.start();
            
            // Processing incoming task
            MapperThread mapperThread = new MapperThread(this);
            mapperThread.start();
            
            // Sending Thread
            MapperSender mapperSender = new MapperSender(this);
            mapperSender.start();
            
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }   
    
    public int getMapperID() {
        return ID;
    }
    
    public boolean taskIsEmpty() {
        return TASKS.isEmpty();
    }
    
    public MapperConfig getNextTask() {
        return (MapperConfig) TASKS.poll();
    }
    
    public void addTask(MapperConfig MC) {
        TASKS.add(MC);
    }
    
    public ArrayList readContent(MapperConfig MC) {
        ArrayList<String> content = null;
      //  String File_Directory = MC.getFileDirectory();
      String File_Directory = MC.getPath();
       // int Mapper_Count = MC.getMapperCount();
       int Mapper_Count = MC.getCountMappers();
       // int Mapper_Index = MC.getMapperID();
       int Mapper_Index = MC.getId();
        
        File inFile = new File("src/face_pull/" + File_Directory);
        // Read assigned line in file
        content = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
            String line;
            int count = 1;
            while ((line = br.readLine()) != null) {
                // read only lines which are responsible to this reducer
                if( (count++) % Mapper_Count == Mapper_Index)
                    content.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }        
        
        return content;
    }
    
    public HashMap<String, Integer> mapping(ArrayList<String> contents) {
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
        
        return table;
    }
    
    public boolean sendIsEmpty() {
        return SENDS.isEmpty();
    }
    
    public void addSend(ReducerPackage RC) {
        SENDS.add(RC);
    }
    
    public ReducerPackage getNextSend() {
        return (ReducerPackage) SENDS.poll();
    }
}

class MapperThread extends Thread {
    private Mapper1 mapper;
    
    public MapperThread(Mapper1 _mapper) {
        this.mapper = _mapper;
    }
    
    public void run() {
        MapperConfig MC = null;
        ReducerPackage RC = null;
        ArrayList<String> contents = null;
        HashMap<String, Integer> table = null;
        
        while(true) {
            try {
                // Check Task queue periodically 
                TimeUnit.SECONDS.sleep(1);
                if(!mapper.taskIsEmpty()) {
                    System.err.println("Task Detected!");
                    MC = mapper.getNextTask();
                    contents = mapper.readContent(MC);
                    table = mapper.mapping(contents);
                  //  RC = new ReducerPackage(MC.getFileDirectory(), table);
                  //***********RC = new ReducerPackage(MC.getPath(), table);
                    mapper.addSend(RC);
                    System.err.println("Finish Task!!");
                }              
            } catch (InterruptedException ex) {
                Logger.getLogger(MapperThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class MapperListener extends Thread {
    private ServerSocket serverSocket;
    private Mapper1 mapper;
    
    public MapperListener(ServerSocket _serverSocket, Mapper1 _mapper) {
        this.serverSocket = _serverSocket;
        this.mapper = _mapper;
    }
    
    public void run() {
        Socket socket = null;
        ObjectInputStream in = null;
        MapperConfig MC = null;
        
        while(true) {
            try {
                socket = serverSocket.accept();
                in = new ObjectInputStream(socket.getInputStream());
                MC = (MapperConfig) in.readObject();
                mapper.addTask(MC);
                
            } catch (IOException ex) {
                Logger.getLogger(MapperListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MapperListener.class.getName()).log(Level.SEVERE, null, ex);
            }        
        }    
    }
}

class MapperSender extends Thread {
    private Mapper1 mapper;
    
    public MapperSender(Mapper1 _mapper) {
        this.mapper = _mapper;
    }
    
    public void run() {
        Socket socket = null;
        ObjectOutputStream out = null;
        ReducerPackage RC = null;
        
        while(true) {
            try {
                socket = new Socket("127.0.0.1", 9999);
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(MapperSender.class.getName()).log(Level.SEVERE, null, ex);
            }
       
            while(true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    // If Send queue is not empty
                    if(!mapper.sendIsEmpty()) {
                        // send the message sequencially
                        RC = mapper.getNextSend();
                        out.writeObject(RC);
                    }
                
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
}
