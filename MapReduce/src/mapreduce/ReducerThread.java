/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Egan
 */
public class ReducerThread extends Thread {
    private final Socket socket;
    private final Reducer reducer;
    
    public ReducerThread(Socket socket, Reducer reducer) {
        this.socket = socket;
        this.reducer = reducer;
    }
    
    public void run() {
        try {    
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());       
            while(true) {
            //System.out.println(socket.getInetAddress());
            // Get indexing task from master
                //TimeUnit.SECONDS.sleep(1);
            // push task into task stack
                ReducerConfig task = (ReducerConfig)input.readObject();
                System.out.println("reducer new task: " + task.getFileSource());
                reducer.addTask(task); 
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MapperThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MapperThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
