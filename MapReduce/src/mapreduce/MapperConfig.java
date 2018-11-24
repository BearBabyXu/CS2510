/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce;

import java.io.Serializable;

/**
 *
 * @author Egan
 */
public class MapperConfig implements Serializable{
    private String File_Dir;
    private int Mapper_Count;
    private int Reducer_Count;
    private int File_Index;
    
    public MapperConfig(String File_Dir, int Mapper_Count, int Reducer_Count, int File_Index) {
        this.File_Dir = File_Dir;
        this.Mapper_Count = Mapper_Count;
        this.Reducer_Count = Reducer_Count;
        this.File_Index = File_Index;
    }
    
    public String getFileDirectory() {
        return File_Dir;
    }
    
    public int getMapperCount() {
        return Mapper_Count;
    }
    
    public int getFileIndex() {
        return File_Index;
    }
    
    public int getReducerCount() {
        return Reducer_Count;
    }
}
