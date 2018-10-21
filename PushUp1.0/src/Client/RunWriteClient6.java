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
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunWriteClient6 {

    private final static int port = 8889;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static Socket socket;

    public static void main(String args[]) throws IOException, ClassNotFoundException {

        Client1 client = new Client1();

        Scanner read = new Scanner(System.in);

        String update = "0";

        do {

            String response = null;
            ClientRequest request = null;

            // Get data from data.txt
            update = String.valueOf(readData());
            if (!update.equals("null")) {
                try {
                socket = new Socket("127.0.0.1", port);
                //System.out.println("connect to server");

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
                
                System.out.println("@Write Client 6, Update: " + update);
                // create request message
                request = new ClientRequest("Write", "Server1", update);
                // send request
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(request);
                output.flush();
                System.out.println("Request Sent");

                // Wait for reply    
                BufferedReader bw = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("@Write Client 6, Sum: " + bw.readLine());

            } else {
                System.out.println("Reach End");
            }
        } while (!(update.equals("null")));
        
        File file = new File("data6.txt");
        file.delete();
        socket.close();
    }

    private static String readData() throws FileNotFoundException, IOException {
        String result = null;
        FileInputStream fis = new FileInputStream("data6.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

        String line = "";
        boolean firstLine = true;
        StringBuffer sb = new StringBuffer("");
        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                result = line;
            } else {
                sb.append(line + "\n");
            }
            firstLine = false;
        }

        reader.close();
        fis.close();

        FileWriter fw = new FileWriter(new File("data6.txt"));
        fw.write(sb.toString());
        fw.close();

        return result;
    }

}
