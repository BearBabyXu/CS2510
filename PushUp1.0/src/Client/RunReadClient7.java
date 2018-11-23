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

public class RunReadClient7 {

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
            request = new ClientRequest("Read", "Server3", update);
            // send request
            output = new ObjectOutputStream(socket.getOutputStream());
            long start = System.currentTimeMillis();
            output.writeObject(request);
            output.flush();
            System.out.println("Request Sent");

            // Wait for reply    
            BufferedReader bw = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("@Read Client 7, Sum: " + bw.readLine());
            long end = System.currentTimeMillis();

            writeRes(end,start);
            TimeUnit.MILLISECONDS.sleep(500);
            
        } while (true);

    }

    private static void writeRes(long end, long start) throws IOException {
        System.out.println(end + " - " + start + ", " + Long.toString(end - start));
        File file = new File("src/time/Client7.txt");
        if(!file.exists())
            file.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
        bw.write(Long.toString(end - start) + "\n");
        bw.close();
    }
}
