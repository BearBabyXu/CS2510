/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package face_pull;

import java.io.Serializable;

/**
 *
 * @author Egan
 */
public class Posting implements Serializable {
    private String File_Source;
    private int Occurence;
    
    public Posting(String _FILESOURCE, int _OCCURENCE) {
        this.File_Source = _FILESOURCE;
        this.Occurence = _OCCURENCE;
    }
    
    public String getFileName() {
        return File_Source;
    }
    
    public int getOccurence() {
        return Occurence;
    }
}
