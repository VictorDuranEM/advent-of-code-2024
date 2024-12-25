package org.example.day9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day9 {
    public static void main(String[] args) throws IOException {
        String input;
        try (var inputR = Day9.class.getResourceAsStream("/day9/input");
             var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputR)))) {
            input = reader.readLine();
        }

        int[] lengths = input.chars().map(c -> c - '0').toArray();

        List<Integer> initPositions = new ArrayList<>(List.of(0));
        List<Integer> blocks = new ArrayList<>();
        int fileID = 0;
        boolean expectingFile = true;
        for (int len : lengths) {
            initPositions.add(initPositions.getLast() + len);
            if (expectingFile) {
                for (int i = 0; i < len; i++) {
                    blocks.add(fileID);
                }
                fileID++;
            } else {
                for (int i = 0; i < len; i++) {
                    blocks.add(-1);
                }
            }
            expectingFile = !expectingFile;
        }

        //reorder1(blocks);
        reorder2(blocks, lengths, initPositions);

        long checksum = 0;
        for (int i = 0; i < blocks.size(); i++) {
            int id = blocks.get(i);
            if (id >= 0) {
                checksum += (long) i * id;
            }
        }

        System.out.println(checksum);
    }

    private static void reorder2(List<Integer> blocks, int[] lengths, List<Integer> initPositions) {
        int length = lengths.length;
        int right = length % 2 != 0 ? length - 1 : length - 2;

        while (right > 0) {
            int left = 1;
            while (left < right) {
                if (lengths[left] >= lengths[right]) {
                    var Lpos = initPositions.get(left);
                    var Rpos = initPositions.get(right);
                    var ID = blocks.get(Rpos);
    
                    for (int i = 0; i < lengths[right]; i++) {
                        blocks.set(Lpos + i, ID);
                        blocks.set(Rpos + i, -1);
                    }
    
                    lengths[left] -= lengths[right];
                    initPositions.set(left, initPositions.get(left) + lengths[right]);
                    break;        
                } else {
                    left += 2;
                }
            }
            right -= 2;
        }
    }

    private static void reorder1(List<Integer> blocks) {
        int left = 0;
        int right = blocks.size() - 1;

        while (left < right) {
            int leftValue = blocks.get(left);
            int rightValue = blocks.get(right);
            
            if (leftValue != -1) {
                left++;
                continue;
            }
            
            if (rightValue == -1) {
                right--;
                continue;
            }
            
            blocks.set(left, rightValue);
            blocks.set(right, -1);
        }
    }
}