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

    public boolean SendWriteRequest(String target, int quantity) {
        ClientRequest request = new ClientRequest("write");
        request.addUpdate(Integer.toString(quantity), target);

        return Send(request);
    }

    public boolean SendReadRequest(String target) {
        ClientRequest request = new ClientRequest("read");
        request.addUpdate(null, target);

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
