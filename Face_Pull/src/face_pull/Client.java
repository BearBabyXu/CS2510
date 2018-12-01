/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Egan
 */
public class Client {

    public static void main(String args[]) throws ClassNotFoundException {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        Request request = null;

        try {
            String req = "";
            
            do {
                Socket socket = new Socket("127.0.0.1", 8888);
                Scanner read = new Scanner(System.in);
                out = new ObjectOutputStream(socket.getOutputStream());
                System.out.print("Request Type >>> ");
                req = read.nextLine();
                if (req.toLowerCase().equals("exit")) {
                    break;
                }
                if (req.toLowerCase().equals("index")) {
                    System.out.print("File Directory >>> ");
                    String file = read.nextLine();
                    //String file = "/Users/brantxu/Documents/GitHub/CS2510/Face_Pull/files";
                    //"/Users/Rycemond/Documents/UPitts/CS2510/P2/MapReduce/CS2510/Face_Pull/files"
                    request = new Request(0);
                    request.addFileDirectory(file);
                    out.writeObject(request);
                } else if (req.toLowerCase().equals("search")) {

                    System.out.print("Keywords >>> ");
                    String keyword = read.nextLine();
                    String[] keywords = keyword.split(" ");

                    int length = keywords.length;
                    int count = 0;
                    request = new Request(1);
                    request.addQuery(keyword);
                    out.writeObject(request);                   

                    System.err.println("");
                    System.err.println("****************************************");
                    System.err.println("***** Tiny Google Searching Result *****");
                    System.err.println("****************************************");
                    in = new ObjectInputStream(socket.getInputStream());
                    Ranks ranks = new Ranks();
                    while (count++ < length) {
                        SearchResult temp = (SearchResult) in.readObject();
                        ranks.addListFactor(temp.getList());
                    }
                    ranks.searchResult();
                    System.out.println();
                } else {
                    System.err.println("Unknown Request");
                }
                socket.close();
            } while (!req.toLowerCase().equals("exit"));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
