/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Request.ClientRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunClient {
    

    private final static int port = 8888;

    public static void main(String args[]) {

        Client1 client = new Client1();
        
        Scanner read = new Scanner(System.in);
        
        // If return true then continue 
        if(client.connect("127.0.0.1", port)) {
            try {
                // wait until server send response
                String response = null;
                ClientRequest request = null;
                int count = 1;
                do {
                    // write request
                    request = new ClientRequest("Read", "A", null);
                    client.output.writeObject(request);
                    
                    // wait for request
                    response = (String)client.input.readObject();
                    if (response == null) {
                        System.out.println("Get null response from server");
                    } else {
                        System.out.println(response);
                    }
                } while(!(read.next() == ""));
                
                
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
