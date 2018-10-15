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
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static Socket socket;

    public static void main(String args[]) {

        Client1 client = new Client1();

        Scanner read = new Scanner(System.in);

        // If return true then continue 
        try {
            socket = new Socket("127.0.0.1", port);
            System.out.println("connect to server");
            
            // wait until server send response
            String response = null;
            ClientRequest request = null;
            int count = 1;
            do {
                // write request
                request = new ClientRequest("Read", "A", null);
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(request);
                
                
                System.out.println("Wait for reply");
                // wait for request
                BufferedReader bw=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println(bw.readLine());
                /*
               input = new ObjectInputStream(socket.getInputStream());
                response = (String)input.readObject();
                if (response == null) {
                    System.out.println("Get null response from server");
                } else {
                    System.out.println(response);
                }
                */
                
            } while (!(read.next() == ""));

        } catch (Exception e) {

        }

    }
}
