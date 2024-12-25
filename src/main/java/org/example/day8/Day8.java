package org.example.day8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Day8 {
    record Point(int x, int y) {}

    public static void main(String[] args) throws IOException {
        List<String> lines;
        try (var input = Day8.class.getResourceAsStream("/day8/input");
             var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(input)))) {
            lines = reader.lines().toList();
        }

        int height = lines.size();
        int width = lines.isEmpty() ? 0 : lines.getFirst().length();

        // Collect all antennas grouped by frequency
        Map<Character, List<Point>> antennasByFreq = new HashMap<>();
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                char ch = line.charAt(x);
                if (ch != '.') {
                    antennasByFreq.computeIfAbsent(ch, k -> new ArrayList<>()).add(new Point(x, y));
                }
            }
        }
        
        // Process each group of antennas
        Set<Point> resonancePoints = getPoints(antennasByFreq, width, height);
        Set<Point> resonancePoints2 = getPoints2(antennasByFreq, width, height);
        System.out.println(resonancePoints.size());
        System.out.println(resonancePoints2.size());
    }

    private static Set<Point> getPoints(Map<Character, List<Point>> antennasByFreq, int width, int height) {
        Set<Point> resonancePoints = new HashSet<>();
        for (var entry : antennasByFreq.entrySet()) {
            List<Point> antennas = entry.getValue();
            
            for (int i = 0; i < antennas.size(); i++) {
                for (int j = i + 1; j < antennas.size(); j++) {
                    Point A = antennas.get(i);
                    Point B = antennas.get(j);

                    int dx = B.x() - A.x();
                    int dy = B.y() - A.y();
                    
                    // Resonance point away from A 
                    {
                        int x = A.x() - dx;
                        int y = A.y() - dy;
                        if (x >= 0 && x < width && y >= 0 && y < height) {
                            resonancePoints.add(new Point(x, y));
                        }
                    }

                    // Resonance point away from B 
                    {
                        int x = B.x() + dx;
                        int y = B.y() + dy;
                        if (x >= 0 && x < width && y >= 0 && y < height) {
                            resonancePoints.add(new Point(x, y));
                        }
                    }
                }
            }
        }
        return resonancePoints;
    }

    private static Set<Point> getPoints2(Map<Character, List<Point>> antennasByFreq, int width, int height) {
        Set<Point> resonancePoints = new HashSet<>();
        for (var entry : antennasByFreq.entrySet()) {
            List<Point> antennas = entry.getValue();

            for (int i = 0; i < antennas.size(); i++) {
                for (int j = i + 1; j < antennas.size(); j++) {
                    Point A = antennas.get(i);
                    Point B = antennas.get(j);
                    
                    resonancePoints.add(new Point(A.x(), A.y()));
                    resonancePoints.add(new Point(B.x(), B.y()));

                    int dx = B.x() - A.x();
                    int dy = B.y() - A.y();

                    // Resonance points away from A 
                    {
                        int x = A.x();
                        int y = A.y();
                        while (true) {
                            x -= dx;
                            y -= dy;
                            if (x < 0 || x >= width || y < 0 || y >= height) {
                                break;
                            }
                            resonancePoints.add(new Point(x, y));
                        }
                    }

                    // Resonance points away from B 
                    {
                        int x = B.x();
                        int y = B.y();
                        while (true) {
                            x += dx;
                            y += dy;
                            if (x < 0 || x >= width || y < 0 || y >= height) {
                                break;
                            }
                            resonancePoints.add(new Point(x, y));
                        }
                    }
                }
            }
        }
        return resonancePoints;
    }
}
