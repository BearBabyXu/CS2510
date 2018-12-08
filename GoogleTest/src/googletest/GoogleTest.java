/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googletest;

/**
 *
 * @author brantxu
 */
public class GoogleTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

    }
    
    public int solution(int[] S, int[] E) {
        // write your code in Java SE 8
        int max=0;
        
        for(int i=0;i<S.length;i++){
            max=Math.max(max, numChairs(S,E,S[i]));
        }
        return max;
    }
    
    public int numChairs(int[]S, int[] E, int time ){
        if(S.length==0|| E.length==0){
            return 0;
        }
        int count=0;
        for(int i=0;i<S.length;i++){
            if(S[i]<=time && E[i]>time ){
                count++;
            }
            
        
        }
            
    return count;
    
    }

    public int solution(int[] A) {
        // write your code in Java SE 8
        
        int length = A.length;
        int count=0;
        
        for (int i = 0; i < length; i++) {
            if(jump(A,i,true)){
            count++;
            }
        }
        return count;
    }

    public boolean jump(int[] src, int start, boolean ifOdd) {

        int length = src.length;
        
        if(length==0){
            return false;
            
        }
        if(start==src.length-1){
            return true;
        }
        
        int next = start;

        if (ifOdd) {
            int nextIndex = findMin(src, start);
            if (nextIndex == -1) {
                return false;
            } else if (nextIndex == src.length - 1) {
                return true;

            } else {
                return jump(src, nextIndex, !ifOdd);

            }
        } else {
            int nextIndex = findMax(src, start);
            if (nextIndex == -1) {
                return false;
            } else if (nextIndex == src.length - 1) {
                return true;

            } else {
                return jump(src, nextIndex, !ifOdd);

            }
        }

    }

    public int findMin(int src[], int start) {

        int base = src[start];
        int realStart = start;
        int min = 0;
        int minIndex = 0;

        boolean findRealStart = false;

        while (!findRealStart) {
            realStart++;
            if (realStart == src.length) {
                return -1;
            }

            if (src[realStart] > base) {
                findRealStart = true;
                min = src[realStart];
                minIndex = realStart;
            }
        }

        for (int i = realStart + 1; i < src.length; i++) {
            if (src[i] <= min && src[i] > base) {
                min = src[i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    public int findMax(int src[], int start) {

        int base = src[start];
        int realStart = start;
        int max = 0;
        int maxIndex = 0;

        boolean findRealStart = false;

        while (!findRealStart) {
            realStart++;
            if (realStart == src.length) {
                return -1;
            }

            if (src[realStart] < base) {
                findRealStart = true;
                max = src[realStart];
                maxIndex = realStart;
            }
        }

        for (int i = realStart + 1; i < src.length; i++) {
            if (src[i] >= max && src[i] < base) {
                max = src[i];
                maxIndex = i;
            }
        }

        return maxIndex;

    }

}
