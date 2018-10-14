/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Request.Activity;
import Request.ClientRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class Client {
    
    protected Socket socket;
    protected ObjectOutputStream output;
    protected ObjectInputStream input;
    
    public boolean connect(final String server, final int port) {
	System.out.println("attempting to connect");
        
	try {
            socket = new Socket(server, port);
            System.out.println("Connected to " + server + " on port " + port);
            
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            
            ClientRequest req;
            req = new ClientRequest("write");
            output.writeObject(req);
            
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
