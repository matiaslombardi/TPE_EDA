package utils;

import java.util.HashMap;
import java.util.Map;

public class QGrams {
    private final int n;

    public QGrams(int n) {
        this.n = n;
    }

    public Map<String,Integer> generateGrams(String string){ // Orden n
        StringBuilder numerales = new StringBuilder();
        StringBuilder aux = new StringBuilder(string);
        for(int i = 0 ; i < n - 1 ; i++){ // Orden 2
            numerales.append("#");
            aux.append("#");
        }
        numerales.append(aux);
        string = new String(numerales); // Orden 5
        Map<String, Integer> toReturn = new HashMap<>();
        for(int i = 0 ; i <= string.length() - n ; i++){ // Orden n
            String key = string.substring(i, i + n); // Orden 3
            int value = toReturn.getOrDefault(key,0);
            toReturn.put(key, value + 1);
        }
        return toReturn;
    }

    public double similarity(String first, String second){
        Map<String,Integer> firstMap = generateGrams(first); // Orden n = first.length
        Map<String,Integer> secondMap = generateGrams(second); // Orden m = second.length
        Integer firstSize = 0;
        Integer secondSize = 0;
        int inCommon = 0;
        for (Integer amount:firstMap.values())  // Orden n (Si son todos gramas diferentes)
            firstSize += amount;

        for (Integer amount:secondMap.values())  // Orden m
            secondSize += amount;

        for (Map.Entry<String, Integer> firstEntry :firstMap.entrySet()) { // n
            for (Map.Entry<String, Integer> secondEntry :secondMap.entrySet()) { // m
                if(firstEntry.getKey().equals(secondEntry.getKey()))
                    inCommon += Math.min(firstEntry.getValue(),secondEntry.getValue()) * 2;
            }
        } // n*m
        int total = firstSize + secondSize;
        double numerator = inCommon;
        return numerator / total;
    }
}
