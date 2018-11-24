/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.io.Serializable;

/**
 *
 * @author Egan
 */
public class IndexRequest implements Serializable{

    private String File_Directory;
    private int type;
    private String query;

    public IndexRequest(int type) {
        this.type = type;
        this.File_Directory = null;
        this.query = null;
    }

    public String getFileDirectory() {
        return this.File_Directory;
    }
    
    public void addQuery(String query) {
        this.query = query;
    }
    
    public void addFileDirectory(String fileDirectory) {
        this.File_Directory = fileDirectory;
    }
    
    public String getQuery() {
        return query;
    }
    
    public int getType() {
        return type;
    }
}
