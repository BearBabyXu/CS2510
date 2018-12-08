/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package face_pull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author brantxu
 */
public class SearcherHelper {
    private String keyword;
    private String ip;
    private int port;

    private int id;
    
    public SearcherHelper(String keyword, int id,String ip, int port){
        
        this.keyword=keyword;
        this.ip=ip;
        this.port=port;
        this.id=id;
    
    
    }
    
    public String getIp(){
        return this.ip;
    }
    public int getPort(){
        return this.port;
    }
    
   public String toString(){
       return "Searcher:"+id+" ip:"+this.ip+" port:"+this.port+" keyword:"+this.keyword;
   }
    
    public Config createConfig() {
        Config temp=new Config(3);
        temp.addConfig(new SearcherConfig(this.keyword,this.id));
        
         return temp;
    }
    
   
    
    
}
