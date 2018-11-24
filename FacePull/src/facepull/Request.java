/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facepull;

/**
 *
 * @author brantxu
 */
public class Request {

    private int type; // 1.indexing 2.query
    private String dir;
    private String query;

    public Request(int type, String dir) {   //Constructor for indexing
        
        this.type = type;
        this.dir = dir;

    }
    
    public int getType(){
        return this.type;
        
    }

    public String getDir(){
    
        return this.dir;
    }
    
    
}
