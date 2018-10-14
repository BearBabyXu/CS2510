/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server1;

import Request.Activity;
import Request.ClientRequest;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author brantxu
 */
public class ActivityHandler {
    
    private static int readCounter=0;       // Current ongoing read operations
    private static ArrayList<Activity> activityList = new ArrayList<>(); // Queue to store activities;
    private static int timeCounter=0;
    
    private static final String peerIP="127.0.0.1";
    private static final int peerPort=9998;
    

    public static String Handle(Socket socket, long session_ID) throws IOException, ClassNotFoundException{
        
        //read request from client
        ObjectInputStream clientInput=new ObjectInputStream(socket.getInputStream()); 
        ClientRequest request=(ClientRequest) clientInput.readObject(); 
        
        //convert request to activity
        Activity activity=Activity.RequestConversion(++timeCounter, session_ID, request);
        
        //Push the activity into the queue
        activityList.add(activity);
        
        // Time syncronization
        TimeSync(activity);
        
        
        return "";
    }
    
    public static String execute(Activity activity){
        
        if(activity.getRequest().getType().equals("read")){
            
          
        
        }else{
            System.out.println("hi");
        
        }
    
        return "hi";
    }

    public static boolean TimeSync(Activity activity) throws IOException{
        
        Socket peerSocket=new Socket(peerIP, peerPort);
        
        ObjectOutputStream output=new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(activity);
        
        DataInputStream input=new DataInputStream(peerSocket.getInputStream());
        
        // update local time
        timeCounter=input.readInt();
        
        return true;
    }
    
    
    
   
}
