/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Request.ClientRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class RunClient2 {
    private final static int port = 8889;
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
            
            // Testing
            Random random = new Random();
            int i = 0;
            
            do {
                i = random.nextInt(2);
                if(i == 0) {
                    request = new ClientRequest("Read", "A", null);
                    System.out.printf("Send request: %s, %s, %s \n", "Read", "A");
                } else {
                    request = new ClientRequest("Write", "A", "2");
                    System.out.printf("Send request: %s, %s, %s \n", "Write", "A", "2");
                }
                
                // write request
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(request);
                
                
                System.out.println("------ Wait for reply -----");
                // wait for request
                BufferedReader bw=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println(bw.readLine());
                
                // Delay in 1~3 seconde
                TimeUnit.SECONDS.sleep(random.nextInt(2)+1);
                
            } while (!true);

        } catch (Exception e) {

        }

    }
}
