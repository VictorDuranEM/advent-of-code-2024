package org.example.day3;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day3 {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Path path = Paths.get(Objects.requireNonNull(
                Day3.class.getClassLoader().getResource("day3/input")
        ).toURI());

        String patternString = "do\\(\\)|don't\\(\\)|mul\\((\\d+),(\\d+)\\)";
        Pattern linePattern = Pattern.compile(patternString);
        
        try (Stream<String> lines = Files.lines(path)) {
            long sum = 0;

            boolean mulEnabled = true;  // Start with mul enabled
            for (String line : (Iterable<String>) lines::iterator) {
                Matcher matcher = linePattern.matcher(line);

                while (matcher.find()) {
                    String match = matcher.group(0);

                    if ("do()".equals(match)) {
                        mulEnabled = true;
                    } else if ("don't()".equals(match)) {
                        mulEnabled = false;
                    } else if (match.startsWith("mul(") && mulEnabled) {
                        long x = Long.parseLong(matcher.group(1));
                        long y = Long.parseLong(matcher.group(2));
                        sum += x * y;
                    }
                }
            }

            System.out.println("Sum of all multiplications: " + sum);
        }

    }
}
