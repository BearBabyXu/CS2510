/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Egan
 */
public class MapperHelper {
    private String path;
    private int id;
    private String file;
    private String ip;
    private  int port;
    
    public MapperHelper(String path, int id, String file, String ip, int port){
    this.path=path;
    this.id=id;
    this.file=file;
    this.ip=ip;
    this.port=port;
    }
    
    public boolean callMapper() throws IOException{
        
        Socket socket=new Socket(this.ip,port);
        return true;
    }
    
    public boolean initialize(){
        return true;
    }
    
}
