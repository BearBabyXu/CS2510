/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author brantxu
 */
public class test {
    public static void main(String[] args) throws IOException {
      Socket socket = new Socket("127.0.0.1", 8888);

        PrintWriter pw = new PrintWriter(socket.getOutputStream());
       
        pw.println("new");

        pw.flush();
        
    }
}
