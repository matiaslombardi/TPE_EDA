package utils;

public class Levenshtein {
    public static int distance(String first, String second){
        int firstLength = first.length();
        int secondLength = second.length();
        int[][] distance = new int[firstLength + 1][secondLength + 1];
        for(int i = 0 ; i <= firstLength ; i++){
            distance[i][0] = i;
        }
        for(int j = 0 ; j <= secondLength ; j++){
            distance[0][j] = j;
        }
        for( int i = 1 ; i <= firstLength ; i++ ){
            for( int j = 1 ; j <= secondLength ; j++ ){
                distance[i][j] = Math.min(distance[i-1][j] + 1 ,
                        Math.min(distance[i][j-1] + 1 ,
                                distance[i-1][j-1] + (first.charAt(i-1) == second.charAt(j-1) ? 0:1) ));
            }
        }
        return distance[firstLength][secondLength];
    }

    public static double normalizedSimilarity(String first, String second){
        return 1 - (double)distance(first,second)/Math.max(first.length(),second.length());
    }
}
