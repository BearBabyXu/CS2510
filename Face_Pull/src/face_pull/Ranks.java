/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package face_pull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 *
 * @author Egan
 */

public class Ranks {
    private ArrayList<Rank> rankList;
    private ArrayList<String> fileList;

    public Ranks () {
        rankList = new ArrayList<>();
        fileList = new ArrayList<>();
    }
    
    public void addListFactor(LinkedList<Posting> list) {
        for(Posting p: list) {
            String fileName = p.getFileName();
            if(!fileList.contains(fileName)) {
                fileList.add(fileName);
                rankList.add(new Rank(fileName));
            }
            int index = fileList.indexOf(fileName);
            rankList.get(index).addValue(p.getOccurence());
        }
    }
    
    public void searchResult() {
        if(rankList.size() == 0)
            System.err.println("            Result not Found        ");
        Collections.sort(rankList, new SortbyRank());
        for(int i = rankList.size()-1; i >= 0 ; i--) 
            System.out.println(rankList.get(i));
    }
}

class Rank {
    private String fileName;
    private int rank;
    
    public Rank(String fileName) {
        this.fileName = fileName;
        rank = 0;
    }
    
    public void addValue(int value) {
        rank += value;
    }
    
    public String toString() {
        return String.format(" % 8d ; %s", rank, fileName);
    }
    
    public int getValue() {
        return rank;
    }
 }

class SortbyRank implements Comparator<Rank> {

    public int compare(Rank r1, Rank r2) {
        return r1.getValue() - r2.getValue();
    }
    
    public int reverse(Rank r1, Rank r2) {
        return r1.getValue() - r2.getValue();
    }
} 
