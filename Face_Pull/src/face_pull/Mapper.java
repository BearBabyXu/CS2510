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
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Egan
 */
public class Mapper extends Thread{
    // Mapper Configuration
    private final MapperConfig config;
    
    // element for mapping work
    private int mapperID;
    private String filePath;
    private int mapperCount;
    private ArrayList<String> contents;
    private HashMap<String, Integer> mappedTable;
    
    // elements for sending work
    private String reducerIP;
    private  int reducerPort;
    private String fileName;
    private  ArrayList<ReducerDes> reducerList;
    
    public Mapper(MapperConfig config) {
        // initialization
        this.config = config;
        this.mapperID = config.getId();
        this.mapperCount = config.getCountMappers();
        this.reducerIP = config.getIp();
        this.reducerPort = config.getPort();
        this.filePath = config.getPath();
        this.fileName = config.getName();
        this.reducerList = config.getReducerList();
        this.mappedTable = new HashMap<>();
    }
    
    public void run() {
        // mapper process
        contents = readContent();
        mappedTable = mapping();
        shuffle();
        this.interrupt();
    }
    
    private ArrayList<String> readContent() {
        
        File inFile = new File(filePath);
        // Read assigned line in file
        ArrayList<String> contents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
            String line;
            int count = 1;
            while ((line = br.readLine()) != null) {
                // read only lines which are responsible to this mapper
                if( (count++) % mapperCount == mapperID)
                    contents.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
              
        ArrayList<String> managedData = new ArrayList<>();
        // data management
        for(String line: contents) {
            // remove space
            for(String word: line.split(" ")) {
                // eliminate non-alphebet character 
                String newword = "";
                for(int c = 0; c < word.length(); c++)
                    if(Character.isAlphabetic(word.charAt(c)))
                        newword += word.charAt(c);
                // change every terms into lowerCase character
                newword = newword.toLowerCase();
                managedData.add(newword);
            }
        }
        
        return managedData;
    }
    
    private HashMap<String, Integer> mapping() {
        
        HashMap<String, Integer> table = new HashMap<>();
        // mapping function

        for(String word: contents) {
            int value = 1;
            if(table.containsKey(word))
                value += table.get(word);
            table.put(word, value);
        }
        
        return table;
    }
    
    private boolean shuffle() {
        // how many existing reducer
        int reducerCount = this.reducerList.size();
        // this array contains total numbers of packages that are being sent to each reducer
        ReducerPackage[] packages = new ReducerPackage[reducerCount];
        
        // set filepath of each package
        for(int i = 0; i < reducerCount; i++) {
            System.out.println(reducerCount + "," + i);
            packages[i] = new ReducerPackage();
            packages[i].setFilePath(fileName);
        }
        
        int hashCode = 0;
        for(String word: mappedTable.keySet()) {
            // go over each word and seperate into each package
            hashCode = Math.abs(word.hashCode()); 
            int packIndex=hashCode%reducerCount;
            //System.out.println("packIndex:"+packIndex + "reduceCount: " + reducerCount);
            //System.out.println(hashCode + "," + reducerCount + "====== " + hashCode%reducerCount + " ====== " + packages[hashCode%reducerCount]);
            packages[packIndex].addPosting(word, mappedTable.get(word));
        }
        
        return sendToReducer(packages);
    }
    
    
    private boolean sendToReducer(ReducerPackage[] packages) {
        int reducerCount = packages.length;
        Socket socket = null;
        ObjectOutputStream out = null;
        ReducerPackage pack = null;
        
        try {
            for(int i = 0; i < reducerCount; i++) {
                // Connecting to each Reducer
                System.out.println(reducerList.get(i).getIp() + " / " + reducerList.get(i).getPort());
                socket = new Socket(reducerList.get(i).getIp(), reducerList.get(i).getPort());
                System.out.printf("Mapper #%d-%s, connected to Reducer on %s:%d \n", mapperID, filePath, reducerList.get(i).getIp(), reducerList.get(i).getPort());
                out = new ObjectOutputStream(socket.getOutputStream());
                
                // send package to assigned Reducer
                out.writeObject(packages[i]);
                System.out.println("Package Sent.");
                out.close();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Mapper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
}
