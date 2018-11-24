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
public class ReducerHelper {
    
    private int id;
    private int port;
    
    public ReducerHelper(int id, int port){
        
        this.id=id;
        this.port=port;
    }
    
    public boolean callReducer(){
        
        return true;
    }
    
    public boolean initializeReducer(){
        
        return true;
    }
    
}
