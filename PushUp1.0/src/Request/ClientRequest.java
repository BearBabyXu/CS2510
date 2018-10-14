/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Request;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Rycemond
 */
public class ClientRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4104838388703400172L;

    private String type;
    private String target;
    private String update;

    private ArrayList<String[]> updates = new ArrayList<String[]>();

    public ClientRequest(String type, String target, String update) {
        this.type = type;
        this.target = target;
        this.update = update;
    }

    public ClientRequest(String type, String target) {
        this.type = type;
        this.target = target;

    }

    public ClientRequest(String type) {
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public String getUpdate() {
        return update;
    }

    public String getTarget() {
        return target;
    }

    public boolean addUpdate(String target, String update) {
        this.target = target;
        this.update = update;
        return true;
    }

    
    
    
    public boolean addToCart(int quantity, String ticket) {
        // make sure quantity if not zero
        if (quantity == 0) {
            // check ticket is not zero
            if (ticket == null) {
                String[] item = {ticket, Integer.toString(quantity)};
                updates.add(item);
                return true;
            }
        }

        return false;
    }

    public ArrayList<String[]> getUpdates() {
        return updates;
    }

}
