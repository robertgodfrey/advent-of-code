/*
 * Advent of Code 2024
 * Day 18: RAM Run
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Day18 {
    private final Queue<int[]> pointsToCheck = new LinkedList<>();
    private final ArrayList<int[]> fallingBytes = new ArrayList<>();
    private final int NUM_FALLING_BYTES = 1024;
    private final int GRID_SIZE = 71;
    private int[][][] memoryGrid;
    private int[][][] workingMemoryGrid;

    private void initializeMap() {
        memoryGrid = new int[GRID_SIZE][GRID_SIZE][3];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                memoryGrid[i][j] = new int[] {0, 0, Integer.MAX_VALUE - 1};  // danger, visited, distance
            }
        }
        memoryGrid[0][0][2] = 0;
    }

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                fallingBytes.add(new int[] {
                        Integer.parseInt(line.split(",")[0]),
                        Integer.parseInt(line.split(",")[1])
                });
            }
            for (int i = 0; i < NUM_FALLING_BYTES; i++) {
                int[] obstacle = fallingBytes.get(i);
                memoryGrid[obstacle[1]][obstacle[0]][0] = 1;
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkAndAdd(int oldPosX, int oldPosY, int newPosX, int newPosY) {
        if (newPosX >= 0 && newPosX < GRID_SIZE
                && newPosY >= 0 && newPosY < GRID_SIZE
                && workingMemoryGrid[newPosY][newPosX][0] == 0
                && workingMemoryGrid[newPosY][newPosX][1] == 0) {
            workingMemoryGrid[newPosY][newPosX][1] = 1;
            workingMemoryGrid[newPosY][newPosX][2] = Math.min(workingMemoryGrid[newPosY][newPosX][2], workingMemoryGrid[oldPosY][oldPosX][2] + 1);
            pointsToCheck.add(new int[] {newPosX, newPosY});
        }
    }

    private void findPath(int posX, int posY) {
        checkAndAdd(posX, posY, posX, posY - 1); // up
        checkAndAdd(posX, posY, posX, posY + 1); // down
        checkAndAdd(posX, posY, posX - 1, posY); // left
        checkAndAdd(posX, posY, posX + 1, posY); // right
    }

    private void resetWorkingGrid() {
        workingMemoryGrid = new int[GRID_SIZE][GRID_SIZE][3];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                workingMemoryGrid[i][j] = new int[]{memoryGrid[i][j][0], memoryGrid[i][j][1], memoryGrid[i][j][2]};
            }
        }
    }

    private int partOne() {
        resetWorkingGrid();
        pointsToCheck.add(new int[] {0,0});
        while (!pointsToCheck.isEmpty()) {
            int[] point = pointsToCheck.poll();
            findPath(point[0], point[1]);
        }
        return workingMemoryGrid[GRID_SIZE - 1][GRID_SIZE - 1][2];
    }

    private String partTwo() {
        for (int i = NUM_FALLING_BYTES + 1; i < fallingBytes.size(); i++) {
            resetWorkingGrid();
            for (int j = NUM_FALLING_BYTES + 1; j < i; j++) {
                int[] byteObstacle = fallingBytes.get(j);
                workingMemoryGrid[byteObstacle[1]][byteObstacle[0]][0] = 1;
            }
            pointsToCheck.clear();
            pointsToCheck.add(new int[] {0,0});
            while (!pointsToCheck.isEmpty()) {
                int[] point = pointsToCheck.poll();
                findPath(point[0], point[1]);
            }
            if (workingMemoryGrid[GRID_SIZE - 1][GRID_SIZE - 1][2] == Integer.MAX_VALUE - 1) {
                return String.format("%d,%d", fallingBytes.get(i - 1)[0], fallingBytes.get(i - 1)[1]);
            }
        }
        return "¯\\_(ツ)_/¯";
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day18 solution = new Day18();
        solution.initializeMap();
        solution.getInput();
        System.out.printf("Part one: %d\n", solution.partOne());
        System.out.printf("Part two: %s\n", solution.partTwo());
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

