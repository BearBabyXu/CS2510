/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Egan
 */
public class ReducerHelper {

    private int id;
    private String ip;
    private int WorkerServerPort;
    private int totalMappers;
    private ObjectOutputStream output;
    private int reducerPort;
    private int masterPort;
    private String masterIp;
    

    public ReducerHelper(int id, String ip,int port,int tatalMappers,String masterIp, int masterPort) {
        this.id = id;
        this.ip=ip;
        this.WorkerServerPort=port;
        this.masterIp=masterIp;
        this.masterPort=masterPort;
    }
    
    public String getIp(){
        return this.ip;
    }
    
   
     
        public int getReducerPort(){
        return this.reducerPort;
    }
        
    public boolean askReducerPort() throws IOException{
        Socket socket =new Socket(this.ip,this.WorkerServerPort);
        ObjectOutputStream portAskingOutput=new ObjectOutputStream(socket.getOutputStream());
        portAskingOutput.writeObject(new Config(2));
        
        DataInputStream dInput=new DataInputStream(socket.getInputStream());
        this.reducerPort=dInput.readInt();
        socket.close();
        return true;
    }
     
    private boolean callReducer(Config config) throws IOException{
        
        Socket socket=new Socket(this.ip,this.WorkerServerPort);
        output=new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(config);
        socket.close();
        
        
    return true;
    }
    
    public boolean initialize() throws IOException{
        Config temp=new Config(1);
        temp.addConfig(new ReducerConfig(this.id,this.ip,this.reducerPort,this.totalMappers,this.masterIp,this.masterPort));
        this.callReducer(temp);
        
        return true;
    }

}
