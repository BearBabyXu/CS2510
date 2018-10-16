/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Request;

import java.util.Comparator;

/**
 *
 * @author brantxu
 */
public class ActivityComparator implements Comparator<Activity>{

    @Override
    public int compare(Activity o1, Activity o2) {
       if(o1.compareTo(o2)>0){
       return -1;
       }else{
       return 1;
       }
    }
    
}
