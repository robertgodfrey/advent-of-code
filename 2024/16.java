/*
 * Advent of Code 2024
 * Day 16: Reindeer Maze
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class Day16 {
    private final ArrayList<String> maze = new ArrayList<>();
    private final HashMap<String, Integer> posDirScores = new HashMap<>();
    private final Set<String> bestPathPositions = new HashSet<>();
    private final int NORTH = 1;
    private final int SOUTH = 2;
    private final int EAST = 3;
    private final int WEST = 4;

    private int minScore = 99999999;

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                maze.add(fileReader.nextLine());
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int dfs(int posX, int posY, int dir, int score, int depth, String path) {
        if (depth > 1000 || score > minScore) {
            return 999999999;
        }
        if (maze.get(posY).charAt(posX) == 'E') {
            if (score < minScore) {
                minScore = score;
                bestPathPositions.clear();
            }
            bestPathPositions.addAll(Arrays.asList(path.split(";")));
            return score;
        }
        if (posDirScores.containsKey(String.format("%d,%d:%d", posX, posY, dir))
                && score > posDirScores.get(String.format("%d,%d:%d", posX, posY, dir))) {
            return 999999999;
        }
        posDirScores.put(String.format("%d,%d:%d", posX, posY, dir), score);
        int upScore = 999999999;
        int downScore = 999999999;
        int leftScore = 999999999;
        int rightScore = 999999999;
        if (maze.get(posY - 1).charAt(posX) != '#') {
            upScore = dfs(posX, posY - 1, NORTH, dir == NORTH ? score + 1 : score + 1001, depth + 1,
                    path + String.format(";%d,%d", posX, posY - 1));
        }
        if (maze.get(posY + 1).charAt(posX) != '#') {
            downScore = dfs(posX, posY + 1, SOUTH, dir == SOUTH ? score + 1 : score + 1001, depth + 1,
                    path + String.format(";%d,%d", posX, posY + 1));
        }
        if (maze.get(posY).charAt(posX - 1) != '#') {
            leftScore = dfs(posX - 1, posY, WEST, dir == WEST ? score + 1 : score + 1001, depth + 1,
                    path + String.format(";%d,%d", posX - 1, posY));
        }
        if (maze.get(posY).charAt(posX + 1) != '#') {
            rightScore = dfs(posX + 1, posY, EAST, dir == EAST ? score + 1 : score + 1001, depth + 1,
                    path + String.format(";%d,%d", posX + 1, posY));
        }
        return Math.min(Math.min(upScore, downScore), Math.min(leftScore, rightScore));
    }

    private int partOne() {
        int posY = maze.size() - 2;
        int upScore = 999999999;
        int rightScore = 999999999;
        String positions = String.format("%d,%d", 1, posY);
        posDirScores.put(String.format("%d,%d:%d", 1, posY, EAST), 0);
        if (maze.get(posY - 1).charAt(1) == '.') {
            upScore = dfs(1, posY - 1, NORTH, 1001, 0, positions);
        }
        if (maze.get(posY).charAt(2) == '.') {
            rightScore = dfs(2, posY, EAST, 1, 0, positions);
        }
        return Math.min(upScore, rightScore);
    }

    private int partTwo() {
        return bestPathPositions.size() + 1;
    }

    public static void main(String[] args) {
        Day16 solution = new Day16();
        solution.getInput();
        int bestPathVal = solution.partOne();
        System.out.printf("Part one: %d\n", bestPathVal);
        System.out.printf("Part two: %d\n", solution.partTwo());
    }
}

