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

    public IndexRequest(String _FileDirectory) {
        this.File_Directory = _FileDirectory;
    }

    public String getFileDirectory() {
        return this.File_Directory;
    }
}
