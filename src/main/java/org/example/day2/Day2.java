package org.example.day2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Day2 {

    public static final String SPLIT_DELIMITER = " ";

    public static void main(String[] args) throws URISyntaxException, IOException {
        Path path = Paths.get(
                Objects.requireNonNull(
                        Day2.class.getClassLoader().getResource("day2/input")
                ).toURI()
        );

        List<List<Integer>> data = collectData(path);

        long count = data.stream().filter(Day2::isReportSafe).count();

        System.out.println(count);
    }

    private static boolean isReportSafe(List<Integer> report) {
        for (int i = 0; i < report.size(); i++) {
            List<Integer> potentialSafe = new ArrayList<>(report);
            potentialSafe.remove(i);
            if (isSequenceValid(potentialSafe)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSequenceValid(List<Integer> sequence) {
        if (sequence.size() < 2) return true;
        
        boolean isDescending = sequence.get(0) > sequence.get(1);
        
        for (int i = 0; i < sequence.size() - 1; i++) {
            int diff = isDescending? sequence.get(i) - sequence.get(i + 1) : sequence.get(i + 1) - sequence.get(i);
            if (diff < 1 || diff > 3) return false;
        }
        return true;
    }

    private static List<List<Integer>> collectData(Path path) throws IOException {
        List<List<Integer>> data = new ArrayList<>();
        try(Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> {
                List<Integer> parts = new ArrayList<>(
                        Arrays.stream(line.split(SPLIT_DELIMITER)).map(Integer::parseInt).toList());
                data.add(parts);
            });
        }
        return data;
    }

}
