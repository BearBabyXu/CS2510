/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

/**
 *
 * @author Egan
 */
public class ReducerHelper {

    private int id;
    private String ip;
    private int port;

    public ReducerHelper(int id, String ip,int port) {
        this.id = id;
        this.ip=ip;
        this.port=port;
    }
    
    public boolean callReducer(){
    return true;
    }
    
    public boolean initialize(){
        return true;
    }

}
