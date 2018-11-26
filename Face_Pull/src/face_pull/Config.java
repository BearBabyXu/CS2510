/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.Serializable;

/**
 *
 * @author Egan
 */
public class Config implements Serializable {

    private int type;
    private Object config;
    
    public Config(int type) {
        this.type = type;
        this.config = null;
    }
    
    public int getType(){
        return this.type;
    }
    
    public void addConfig(Object config) {
        if(type == 0) {
            // MapperConfig
            this.config = (MapperConfig) config;
        } else if(type == 1) {
            this.config = (ReducerConfig) config;
        } else {
            System.err.println("Wrong Type");
        }
    }
    
   
    
    public Object getConfig() {
        if(type == 0) {
            return (MapperConfig) config;
        } else if(type == 1) {
            return (ReducerConfig) config;
        } else{
            return config;
        }
    }
    
    public String toString(){
    if(type == 0) {
            // MapperConfig
            this.config = (MapperConfig) config;
            return this.config.toString();
        } else if(type == 1) {
            this.config = (ReducerConfig) config;
            return this.config.toString();
        } else {
            return "wrong type";
        }
    
    }
}
