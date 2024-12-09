package org.example.day1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day1 {

    public static void main(String[] args) {
        try {
            Path path = Paths.get(Objects.requireNonNull(Day1.class
                                                                 .getClassLoader()
                                                                 .getResource("day1/input"))
                                          .toURI());
            List<List<Long>> data = collectData(path);
            
            long distance = calculateTotalDistance(data.getFirst(), data.getLast());
            System.out.println("Total distance: " + distance);
            
            long similarityScore = calculateSimilarityScore(data.getFirst(), data.getLast());
            System.out.println("Similarity score: " + similarityScore);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static List<List<Long>> collectData(Path path) throws IOException {
        try(Stream<String> lines = Files.lines(path)) {
            List<Long> firstColumn = new ArrayList<>();
            List<Long> secondColumn = new ArrayList<>();

            lines.forEach(line -> {
                String[] parts = line.split("\\s+");
                firstColumn.add(Long.parseLong(parts[0].trim()));
                secondColumn.add(Long.parseLong(parts[1].trim()));
            });

            Collections.sort(firstColumn);
            Collections.sort(secondColumn);

            return List.of(firstColumn, secondColumn);
        }
    }

    private static long calculateTotalDistance(List<Long> first, List<Long> last) {
        return IntStream.range(0, first.size())
                .mapToLong(i -> Math.abs(first.get(i) - last.get(i)))
                .sum();
    }

    private static long calculateSimilarityScore(List<Long> first, List<Long> last) {
        return first.stream()
                .mapToLong(item -> item * Collections.frequency(last, item))
                .sum();
    }
}
