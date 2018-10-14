/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server2;

/**
 *
 * @author brantxu
 */
public class Server2 {
    public static void main(String[] args) {
       new ClientServer().start();
       new PeerServer().start();
    }
}
