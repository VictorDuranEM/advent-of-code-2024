package org.example.day5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;

public class Day5 {

    record Rule(int before, int after) {}

    public static void main(String[] args) throws URISyntaxException, IOException {
        List<String> allLines;
        try (var in = Day5.class.getResourceAsStream("/day5/input");
             var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))) {
            allLines = reader.lines().toList();
        }

        int separatorIndex = -1;
        for (int i = 0; i < allLines.size(); i++) {
            String line = allLines.get(i).strip();
            if (!line.isBlank() && !line.contains("|")) {
                separatorIndex = i;
                break;
            }
        }

        List<Rule> rules = new ArrayList<>();
        for (int i = 0; i < separatorIndex; i++) {
            String line = allLines.get(i).strip();
            if (line.isBlank()) continue;
            var parts = line.split("\\|");
            int before = Integer.parseInt(parts[0].strip());
            int after = Integer.parseInt(parts[1].strip());
            rules.add(new Rule(before, after));
        }

        List<List<Integer>> updates = new ArrayList<>();
        for (int i = separatorIndex; i < allLines.size(); i++) {
            String line = allLines.get(i).strip();
            if (line.isBlank()) continue;
            var pages = Stream.of(line.split(","))
                              .map(String::strip)
                              .map(Integer::parseInt)
                              .toList();
            updates.add(pages);
        }
        
        int sumOfMiddlesCorrect = 0;
        int sumOfMiddlesIncorrect = 0;
        
        for (var update : updates) {
            if (isCorrectlyOrdered(update, rules)) {
                sumOfMiddlesCorrect += update.get(update.size() / 2);
            } else {
                var reordered = reorderUpdate(update, rules);
                sumOfMiddlesIncorrect += reordered.get(reordered.size() / 2);
            }
        }

        System.out.println("Part One: " + sumOfMiddlesCorrect);
        System.out.println("Part Two: " + sumOfMiddlesIncorrect);
    }

    private static boolean isCorrectlyOrdered(List<Integer> update, List<Rule> rules) {
        var positionMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < update.size(); i++) {
            positionMap.put(update.get(i), i);
        }
        
        for (var rule : rules) {
            var before = rule.before();
            var after = rule.after();

            if (positionMap.containsKey(before) && positionMap.containsKey(after)) {
                if (positionMap.get(before) > positionMap.get(after)) {
                    return false;
                }
            }
        }
        
        return true;
    }

    private static List<Integer> reorderUpdate(List<Integer> update, List<Rule> rules) {
        Set<Integer> pagesInUpdate = new HashSet<>(update);
        
        Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();

        for (int page : pagesInUpdate) {
            adjacencyList.put(page, new ArrayList<>());
            inDegree.put(page, 0);
        }
        
        for (var rule : rules) {
            int before = rule.before();
            int after = rule.after();
            
            if (pagesInUpdate.contains(before) && pagesInUpdate.contains(after)) {
                adjacencyList.get(before).add(after);
                inDegree.put(after, inDegree.get(after) + 1);
            }
        }
        
        Queue<Integer> queue = new LinkedList<>();
        for (var entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        List<Integer> sortedPages = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            sortedPages.add(current);
            
            for (int nei : adjacencyList.get(current)) {
                inDegree.put(nei, inDegree.get(nei) - 1);
                if (inDegree.get(nei) == 0) {
                    queue.offer(nei);
                }
            }
        }
        
        return sortedPages;
    }
}
