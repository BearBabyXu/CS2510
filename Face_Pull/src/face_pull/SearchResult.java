/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author brantxu
 */
public class SearchResult implements Serializable{
    private String keyword;
    private LinkedList<Posting> list;
    
    
    public SearchResult(String keyword, LinkedList<Posting> list){
        this.keyword=keyword;
        this.list=list;
    
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public LinkedList<Posting> getList() {
        return list;
    }
    
    public String toString(){
        String res=keyword;
        
        for(Posting e: list){
            res=res+" "+e.getFileName()+" "+e.getOccurence()+" ";
        
        }
        
        return res;
    
    }
    
    
}
