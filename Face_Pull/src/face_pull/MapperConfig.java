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
public class MapperConfig implements Serializable{
    private String File_Directory;
    private int Mapper_Count;
    private int Reducer_Count;
    private int File_Index;
    
    public MapperConfig(String _FILEDIRECTORY, int _MAPPERCOUNT, int _REDUCERCOUNT, int _FILEINDEX) {
        this.File_Directory = _FILEDIRECTORY;
        this.Mapper_Count = _MAPPERCOUNT;
        this.Reducer_Count = _REDUCERCOUNT;
        this.File_Index = _FILEINDEX;
    }
    
    public String getFileDirectory() {
        return File_Directory;
    }
    
    public int getMapperCount() {
        return Mapper_Count;
    }
    
    public int getReducerCount() {
        return Reducer_Count;
    }
    
    public int getFileIndex() {
        return File_Index;
    }
}
