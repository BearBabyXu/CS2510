/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce;

/**
 *
 * @author Rycemond
 */
public class MapReduce {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Reducer reducer = new Reducer(1111);
            reducer.start();
        }catch(Exception e) {
            
        }
        
    }
    
}
