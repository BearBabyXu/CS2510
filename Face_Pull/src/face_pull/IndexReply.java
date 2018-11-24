/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Egan
 */
public class IndexReply implements Serializable {
    private HashMap<String, LinkedList> Result;
    
    public IndexReply(HashMap<String, LinkedList> _result) {      
        this.Result = _result;
    }
    
    public HashMap<String, LinkedList> getTable() {
        return Result;
    }
}
