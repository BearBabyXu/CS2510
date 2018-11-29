/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

/**
 *
 * @author brantxu
 */
public class SearcherConfig {
    private String keyWord;
    private int id;
    public SearcherConfig(String keyWord, int id){
        
        this.keyWord=keyWord;
        this.id=id;
  
    }
    
    public String getKeyword(){
        return this.keyWord;
    }
    
    public int getId(){
        return this.id;
    }
    
}
