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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunClient {

    private final static int port = 8888;
    protected static Socket socket;
    protected static ObjectOutputStream output;
    protected static ObjectInputStream input;

    public static void main(String args[]){

        try {
            //Client1 client = new Client1();
            socket = new Socket("127.0.0.1", port);
            output = new ObjectOutputStream(socket.getOutputStream());
            
        } catch (IOException ex) {
            Logger.getLogger(RunClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scanner read = new Scanner(System.in);
        
        try {
            // wait until server send response
            String response = null;
            ClientRequest request = null;
            int count = 1;
            
                // write request
                request = new ClientRequest("Read", "A", null);
                output.writeObject(request);
                System.out.println("Request sent");
                
                
                BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println(br.readLine());
                

                /*
                input = new ObjectInputStream(socket.getInputStream());
                // wait for request
                //response = client.newInputStream();
                response = (String)input.readObject();
                if (response == null) {
                    System.out.println("Get null response from server");
                } else {
                    System.out.println(response);
                }
            */

        } catch (IOException ex) {
            Logger.getLogger(RunClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
