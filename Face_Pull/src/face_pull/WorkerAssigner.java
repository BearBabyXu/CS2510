/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brantxu
 */
public class WorkerAssigner extends Thread {

    private Socket socket;
    private ObjectInputStream input;
    private static int startPort = 10000;
    private static int endPort = 15000;
    private static int currentPort = startPort;

    public WorkerAssigner(Socket socket) {

    }

    public void run() {

        try {
            input = new ObjectInputStream(socket.getInputStream());
            Config config = (Config) input.readObject();

            switch (config.getType()) {
                case 0:
                   
                    break;

                case 1:
                    break;

                case 2:

                    boolean ifFound = false;

                    while (!ifFound) {
                        if (currentPort > endPort) {
                            currentPort = startPort;
                        }
                        if (available(currentPort)) {

                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeInt(currentPort);
                            dos.close();
                            ifFound = true;
                        }
                        currentPort++;

                    }

                    break;

            }

        } catch (IOException ex) {
            Logger.getLogger(WorkerAssigner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WorkerAssigner.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static boolean available(int port) {
        System.out.println("--------------Testing port " + port);
        Socket s = null;
        try {
            s = new Socket("localhost", port);

            // If the code makes it this far without an exception it means
            // something is using the port and has responded.
            System.out.println("--------------Port " + port + " is not available");
            return false;
        } catch (IOException e) {
            System.out.println("--------------Port " + port + " is available");
            return true;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    throw new RuntimeException("You should handle this error.", e);
                }
            }
        }
    }
}
