/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Egan
 */
public class ReducerPackage implements Serializable{
    private String File_Directory;
    private HashMap<String, Integer> Table;
    
    public ReducerPackage() {
        this.File_Directory = null;
        this.Table = new HashMap<>();
    }
    
    public void setFilePath(String filePath) {
        File_Directory = filePath;
    }
    
    public String getFileDirectory() {
        return File_Directory;
    }
    
    public HashMap<String, Integer> getTable() {
        return Table;
    } 
    
    public boolean addPosting(String key, int value) {
        if(Table.containsKey(key)) {
            System.err.printf("Duplicate Key: %s", key);
            return false;
        } else {
            Table.put(key, value);
            return true;
        }
    }
}
