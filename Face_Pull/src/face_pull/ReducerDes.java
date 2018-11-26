/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.Serializable;

/**
 *
 * @author brantxu
 */
public class ReducerDes implements Serializable{
    private String ip;
    private int port;
    
    public ReducerDes(String ip, int port){
        this.ip=ip;
        this.port=port;
             
    }
    
    public String toString(){
        return "ReducerDes ip:"+this.ip+"port"+this.port;
    }
    
    public String getIp(){
        return this.ip;
    }
    
    public int getPort(){
        return this.port;
    }
    
    
            
         
}
