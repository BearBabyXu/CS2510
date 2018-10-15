/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server2;


import Request.Activity;
import Request.ClientRequest;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

    private static int readCounter = 0;       // Current ongoing read operations
    private static ArrayList<Activity> activityList = new ArrayList<>(); // Queue to store activities;
    private static int timeCounter = 0;

    private static final String peerIP = "127.0.0.1";
    private static final int peerPort = 9998;
    
    
    public static boolean PHandle(Socket socket) throws IOException, ClassNotFoundException{
        
        ObjectInputStream peerInput= new ObjectInputStream(socket.getInputStream());
        Activity peerActivity=(Activity) peerInput.readObject();
        
        switch(peerActivity.getType()){
            
            case 0: timeCounter=peerActivity.getTimeStamp()+1;
                    DataOutputStream peerOutput=new DataOutputStream(socket.getOutputStream());
                    peerOutput.writeInt(timeCounter);
                    socket.close();
                    break;
            case 1:
                readCounter++;
                activityList.remove(0);
                break;
            case 2:
                readCounter--;
                break;
            case 3: activityList.remove(0);
             break;
                    
            
        
        }
        
        return true;
        
        
    }

    public static String Handle(Socket socket, long session_ID) throws IOException, ClassNotFoundException {

        //read request from client
        ObjectInputStream clientInput = new ObjectInputStream(socket.getInputStream());
        ClientRequest request = (ClientRequest) clientInput.readObject();

        //convert request to activity
        Activity activity = Activity.requestConversion(++timeCounter, session_ID, request);

        //Push the activity into the queue
        activityList.add(activity);

        // Time syncronization
        timeSync(activity);
        return execute(activity);

       
    }

    public static String execute(Activity activity) throws IOException {

        if (activity.getRequest().getType().equals("read")) {

            while (activityList.get(0).getRequesterId() != activity.getRequesterId()) {
            }
            
            
            
            readCounter++;
            sendReadSkip();
            String result=Operate(activity); 
            sendReadRelease();
            activityList.remove(0);
            readCounter--;
            
            return result;
            

        } else {
            while (activityList.get(0).getRequesterId() != activity.getRequesterId() || readCounter!=0) {
            }
            
            String result=Operate(activity);
            sendWriteRelease();
            
            return result;
            
            

        }

        
    }
    
    private static String Operate(Activity activity){
    
        return "hi";
    }
   
    private static boolean timeSync(Activity activity) throws IOException {

        Socket peerSocket = new Socket(peerIP, peerPort);

        ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(activity);

        DataInputStream input = new DataInputStream(peerSocket.getInputStream());

        // update local time
        timeCounter = input.readInt();
        peerSocket.close();
        return true;
    }
    
    private static boolean sendReadSkip() throws IOException{
        Socket peerSocket = new Socket(peerIP, peerPort);
        ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(Activity.createReadSkip());
        peerSocket.close();
        
        return true;
    }
    
     private static boolean sendReadRelease() throws IOException{
        Socket peerSocket = new Socket(peerIP, peerPort);
        ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(Activity.createReadRelease());
        peerSocket.close();
        
        return true;
    }
     
     private static boolean sendWriteRelease() throws IOException{
        Socket peerSocket = new Socket(peerIP, peerPort);
        ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(Activity.createWriteRelease());
        peerSocket.close();
        
        return true;
    }

}
