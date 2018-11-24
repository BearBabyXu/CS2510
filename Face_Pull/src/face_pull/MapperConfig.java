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
public class MapperConfig implements Serializable {
    private String File_Directory;
    private int Mapper_ID;
    private int Mapper_Count;
    private int Reducer_Count;
    
    public MapperConfig(String _FILEDIRECTORY, int _MAPPERID, int _REDUCERCOUNT, int _MAPPERCOUNT) {
        this.File_Directory = _FILEDIRECTORY;
        this.Mapper_ID = _MAPPERID;
        this.Mapper_Count = _MAPPERCOUNT;
        this.Reducer_Count = _REDUCERCOUNT;
    }
    
    public String getFileDirectory() {
        return File_Directory;
    }
    
    public int getMapperID() {
        return Mapper_ID;
    }
    
    public int getReducerCount() {
        return Reducer_Count;
    }
    
    public int getMapperCount() {
        return Mapper_Count;
    }
}
