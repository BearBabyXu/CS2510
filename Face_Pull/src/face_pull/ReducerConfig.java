/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package face_pull;

import java.io.Serializable;

/**
 *
 * @author Egan
 */
public class ReducerConfig implements Serializable{
     private int id;
    private String ip;
    private int port;
    private int totalMappers;
    private int masterPort;
    private String masterIp;
    
    
    public ReducerConfig(int id, String ip, int port, int totalMappers, String masterIp,int masterPort){
        this.id=id;
        this.ip=ip;
        this.port=port;
        this.totalMappers=totalMappers;
        this.masterPort=masterPort;
        this.masterIp=masterIp;
        
    
    }
    
    public String toString(){
        return "ReducerConfig id"+this.id+"port:"+this.port+" total Mapper:"+this.totalMappers;
    }
    public String getMasterIp(){
        return this.masterIp;
    }
    
    public int getMasterPort(){
        return this.port;
    }
    
   public int getId(){
   return this.id;
   }
   
   public String getIp(){
       return this.ip;
   }
   
   public int getPort(){
       return this.port;
   }
   
   public int getTotalMapper(){
       return this.totalMappers;
   }
}
