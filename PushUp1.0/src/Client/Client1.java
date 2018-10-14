/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Request.ClientRequest;
import Request.Activity;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client1 extends Client implements ClientInterface {

    public boolean WriteRequest(int quantity, String ticket) {
        ClientRequest request = new ClientRequest("write");
        request.addToCart(quantity, ticket);

        return Send(request);
    }

    public boolean ReadRequest() {
        ClientRequest request = new ClientRequest("read");

        return Send(request);
    }

    public boolean Send(ClientRequest request) {
        try {
            output.writeObject(request);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Client1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

}
