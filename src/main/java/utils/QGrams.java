package utils;

import java.util.HashMap;
import java.util.Map;

public class QGrams {
    private final int n;

    public QGrams(int n) {
        this.n = n;
    }

    public Map<String,Integer> generateGrams(String string){
        String numerales = "";
        for(int i = 0 ; i < n - 1 ; i++){
            numerales = numerales.concat("#");
            string = string.concat("#");
        }
        string = numerales.concat(string);
        Map<String, Integer> toReturn = new HashMap<>();
        for(int i = 0 ; i <= string.length() - n ; i++){
            String key = string.substring(i, i + n);
            int value = toReturn.getOrDefault(key,0);
            toReturn.put(key, value + 1);
        }
        return toReturn;
    }

    public double similarity(String first, String second){
        Map<String,Integer> firstMap = generateGrams(first);
        Map<String,Integer> secondMap = generateGrams(second);
        Integer firstSize = 0;
        Integer secondSize = 0;
        int inCommon = 0;
        for (Integer amount:firstMap.values()) {
            firstSize += amount;
        }
        for (Integer amount:secondMap.values()) {
            secondSize += amount;
        }
        for (Map.Entry<String, Integer> firstEntry :firstMap.entrySet()) {
            for (Map.Entry<String, Integer> secondEntry :secondMap.entrySet()) {
                if(firstEntry.getKey().equals(secondEntry.getKey())){
                    inCommon += Math.min(firstEntry.getValue(),secondEntry.getValue()) * 2;
                }
            }
        }
        int total = firstSize + secondSize;
        double numerator = inCommon;
        return numerator / total;
    }
}
