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
public class Mapper {
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
    
    public void start() {
        // mapper process
        readContent();
        mapping();
        
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
        
        this.contents = contents;
        return true;
    }
    
    private void mapping() {
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
        
        this.mappedTable = table;
    }
    
    private void shuffle() {
        // how many existing reducer
        int reducerCount = this.reducerList.size();
        // this array contains total numbers of packages that are being sent to each reducer
        ReducerPackage[] packages = new ReducerPackage[reducerCount];
        
        // set filepath of each package
        for(ReducerPackage pack: packages)
            pack.setFilePath(filePath);
        
        int hashCode = 0;
        for(String word: mappedTable.keySet()) {
            // go over each word and seperate into each package
            hashCode = word.hashCode();
            packages[hashCode%reducerCount].addPosting(word, mappedTable.get(word));
        }
        
    }
    
    private boolean sendToReducer(ReducerPackage[] packages) {
        int reducerCount = packages.length;
        Socket socket = null;
        ObjectOutputStream out = null;
        ReducerPackage pack = null;
        
        try {
            for(int i = 0; i < reducerCount; i++) {
                // Connecting to each Reducer
                socket = new Socket(reducerIP, reducerPort);
                System.out.printf("Mapper #d, connected to Reducer...\n", mapperID);
                out = new ObjectOutputStream(socket.getOutputStream());
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Mapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
