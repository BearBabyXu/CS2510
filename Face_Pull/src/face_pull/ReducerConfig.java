/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.util.HashMap;

/**
 *
 * @author Egan
 */
public class ReducerConfig {
    private String File_Directory;
    private HashMap<String, Integer> Table;
    
    public ReducerConfig(String _FILEDIRECTORY, HashMap<String, Integer> _TABLE) {
        this.File_Directory = _FILEDIRECTORY;
        this.Table = _TABLE;
    }
    
    
}
