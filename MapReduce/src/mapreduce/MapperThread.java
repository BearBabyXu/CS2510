/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Egan
 */
public class MapperThread extends Thread{
    private final Socket socket;
    private final Mapper mapper;
    
    public MapperThread(Socket socket, Mapper mapper) {
        this.socket = socket;
        this.mapper = mapper;
    }
    
    public void run() {
        //System.out.println("MapperThread");
        try {           
            // Get indexing task from master
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            // push task into task stack
            MapperConfig task = (MapperConfig)input.readObject();
            mapper.addTask(task); 
            
        } catch (IOException ex) {
            Logger.getLogger(MapperThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MapperThread.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
    
}

