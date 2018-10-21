/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Request.ClientRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class RunReadClient8 {

    private final static int port = 8889;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static Socket socket;

    public static void main(String args[]) throws InterruptedException, IOException {

        Client1 client = new Client1();

        Scanner read = new Scanner(System.in);

        String update = "0";

        do {
            socket = new Socket("127.0.0.1", port);

            String response = null;
            ClientRequest request = null;
            
            // create request message
            request = new ClientRequest("Read", "Server2", update);
            // send request
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(request);
            output.flush();
            System.out.println("Request Sent");

            // Wait for reply    
            BufferedReader bw = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("@Read Client 8, Sum: " + bw.readLine());

            TimeUnit.MILLISECONDS.sleep(500);
            
        } while (true);

    }

}
