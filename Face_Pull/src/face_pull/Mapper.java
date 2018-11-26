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
    private final String filePath;
    private final int mapperCount;
    private static ArrayList<String> contents;
    private static HashMap<String, Integer> mappedTable;
    
    // elements for sending work
    private final String reducerIP;
    private final int reducerPort;
    private final ArrayList<ReducerDes> reducerList;
    
    public Mapper(MapperConfig config) {
        // initialization
        this.config = config;
        this.mapperID = config.getId();
        this.mapperCount = config.getCountMappers();
        this.reducerIP = config.getIp();
        this.reducerPort = config.getPort();
        this.filePath = config.getPath();
        this.reducerList = config.getReducerList();
    }
    
    public void run() {
        // mapper process
        readContent();
        mapping();
        shuffle();
    }
    
    private boolean readContent() {
        
        File inFile = new File("src/face_pull/" + filePath);
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
            return false;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
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
        
        this.contents = managedData;
        
        return true;
    }
    
    private void mapping() {
        
        HashMap<String, Integer> table = new HashMap<>();
        // mapping function
        for(String word: contents) {
            int value = 1;
            if(table.containsKey(word))
                value += table.get(word);
            table.put(word, value);
        }
        
        this.mappedTable = table;
        
        /*
        HashMap<String, Integer> table = new HashMap<>();
        // mapping index into table <word, occurence>
        for(String line: contents) {
            for(String word: line.split(" ")) {
                // eliminate non-alphebet character 
                String newword = "";
                for(int c = 0; c < word.length(); c++)
                    if(Character.isAlphabetic(word.charAt(c)))
                        newword += word.charAt(c);
                newword = newword.toLowerCase();
                int value = 1;
                if(table.containsKey(newword))
                    value += table.get(newword);
                table.put(newword, value);
            }
        }
        
        this.mappedTable = table;
*/
    }
    
    private boolean shuffle() {
        // how many existing reducer
        int reducerCount = this.reducerList.size();
        // this array contains total numbers of packages that are being sent to each reducer
        ReducerPackage[] packages = new ReducerPackage[reducerCount];
        
        // set filepath of each package
        for(ReducerPackage pack: packages) {
            pack = new ReducerPackage();
            pack.setFilePath(filePath);
        }
        
        int hashCode = 0;
        for(String word: mappedTable.keySet()) {
            // go over each word and seperate into each package
            hashCode = word.hashCode();
            packages[hashCode%reducerCount].addPosting(word, mappedTable.get(word));
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
                socket = new Socket(reducerList.get(i).getIp(), reducerList.get(i).getPort());
                System.out.printf("Mapper #d, connected to Reducer on %s:%d \n", mapperID, reducerList.get(i).getIp(), reducerList.get(i).getPort());
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
