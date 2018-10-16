/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server3;



import Request.Activity;
import Request.ActivityComparator;
import Request.ClientRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
    private static final int[] peerPort = {9997,9998};
    
    
    public static boolean PHandle(Socket socket) throws IOException, ClassNotFoundException {

        ObjectInputStream peerInput = new ObjectInputStream(socket.getInputStream());
        Activity peerActivity = (Activity) peerInput.readObject();

        switch (peerActivity.getType()) {

            case 0:
                System.out.println("new activity from a peer received");
                timeCounter = Math.max(peerActivity.getTimeStamp() + 1, timeCounter + 1);
                activityList.add(peerActivity);
                activityList.sort(new ActivityComparator());

                System.out.println("\n A new request from peer, now queue:");
                for (Activity e : activityList) {

                    System.out.println(e.getInfo());
                }
                DataOutputStream peerOutput = new DataOutputStream(socket.getOutputStream());
                peerOutput.writeInt(timeCounter);
                socket.close();
                break;

            case 1:
                System.out.println("readSkip received");
                readCounter++;
                activityList.remove(0);
                 if(!activityList.isEmpty()){
                System.out.println("0: " + activityList.get(0).getRequesterId());}
                break;
            case 2:
                System.out.println("readRelease received");
                readCounter--;
                     if(!activityList.isEmpty()){
                   System.out.println("0: " + activityList.get(0).getRequesterId());
                
                System.out.println("0: " + activityList.get(0).getRequesterId());}
          
             
                break;
            case 3:
                System.out.println("writeRelease received");
                activityList.remove(0);
                
                if(!activityList.isEmpty()){
                
                
                System.out.println("0: " + activityList.get(0).getRequesterId());}
          

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
        System.out.println("\n A new request from client, now queue:");
        for (Activity e : activityList) {

            System.out.println(e.getInfo());
        }

        // Time syncronization
        timeSync(activity);
        return execute(activity);

    }

    public static String execute(Activity activity) throws IOException, InterruptedException {

        if (activity.getRequest().getType().equals("Read")) {
            boolean flag1 = true;
            System.out.println("0: " + activityList.get(0).getRequesterId());
            System.out.println("ID: " + activity.getRequesterId());
            System.out.print("ReadCounter:"+readCounter);

            while (flag1) {
                Thread.sleep(50);
                if (activityList.get(0).getRequesterId() == activity.getRequesterId()) {
                    flag1 = false;
                }
            }

            readCounter++;

            activityList.remove(0);

            sendReadSkip();
            String result = Operate(activity);
            sendReadRelease();
            readCounter--;

            return result;

        } else {

            System.out.println("0: " + activityList.get(0).getRequesterId());
            System.out.println("ID: " + activity.getRequesterId());
            boolean flag2 = true;
            long previous=0;
            while (flag2) {
                if(previous!=activityList.get(0).getRequesterId()){
                System.out.println("previous:" +previous+" Now:"+activityList.get(0).getRequesterId());
                
                }
                Thread.sleep(50);
                previous=activityList.get(0).getRequesterId();
                
                if (activityList.get(0).getRequesterId() == activity.getRequesterId() && readCounter == 0) {
                    flag2 = false;
                    break;
                }

            }
            System.out.println("out");
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
        int time = activity.getTimeStamp();

        String result = "";
        int sum = 0;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("shareFile.txt")));
            StringBuffer sb = new StringBuffer();

            //read each line
            String line = "";
            while ((line = reader.readLine()) != null) {
                sum = Integer.parseInt(line);
                sb.append(line + "\n");
            }

            reader.close();

            if (type.equals("Write")) {
                System.out.printf("Write (oldVal: %d, addVal: %d) \n", sum, Integer.parseInt(update));
                sum += Integer.parseInt(update);
                sb.append(sum + "\n");

                System.out.println("New Sum: " + sum);
                fout = new FileOutputStream("shareFile.txt");
                writer = new BufferedWriter(new OutputStreamWriter(fout));
                writer.write(sb.toString());
                writer.flush();
                fout.close();

            } else {
                // Read sum
                return String.valueOf(sum);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ActivityHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ActivityHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Update End");
        return String.valueOf(sum);
    }

    private static boolean timeSync(Activity activity) throws IOException {
        for (int i = 0; i < peerPort.length; i++) {
            Socket peerSocket = new Socket(peerIP, peerPort[i]);

            ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
            output.writeObject(activity);

            DataInputStream input = new DataInputStream(peerSocket.getInputStream());

            // update local time
            timeCounter = Math.max(input.readInt(), timeCounter);
            peerSocket.close();

            //test
        }

        return true;
    }

    private static boolean sendReadSkip() throws IOException {
        for (int i = 0; i < peerPort.length; i++) {
            Socket peerSocket = new Socket(peerIP, peerPort[i]);
            ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
            output.writeObject(Activity.createReadSkip());
            peerSocket.close();
        }
        return true;
    }

    private static boolean sendReadRelease() throws IOException {
        for (int i = 0; i < peerPort.length; i++) {
            Socket peerSocket = new Socket(peerIP, peerPort[i]);
            ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
            output.writeObject(Activity.createReadRelease());
            peerSocket.close();
        }
        return true;
    }

    private static boolean sendWriteRelease() throws IOException {
        for (int i = 0; i < peerPort.length; i++) {
            Socket peerSocket = new Socket(peerIP, peerPort[i]);
            ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
            output.writeObject(Activity.createWriteRelease());
            peerSocket.close();
        }
        return true;
    }

}
