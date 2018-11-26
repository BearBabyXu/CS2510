/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Egan
 */
public class MapperConfig  implements Serializable {
  
   private String path;
   private int id;
   private String name;
   private String ip;
   private int port;
   
   private ArrayList<ReducerDes> reducerList;
   private int numMappers;
   private int numReducer;
    
    
    public MapperConfig(MapperHelper mh, int numReducer, ArrayList<ReducerDes> reducerList) {
        
            this.path=mh.getPath();
            this.id=mh.getId();
            this.name=mh.getName();
            this.ip=mh.getIp();
            this.port=mh.getPort();
            this.reducerList=reducerList;
            this.numMappers=mh.getNumMappers();
            this.numReducer=numReducer;

    }
    
    public String toString(){
        return "MapperConfig path:"+this.path+"id"+this.id;
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
    
    public String getIp(){
        return this.ip;
    }
    
    public int getPort(){
        return this.port;
    }
    
    public ArrayList<ReducerDes> getReducerList(){
    return this.reducerList;
    
    }
  
    public int getCountMappers(){
        return this.numMappers;
    }
    
    public int getCountReducers(){
    return this.numReducer;
        }
}
