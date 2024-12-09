package org.example.day6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Day6 {
    record Position(int row, int col) {}

    enum Direction {
        UP, DOWN, LEFT, RIGHT;

        Direction turnRight() {
            return switch (this) {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };
        }

        Position move(Position p) {
            return switch (this) {
                case UP -> new Position(p.row() - 1, p.col());
                case RIGHT -> new Position(p.row(), p.col() + 1);
                case DOWN -> new Position(p.row() + 1, p.col());
                case LEFT -> new Position(p.row(), p.col() - 1);
            };
        }
    }

    record State(Position pos, Direction dir) {}

    record PosAndChar(Position pos, char ch) {}

    public static void main(String[] args) throws IOException {
        char[][] map;
        try (InputStream in = Day6.class.getResourceAsStream("/day6/input");
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in)))) {
            map = reader.lines().map(String::toCharArray).toArray(char[][]::new);
        }

        Position startPos = null;
        Direction startDir = Direction.UP;

        outer:
        for (int r = 0; r < map.length; r++) {
            char[] line = map[r];
            for (int c = 0; c < line.length; c++) {
                char ch = line[c];
                if (ch == '^') {
                    startPos = new Position(r, c);
                    break outer;
                }
            }
        }

        Set<Position> originalPath = getPositions(startPos, startDir, map);
        
        int loopCount = 0;
        for (Position p : originalPath) {
            if (p.equals(startPos) || map[p.row()][p.col()] == '#') {
                continue;
            }
            char originalChar = map[p.row()][p.col()];
            map[p.row()][p.col()] = '#';
            
            if (causesLoop(map, startPos, startDir)) {
                loopCount++;
            }

            map[p.row()][p.col()] = originalChar;
        }

        System.out.println(originalPath.size());
        System.out.println(loopCount);
    }

    private static boolean causesLoop(char[][] map, Position guardPos, Direction guardDir) {
        Set<State> visited = new HashSet<>();
        visited.add(new State(guardPos, guardDir));

        while (true) {
            PosAndChar posAndChar = getNextPosAndChar(guardPos, guardDir, map);
            if (posAndChar == null) break;
            if (posAndChar.ch() == '#') {
                guardDir = guardDir.turnRight();
            } else {
                guardPos = posAndChar.pos();
            }
            
            if (!visited.add(new State(guardPos, guardDir))) {
                return true;
            }
        }
        
        return false;
    }

    private static Set<Position> getPositions(Position guardPos, Direction guardDir, char[][] map) {
        
        Set<Position> visited = new HashSet<>();
        visited.add(guardPos);

        while (true) {
            PosAndChar posAndChar = getNextPosAndChar(guardPos, guardDir, map);
            if (posAndChar == null) break;
            if (posAndChar.ch() == '#') {
                guardDir = guardDir.turnRight();
            } else {
                guardPos = posAndChar.pos();
                visited.add(guardPos);
            }
        }
        return visited;
    }

    private static PosAndChar getNextPosAndChar(Position guardPos, Direction guardDir, char[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        Position nextPos = guardDir.move(guardPos);

        if (nextPos.row() < 0 || nextPos.row() >= rows
                || nextPos.col() < 0 || nextPos.col() >= cols) {
            return null;
        }

        char nextChar = map[nextPos.row()][nextPos.col()];
        return new PosAndChar(nextPos, nextChar);
    }
}
