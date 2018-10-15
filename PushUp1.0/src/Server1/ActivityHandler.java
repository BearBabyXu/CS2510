/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server1;

import Request.Activity;
import Request.ClientRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class ActivityHandler {

    private static int readCounter = 0;       // Current ongoing read operations
    private static ArrayList<Activity> activityList = new ArrayList<>(); // Queue to store activities;
    private static int timeCounter = 0;

    private static final String peerIP = "127.0.0.1";
    private static final int[] peerPort = {9998,9999};
    
    
    public static boolean PHandle(Socket socket) throws IOException, ClassNotFoundException{
        
        ObjectInputStream peerInput= new ObjectInputStream(socket.getInputStream());
        Activity peerActivity=(Activity) peerInput.readObject();
        
        switch(peerActivity.getType()){
            
            case 0: activityList.add(peerActivity);
                    timeCounter=peerActivity.getTimeStamp()+1;
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

    public static String Handle(Socket socket, long session_ID) throws IOException, ClassNotFoundException, InterruptedException {

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

    public static String execute(Activity activity) throws IOException, InterruptedException {

        if (activity.getRequest().getType().equals("Read")) {
            boolean flag1=true;
            while (flag1) {
                if(activityList.get(0).getRequesterId() == activity.getRequesterId()){
                    flag1=false;
                }
            }

            readCounter++;
            
            System.out.println("READ"+readCounter);

            activityList.remove(0);
            
            sendReadSkip();
            String result = Operate(activity);
            sendReadRelease();
            readCounter--;

            return result;

        } else {
            
            
            boolean flag2=true;
            while (flag2) {
                
                if(activityList.get(0).getRequesterId() == activity.getRequesterId() || readCounter == 0){
                flag2=false;
                }
                
            }
            
            
          
            String result = Operate(activity);
            
            Thread.sleep(5000);
            activityList.remove(0);
            
            
            
            sendWriteRelease();

            return result;

        }

    }

    private static String Operate(Activity activity) {
        FileInputStream fis = null;
        FileOutputStream fout = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        
        String type = activity.getRequest().getType();
        String target = activity.getRequest().getTarget();
        String update = activity.getRequest().getUpdate();

        try {
            fis = new FileInputStream("data.txt");
            reader = new BufferedReader(new InputStreamReader(fis));
            List<String> data = new ArrayList();
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.add(line);
            //   System.out.println(line);
            }
            
            // read data from file
            fis.close();
            
            int targetIndex = -1;
            for(int i = 0; i < data.size(); i++) {
                // find target
                if (data.get(i).substring(0, 1).equals(target)) {
                    // get target index
                    targetIndex = i;
                }
            }
            
            if(type.equals("Read")) {
                // return target data
                return data.get(targetIndex);          
            } else if (type.equals("Write")) {
                // update updatdata
                fout = new FileOutputStream("data.txt");
                writer = new BufferedWriter(new OutputStreamWriter(fout));
                for(int i = 0; i < data.size(); i++) {
                    if(i == targetIndex) {
                        int oldVal = Integer.parseInt(data.get(targetIndex).substring(3));
                        int newVal = Integer.parseInt(update);
                        writer.write(target + ", " + String.valueOf(oldVal-newVal) + "\n" );
                    } else {
                        writer.write(data.get(i) + "\n");
                    }
                }
                
                writer.flush();
                fout.close();
                System.out.println("Close File");
                return "done";
            } else {
                System.err.println("Not defined request type");
            }
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    private static boolean timeSync(Activity activity) throws IOException {
        for(int i=0;i<peerPort.length;i++){
        Socket peerSocket = new Socket(peerIP, peerPort[i]);

        ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(activity);

        DataInputStream input = new DataInputStream(peerSocket.getInputStream());

        // update local time
        timeCounter = input.readInt();
        peerSocket.close();
        
        //test
        
        System.out.println("\n");
        for(Activity e:activityList){
           
       
        System.out.println(e.getInfo());
        }
        
        }
        
        return true;
    }

    private static boolean sendReadSkip() throws IOException {
        for(int i=0;i<peerPort.length;i++){
        Socket peerSocket = new Socket(peerIP, peerPort[i]);
        ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(Activity.createReadSkip());
        peerSocket.close();
        }
        return true;
    }

    private static boolean sendReadRelease() throws IOException {
        for(int i=0;i<peerPort.length;i++){
        Socket peerSocket = new Socket(peerIP, peerPort[i]);
        ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(Activity.createReadRelease());
        peerSocket.close();
        }
        return true;
    }

    private static boolean sendWriteRelease() throws IOException {
        for(int i=0;i<peerPort.length;i++){
        Socket peerSocket = new Socket(peerIP, peerPort[i]);
        ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(Activity.createWriteRelease());
        peerSocket.close();
        }
        return true;
    }

}
