/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package face_pull;

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
    private int rankType;

    public Ranks (int type) {
        rankList = new ArrayList<>();
        fileList = new ArrayList<>();
        rankType = type;
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
            rankList.get(index).addMatch();
        }
    }
    
    public void searchResult() {
        String format = "";
        if(rankList.size() == 0)
            System.err.println("            Result not Found        ");
        
        switch(rankType) {
            case 1:
                // Sort by match
                Collections.sort(rankList, new SortbyMatch());
                break;
            default:
                // Sort by rank value
                Collections.sort(rankList, new SortbyRank());
                break;
        }
        
        for(int i = rankList.size()-1; i >= 0 ; i--) {
            System.out.println(rankList.get(i).toString(rankType));
        }
    }
}

class Rank {
    private String fileName;
    private int rank;
    private int match;
    
    public Rank(String fileName) {
        this.fileName = fileName;
        rank = 0;
        match = 0;
    }
    
    public void addValue(int value) {
        rank += value;
    }
    
    public int getValue() {
        return rank;
    }
    
    public void addMatch() {
        match += 1;
    }
    
    public int getMatch() {
        return match;
    }
    
    public String toString(int type) {
        switch(type) {
            case 1:
                return String.format(" % 8d ; %s", match, fileName);
            default:
                return String.format(" % 8d ; %s", rank, fileName);
        }
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

class SortbyMatch implements Comparator<Rank> {

    public int compare(Rank r1, Rank r2) {
        return r1.getMatch() - r2.getMatch();
    
    }

}
