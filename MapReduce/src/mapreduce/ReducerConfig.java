/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Egan
 */
public class ReducerConfig implements Serializable{
    private static int ID;
    private static String File_Source;
    private static HashMap<String, Integer> table;
            
    public ReducerConfig(String _FILESOURCE, HashMap _TABLE) {
        this.ID = 99999;
        this.File_Source = _FILESOURCE;
        this.table = _TABLE;
    }
    
    public String getFileSource() {
        return File_Source;
    }
    
    public HashMap getIndexTable() {
        return table;
    }
}
