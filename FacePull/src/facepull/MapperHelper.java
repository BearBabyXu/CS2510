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
public class MapperHelper {
    
    private int id;
    String ip;
    private int port;
    private String file;
    private String absPath;
    
    public MapperHelper(String file, String absPath, int id, int port,){
        
    this.file=file;
    this.id=id;
    this.port=port;
    this.absPath=absPath;
    }
    
    public boolean callMapper(){
    
        return true;
    }
    
    public boolean initializeMapper(){
    
         return true;
         
    }
    
    
}
