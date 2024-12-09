package org.example.day4;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class Day4 {
    public static final String TARGET_WORD = "XMAS";
    public static final int[] DIR_X = {0, 0, 1, -1, 1, 1, -1, -1};
    public static final int[] DIR_Y = {1, -1, 0, 0, 1, -1, 1, -1};

    public static void main(String[] args) throws URISyntaxException, IOException {
        Path path = Paths.get(Objects.requireNonNull(
                Day4.class.getClassLoader().getResource("day4/input")
        ).toURI());


        char[][] grid = readGrid(path);

        System.out.println("Total occurrences of XMAS: " + countOccurrences(grid));

        System.out.println("Total occurrences of X-MAS: " + countXMas(grid));

    }

    private static int countXMas(char[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int count = 0;

        for (int x = 1; x < n - 1; x++) {
            for (int y = 1; y < m - 1; y++) {
                if (grid[x][y] == 'A') {
                    char TL = grid[x - 1][y - 1];
                    char TR = grid[x - 1][y + 1];
                    char BL = grid[x + 1][y - 1];
                    char BR = grid[x + 1][y + 1];

                    boolean diagonal1Valid = (TL == 'M' && BR == 'S') || (TL == 'S' && BR == 'M');
                    boolean diagonal2Valid = (BL == 'M' && TR == 'S') || (BL == 'S' && TR == 'M');

                    if (diagonal1Valid && diagonal2Valid) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private static int countOccurrences(char[][] grid) {
        int count = 0;
        int n = grid.length;
        int m = grid[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int dir = 0; dir < 8; dir++) {
                    if (checkWord(grid, i, j, dir)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static boolean checkWord(char[][] grid, int startX, int startY, int dir) {
        int n = grid.length;
        int m = grid[0].length;

        int x = startX;
        int y = startY;

        for (int k = 0; k < TARGET_WORD.length(); k++) {
            if (grid[x][y] != TARGET_WORD.charAt(k)) {
                return false;
            }
            x += DIR_X[dir];
            y += DIR_Y[dir];

            if (x < 0 || x >= n || y < 0 || y >= m) {
                return false;
            }
        }
        return true;
    }

    private static char[][] readGrid(Path path) throws IOException {
        char[][] grid;
        try (Stream<String> lines = Files.lines(path)) {
            grid = lines.map(String::toCharArray).toArray(char[][]::new);
        }
        return grid;
    }
}
