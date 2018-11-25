/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Egan
 */
public class ReducerHelper {

    private int id;
    private String ip;
    private int port;
    private int totalMappers;
    private ObjectOutputStream output;

    public ReducerHelper(int id, String ip,int port,int tatalMappers) {
        this.id = id;
        this.ip=ip;
        this.port=port;
    }
    
    private boolean callReducer(ReducerConfig config) throws IOException{
        
        Socket socket=new Socket(this.ip,this.port);
        output=new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(config);
        
        
    return true;
    }
    
    public boolean initialize() throws IOException{
        this.callReducer(new ReducerConfig(this.id,this.ip,this.port,this.totalMappers));
        
        return true;
    }

}
