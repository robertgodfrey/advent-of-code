/*
 * Advent of Code 2024
 * Day 6: Guard Gallivant
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;


class Day06 {
    private ArrayList<String> map = new ArrayList<>();
    private enum Direction {UP, DOWN, LEFT, RIGHT};
    private int startingPosRow;
    private int startingPosCol;
    private int currentPosRow;
    private int currentPosCol;
    private Direction currentDirection;

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                map.add(fileReader.nextLine());
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void findStartingPosition() {
        boolean found = false;
        for (int row = 0; row < map.size(); row++) {
            for (int col = 0; col < map.size(); col++) {
                if (map.get(row).charAt(col) == '^') {
                    startingPosRow = row;
                    startingPosCol = col;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
    }

    private boolean nextMoveReachesExit() {
        switch (currentDirection) {
            case UP:
                if (currentPosRow == 0) {
                    return true;
                }
                if (map.get(currentPosRow - 1).charAt(currentPosCol) == '#') {
                    currentDirection = Direction.RIGHT;
                } else {
                    currentPosRow--;
                }
                break;
            case DOWN:
                if (currentPosRow == map.size() - 1) {
                    return true;
                }
                if (map.get(currentPosRow + 1).charAt(currentPosCol) == '#') {
                    currentDirection = Direction.LEFT;
                } else {
                    currentPosRow++;
                }
                break;
            case RIGHT:
                if (currentPosCol == map.get(0).length() - 1) {
                    return true;
                }
                if (map.get(currentPosRow).charAt(currentPosCol + 1) == '#') {
                    currentDirection = Direction.DOWN;
                } else {
                    currentPosCol++;
                }
                break;
            default: // LEFT
                if (currentPosCol == 0) {
                    return true;
                }
                if (map.get(currentPosRow).charAt(currentPosCol - 1) == '#') {
                    currentDirection = Direction.UP;
                } else {
                    currentPosCol--;
                }
        }
        return false;
    }

    private boolean hasLoop() {
        ArrayList<String> tempMap = new ArrayList<>(map);
        ArrayList<String> modifiedMap = new ArrayList<>(map);
        Set<String> visitedPosDirs = new HashSet<>();
        modifiedMap.set(currentPosRow, map.get(currentPosRow).substring(0, currentPosCol) + "#" + map.get(currentPosRow).substring(currentPosCol + 1));
        String currentPosDir = String.format("%d,%d:%s", currentPosRow, currentPosCol, currentDirection.name());
        Direction tempDirection = currentDirection;
        int tempPosRow = currentPosRow;
        int tempPosCol = currentPosCol;
        boolean reachedExit = false;
        map = modifiedMap;
        currentPosRow = startingPosRow;
        currentPosCol = startingPosCol;
        currentDirection = Direction.UP;
        while (!visitedPosDirs.contains(currentPosDir) && !reachedExit) {
            visitedPosDirs.add(currentPosDir);
            reachedExit = nextMoveReachesExit();
            currentPosDir = String.format("%d,%d:%s", currentPosRow, currentPosCol, currentDirection.name());
        }
        map = tempMap;
        currentPosRow = tempPosRow;
        currentPosCol = tempPosCol;
        currentDirection = tempDirection;
        return !reachedExit;
    }

    private void solve() {
        Set<String> visitedPos = new HashSet<>();
        String currentPos = String.format("%d,%d", currentPosRow, currentPosCol);
        int possibleObstructionPositions = 0;
        boolean reachedExit = false;
        currentDirection = Direction.UP;
        currentPosRow = startingPosRow;
        currentPosCol = startingPosCol;
        while (!reachedExit) {
            if (!visitedPos.contains(currentPos)) {
                possibleObstructionPositions += hasLoop() ? 1 : 0;
                visitedPos.add(currentPos);
            }
            reachedExit = nextMoveReachesExit();
            currentPos = String.format("%d,%d", currentPosRow, currentPosCol);
        }
        System.out.println(String.format("Part one: %d", visitedPos.size() + 1));
        System.out.println("Part two: " + possibleObstructionPositions);
    }

    public static void main(String[] args) {
        Day06 solution = new Day06();
        solution.getInput();
        solution.findStartingPosition();
        solution.solve();
    }
}

