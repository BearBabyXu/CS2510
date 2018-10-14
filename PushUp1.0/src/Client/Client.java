/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Request.Request;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Client {
    
    protected Socket socket;
    protected ObjectOutputStream output;
    protected ObjectInputStream input;
    
    public boolean connect(final String server, final int port) {
	System.out.println("attempting to connect");
        
	try {
            socket = new Socket(server, port);
            System.out.println("Connected to " + server + " on port " + port);
            
            //PrintWriter pw=new PrintWriter(socket.getOutputStream());
            //pw.write("to8889");
            //pw.flush();
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            
            output.writeObject("Get Message?");
		    
            return true;
			
	} catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            System.err.println("Error: " + "Unknown Host");
            e.printStackTrace();
	} catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
	}
	
        return false;
    }
}
