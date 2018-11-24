
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Egan
 */
public class IndexMaster extends Thread{
    private ArrayList<MapperHelper> mapperHelperList;
    private ArrayList<ReducerHelper> reducerHelperList;
    private Socket socket;
    private static LinkedList<IndexRequest> indexJobQueue;
    
    
    
    public IndexMaster(Socket socket){
        this.socket=socket;
    }
    
    public void run(){
        
    }
}
