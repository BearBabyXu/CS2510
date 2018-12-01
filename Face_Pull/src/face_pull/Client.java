/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package face_pull;

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
        Ranks ranks = null;

        try {
            String req = "";

            do {
                Socket socket = new Socket("136.142.227.15", 8888);
                Scanner read = new Scanner(System.in);
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
                    out = new ObjectOutputStream(socket.getOutputStream());
                    System.out.print("Keywords >>> ");
                    String keyword = read.nextLine();
                    System.out.print("Ranking Type: 0 - Ranking ; 1 - Matchings \n>>> ");
                    String rankType = read.next();
                    if (rankType.equals("0") || rankType.equals("1")) {
                        keyword = checkIllegalKeyword(keyword);
                        int length = keyword.split(" ").length;
                        int count = 0;
                        request = new Request(1);
                        request.addQuery(keyword);
                        
                        ranks = new Ranks(Integer.parseInt(rankType));
                        out.writeObject(request);

                        System.err.println("");
                        System.err.println("****************************************");
                        System.err.println("***** Tiny Google Searching Result *****");
                        System.err.println("****************************************");
                        in = new ObjectInputStream(socket.getInputStream());

                        while (count++ < length) {
                            SearchResult temp = (SearchResult) in.readObject();
                            ranks.addListFactor(temp.getList());
                        }
                        ranks.searchResult();
                        System.out.println();
                    } else {
                        System.err.println("Wrong ranking type");
                    }

                } else {
                    System.err.println("Unknown Request");
                }
                socket.close();
            } while (!req.toLowerCase().equals("exit"));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String checkIllegalKeyword(String input) {
        String[] keyWords = input.split(" ");
        ArrayList<String> temps = new ArrayList<>();

        // eliminate all illegal keyword
        for (String word : keyWords) {
            if (Character.isAlphabetic(word.charAt(0))) {
                temps.add(word.toLowerCase());
            } else {
                System.err.printf("%s is an illegal keyword. \n", word);
            }
        }
        
        String keyword = "";
        for (String word : temps)
            keyword = keyword + word + " ";
        keyword = keyword.substring(0, keyword.length()-1);

        return keyword;
    }
}
