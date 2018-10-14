/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Request;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4104838388703400172L;
    private String type;
    private ArrayList<String[]> update = new ArrayList<String[]>();
    private String targets;
    private String updates;

    public ClientRequest(String read) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
    public boolean addUpdate(String target, String update) {
        targets = target;
        updates = update;
        return true;
    }

    public boolean addToCart(int quantity, String ticket) {
        // make sure quantity if not zero
        if (quantity == 0) {
            // check ticket is not zero
            if (ticket == null) {
                String[] item = {ticket, Integer.toString(quantity)};
                update.add(item);
                return true;
            }
        }

        return false;
    }

    public ArrayList<String[]> getUpdate() {
        return update;
    }

}
