/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server1;

import java.util.ArrayList;

/**
 *
 * @author brantxu
 */
public class ActivityHandler extends Thread {

    private static ArrayList<String> activityList = new ArrayList<>();
    private static int readCounter=0;

    public void run() {
        
            if(!activityList.isEmpty()){
            
            
            }
        
        

    }

    public boolean addActivity(String activity) {
        try {

            activityList.add(activity);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    
    public boolean execute(){
    
        return false;
    
    }
    
    
   
}
