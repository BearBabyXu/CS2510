/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server1;

import Request.Activity;
import Request.ClientRequest;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
    private static final int peerPort = 9998;

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
            String result = Operate(activity);
            sendReadRelease();
            readCounter--;

            return result;

        } else {
            while (activityList.get(0).getRequesterId() != activity.getRequesterId() || readCounter != 0) {
            }

            String result = Operate(activity);
            sendWriteRelease();

            return result;

        }

    }

    private static String Operate(Activity activity) {
        FileInputStream fis = null;
        FileOutputStream fout = null;
        BufferedReader reader = null;
        
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
                System.out.println(line);
            }

            fis.close();

            for (String each : data) {
                // If target line is found
                if (each.substring(0, 1).equals(target)) {
                    // get target index
                    int index = data.indexOf(each);
                    // get old value of index
                    int oldValue = Integer.parseInt(each.substring(3));

                    // data update
                    String updated = target + ", " + Integer.toString(oldValue - Integer.parseInt(update));
                    data.set(index, updated);
                    
                    return updated;
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
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

    private static boolean sendReadSkip() throws IOException {
        Socket peerSocket = new Socket(peerIP, peerPort);
        ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(Activity.createReadSkip());
        peerSocket.close();

        return true;
    }

    private static boolean sendReadRelease() throws IOException {
        Socket peerSocket = new Socket(peerIP, peerPort);
        ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(Activity.createReadRelease());
        peerSocket.close();

        return true;
    }

    private static boolean sendWriteRelease() throws IOException {
        Socket peerSocket = new Socket(peerIP, peerPort);
        ObjectOutputStream output = new ObjectOutputStream(peerSocket.getOutputStream());
        output.writeObject(Activity.createWriteRelease());
        peerSocket.close();

        return true;
    }

}
