/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunClient {
    

    private final static int port = 8889;

    public static void main(String args[]) {

        Client1 client = new Client1();
        
        // If return true then continue 
        if(client.connect("127.0.0.1", port)) {
            try {
                // wait until server send response
                String response = null;
                response = (String)client.input.readObject();
                if (response == null) {
                    System.out.println("Get null response from server");
                } else {
                    System.out.println(response);
                }
                
                client.disconnect();
                
            } catch (IOException ex) {
                Logger.getLogger(RunClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RunClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            System.err.println("Fail to connect with server.");
        }

    }

}
