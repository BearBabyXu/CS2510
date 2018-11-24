/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce;

/**
 *
 * @author Egan
 */
public class IndexMaster {
    
    public static void main(String args[]) {
        
        try {
            Mapper mapper = new Mapper(9999);
            mapper.start();
            
            Reducer reducer = new Reducer(1111);
            reducer.start();
        } catch (Exception e) {
            
        }
    }
}
