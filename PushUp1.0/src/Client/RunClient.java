/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

public class RunClient {
    
    private final static int port = 8889;
    
    public static void main(String args[]) {
        
        Client1 client = new Client1();
        client.connect("127.0.0.1", port);
        
        System.out.printf("Connected to file server with address %s on port %d \n", "127.0.0.1", port);
			
    }
   
}
