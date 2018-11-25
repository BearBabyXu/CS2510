/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Egan
 */
public class MapperHelper {
    private String path;
    private int id;
    private String name;
    private String ip;
    private  int port;
    private int numMappers;
     private static ObjectOutputStream output;
   
    
    public MapperHelper(String path, int id, String file, String ip, int port, int numMapper){
    this.path=path;
    this.id=id;
    this.name=file;
    this.ip=ip;
    this.port=port;
    this.numMappers=numMapper;
    }
    
   
    
    private boolean callMapper(MapperConfig config) throws IOException{
        
        Socket socket=new Socket(this.ip,port);
        output= new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(config);
        return true;
    }
    
    public boolean initialize(int numReducer, ArrayList<String> reducerList) throws IOException{
        
        this.callMapper(new MapperConfig(this,numReducer, reducerList));
        
        
        return true;
    }
    
    public String getPath(){
    return this.path;
    }
    public int getId(){
        return this.id;
    }
    
    public String getName(){
        return this.name;
    }
    
    public  String getIp(){
          return this.ip;  
        
    }
    
    public int getPort(){
        return this.port;
    }
    
    public int getNumMappers(){
    return this.numMappers;
    }
    
 
    
}
