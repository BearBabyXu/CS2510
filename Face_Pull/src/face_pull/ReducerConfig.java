/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

/**
 *
 * @author Rycemond
 */
public class ReducerConfig {
     private int id;
    private String ip;
    private int port;
    private int totalMappers;
    
    public ReducerConfig(int id, String ip, int port, int totalMappers){
        this.id=id;
        this.ip=ip;
        this.port=port;
        this.totalMappers=totalMappers;
    
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
       return this.getTotalMapper();
   }
}
