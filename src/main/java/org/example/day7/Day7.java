package org.example.day7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Day7 {
    public static void main(String[] args) throws IOException {
        try (var input = Day7.class.getResourceAsStream("/day7/input");
             var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(input)))) {


            BigInteger totalSum = BigInteger.ZERO;
            
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.isBlank()) {
                    continue;
                }
                
                String[] parts = line.split(":");
                if (parts.length < 2) {
                    continue;
                }

                String testValueStr = parts[0];
                String[] numbersStr = parts[1].strip().split("\\s");
                
                BigInteger testValue = new BigInteger(testValueStr);
                BigInteger[] numbers = Arrays.stream(numbersStr).map(BigInteger::new).toArray(BigInteger[]::new);
                
                if (canFormValue(testValue, numbers)) {
                    totalSum = totalSum.add(testValue);
                }
            }

            System.out.println(totalSum);
        }
    }

    private static boolean canFormValue(BigInteger testValue, BigInteger[] numbers) {
        if (numbers.length == 0) return false;
        if (numbers.length == 1) {
            return numbers[0].equals(testValue);
        }
        Set<BigInteger> currentProcessed = new HashSet<>();
        currentProcessed.add(numbers[0]);
        
        for (int i = 1; i < numbers.length; i++) {
            var nextNum = numbers[i];
            Set<BigInteger> newProcessed = new HashSet<>();
            for (BigInteger n : currentProcessed) {
                newProcessed.add(n.add(nextNum));
                newProcessed.add(n.multiply(nextNum));
                int digits = nextNum.toString().length();
                BigInteger base = BigInteger.TEN.pow(digits);
                newProcessed.add(n.multiply(base).add(nextNum));
            }
            currentProcessed = newProcessed;
        }
        
        return currentProcessed.contains(testValue);
    }
}
