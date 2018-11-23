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
import java.io.FileReader;
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

public class RunWriteClient1 {

    private final static int port = 8888;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static Socket socket;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        
        fileDivide(3);

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
                
                System.out.println("@Write Client 1, Update: " + update);
                // create request message
                request = new ClientRequest("Write", "Server2", update);
                // send request
                output = new ObjectOutputStream(socket.getOutputStream());
                long start = System.currentTimeMillis();
                output.writeObject(request);
                output.flush();
                System.out.println("Request Sent");

                // Wait for reply    
                BufferedReader bw = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("@Write Client 1, Sum: " + bw.readLine());
                long end = System.currentTimeMillis();
                writeRes(end,start);

            } else {
                System.out.println("Reach End");
            }
        } while (!(update.equals("null")));
        
        File file = new File("data1.txt");
        file.delete();
        socket.close();
    }

    private static String readData() throws FileNotFoundException, IOException {
        String result = null;
        FileInputStream fis = new FileInputStream("data1.txt");
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

        FileWriter fw = new FileWriter(new File("data1.txt"));
        fw.write(sb.toString());
        fw.close();

        return result;
    }
    
    private static void fileDivide(int SERVER_COUNT) {
        File file = new File("data.txt");

        try {
            BufferedReader rd = new BufferedReader(new FileReader(file));
            String line = "";
            List<String> buffer = new ArrayList<String>();
            int line_count = 0;

            while ((line = rd.readLine()) != null) {
                line_count++;
                buffer.add(line);
            }

            File fw = new File("data1.txt");
            BufferedWriter wr = new BufferedWriter(new FileWriter(fw));
            int file_num = 1;

            for (int i = 1; i <= line_count; i++) {
                if (i % (line_count / SERVER_COUNT) == 0 && file_num < SERVER_COUNT) {
                    wr.close();
                    fw = new File("data" + ++file_num + ".txt");
                    wr = new BufferedWriter(new FileWriter(fw));

                }
                wr.write(buffer.get(i - 1) + "\n");
            }

            wr.close();
            System.err.println("Data division done!");

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void writeRes(long end, long start) throws IOException {
        System.out.println(end + " - " + start + ", " + Long.toString(end - start));
        File file = new File("src/time/Client1.txt");
        if(!file.exists())
            file.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
        bw.write(Long.toString(end - start) + "\n");
        bw.close();
    }
}
