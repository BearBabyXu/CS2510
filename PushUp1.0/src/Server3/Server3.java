/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server3;



/**
 *
 * @author brantxu
 */
public class Server3 {
    public static void main(String[] args) {
       System.out.println("Server3");
       new ClientServer().start();
       new PeerServer().start();
    }
}
